package com.example.actionprice.customerService.post;

import com.example.actionprice.customerService.post.dto.PostListDTO;
import com.example.actionprice.customerService.post.dto.PostSimpleDTO;
import com.example.actionprice.exception.PostNotFoundException;
import com.example.actionprice.exception.UserNotFoundException;
import com.example.actionprice.user.User;
import com.example.actionprice.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

/**
 * @author 연상훈
 * @created 2024-10-27 오후 2:50
 */
@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 게시글 생성 기능
     * @param postForm PostForm(valid : PostCreateGroup)
     * @author 연상훈
     * @created 2024-10-27 오후 2:57
     * @throws UserNotFoundException
     * @info
     * 게시글 생성 후 자기가 만든 게시글을 확인해야 하니, PostDetail로 보내줘야 하는데
     * 여기서 PostDetail을 반환하는 것보다 postId만 가지고 리다이렉트 시켜서 보내는 편이 더 효율적이라
     * postId만 반환해줌.
     */
    @Override
    public PostSimpleDTO createPost(PostForm postForm) {
        log.info("[class] PostServiceImpl - [method] createPost - postForm : " + postForm.toString());
        String username = postForm.getUsername();

        User user = userRepository.findById(username)
            .orElseThrow(() -> new UserNotFoundException(username));

        Post post = Post.builder()
                .user(user)
                .title(postForm.getTitle())
                .content(postForm.getContent())
                .published(postForm.isPublished())
                .build();

        post = postRepository.save(post); // 본래 불필요한 로직이지만, 레포지토리에 저장을 해야만 id가 생기고, id가 있어야 프론트에서 리다이렉트가 가능함

        user.addPost(post); // 그리고 postId가 있어야만 user의 postSet에 등록 가능
        userRepository.save(user); // post가 연결된 상태를 save

        return convertPostToSimpleDTO(username, post, 0);
    }

    /**
     * 게시글 수정 페이지로 이동할 때 해당 게시글의 정보를 출력하는 기능
     * @param postId
     * @param logined_username 로그인 중인 사용자의 username
     * @author 연상훈
     * @created 2024-10-27 오후 3:02
     * @throws PostNotFoundException
     */
    @Override
    public PostSimpleDTO goUpdatePost(Integer postId, String logined_username, boolean isAdmin) {
        Post post = getPostOrThrowException(postId);

        String owner_username = post.getUser().getUsername();

        // 비공개글이면
        if (!post.isPublished()) {
            checkIfPostOwnerOrAdmin(owner_username, logined_username, isAdmin);
        }
        
        // 게시글 수정하러 가는데, commentSize는 알 필요가 없으니 그냥 0으로 고정.
        // commentSet 불러 오는 것도 repository 조회가 필요하고, getSize()로 크기 계산하는 것도 다 코드 낭비임
        return convertPostToSimpleDTO(owner_username, post, 0);
    }

    /**
     * 게시글 수정 기능
     * @param postId
     * @param postForm PostForm(valid : PostUpdateGroup)
     * @author 연상훈
     * @created 2024-10-27 오후 3:11
     * @throws PostNotFoundException
     */
    @Override
    public PostSimpleDTO updatePost(Integer postId, PostForm postForm, String logined_username, boolean isAdmin) {

        Post post = getPostOrThrowException(postId);

        String owner_username = post.getUser().getUsername();

        // 비공개글이면
        if (!post.isPublished()) {
            checkIfPostOwnerOrAdmin(owner_username, logined_username, isAdmin);
        }

        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());

        post = postRepository.save(post);

        return convertPostToSimpleDTO(owner_username, post, 0);
    }

    /**
     * 게시글 삭제 기능
     * @param postId
     * @author 연상훈
     * @created 2024-10-27 오후 3:11
     * @throws PostNotFoundException
     */
    @Override
    public PostSimpleDTO deletePost(Integer postId, String logined_username, boolean isAdmin) {

        log.info("[class] PostServiceImpl - [method] deletePost - postId : {}", postId);
        Post post = getPostOrThrowException(postId);

        String owner_username = post.getUser().getUsername();

        // 비공개글이면
        if (!post.isPublished()) {
            checkIfPostOwnerOrAdmin(owner_username, logined_username, isAdmin);
        }

        postRepository.delete(post);

        return convertPostToSimpleDTO(owner_username, post, 0);
    }

    /**
     * 게시글 내용 및 해당 게시글에 연결된 comment의 목록을 출력하는 기능
     * @author 연상훈
     * @created 2024-10-27 오후 3:14
     * @info
     */
    @Override
    public PostSimpleDTO getDetailPost(Integer postId, int page, String logined_username, boolean isAdmin) {
        log.info(
                "[class] PostServiceImpl - [method] getDetailPost - postId : {} | logined_username : {} | isAdmin : {}",
                postId,
                logined_username,
                isAdmin
        );

        Post post = getPostOrThrowException(postId);

        String owner_username = post.getUser().getUsername();

        // 비공개글이면
        if (!post.isPublished()) {
            log.info("비공개글입니다.");
            checkIfPostOwnerOrAdmin(owner_username, logined_username, isAdmin);
        }

        return convertPostToSimpleDTO(owner_username, post, page);
    }

    /**
     * 게시글 목록을 출력하는 기능
     * @param pageNum 페이지 번호
     * @param keyword 검색 키워드. post의 username과 title에서 검색됨
     * @author 연상훈
     * @created 2024-10-27 오후 3:21
     * @info Page<Post>를 PostListDTO로 변환하는 과정은 PostListDTO의 생성자에서 처리함
     */
    @Override
    public PostListDTO getPostList(int pageNum, String keyword) {
        log.info("[class] PostServiceImpl - [method] getPostList -  - page : {} | keyword : {}", pageNum, keyword);
        Pageable pageable = PageRequest.of(pageNum, 10, Sort.by(Sort.Order.desc("postId")));
        Page<Post> postPage = null;

        if (keyword == null || keyword.isEmpty()) {
            // 키워드가 없을 경우 전체 목록 반환
            keyword = "";
            postPage = postRepository.findAll(pageable);
        } else {
            // 키워드가 있을 경우 post의 title과 username 에서 키워드를 검색
            postPage = postRepository.findByTitleContainingOrUser_UsernameContaining(keyword, keyword, pageable);
        }

        return new PostListDTO(postPage, keyword);
    }

    /**
     * 게시글 목록을 출력하는 기능(MyPage)
     * @param username MyPage의 username. post의 username에서 검색됨
     * @param pageNum 페이지 번호
     * @param keyword 검색 키워드. post의 title에서 검색됨
     * @author 연상훈
     * @created 2024-10-27 오후 3:21
     * @info Page<Post>를 PostListDTO로 변환하는 과정은 PostListDTO의 생성자에서 처리함
     * @info 해당 username을 가진 사람의 게시글을 검색하고, 만약 키워드가 있으면 그 중에서도 title에서 검색
     */
    @Override
    public PostListDTO getPostListForMyPage(String username, String keyword, Integer pageNum) {
        log.info("[class] PostServiceImpl - [method] getPostList > 실행");
        log.info("[class] PostServiceImpl - [method] getPostList - page : {} | keyword : {}", pageNum, keyword);
        Pageable pageable = PageRequest.of(pageNum, 10, Sort.by(Sort.Order.desc("postId")));
        Page<Post> postPage = null;

        if (keyword == null || keyword.isEmpty()) {
            // 키워드가 없을 경우, 해당 사용자가 작성한 게시글의 전체 목록 반환
            keyword = "";
            postPage = postRepository.findByUser_Username(username, pageable);
        } else {
            // 키워드가 있을 경우, 해당 사용자가 작성한 게시글 중 제목에 해당 키워드가 있는 것을 반환
            postPage = postRepository.findByUser_UsernameAndTitleContaining(username, keyword, pageable);
        }

        log.info("[class] PostServiceImpl - [method] getPostList > 완료");

        return new PostListDTO(postPage, keyword);
    }

    /**
     * 사용자가 작성자와 일치하거나 관리자인지 확인하는 메서드
     * @param owner_username 작성자의 사용자 이름
     * @param logined_username 로그인 된 사용자의 사용자 이름
     * @param isAdmin 로그인 된 사용자가 관리자인지 여부
     * @author 연상훈
     * @created 2024-11-30 오후 9:19
     * @info 자주 사용되니까 메서드로 만들어 버림
     * @info 사용자와 작성자가 일치하지 않으면서 관리자도 아니면 접근 거부 예외로 처리함
     */
    private void checkIfPostOwnerOrAdmin(String owner_username, String logined_username, boolean isAdmin) {
        // 사용자 = 작성자 또는 어드민
        if(owner_username.equals(logined_username) || isAdmin) {
            log.info("인증 완료");
            return;
        }

        log.info("인증 실패");

        throw new AccessDeniedException("you are not allowed to access this post");
    }

    /**
     * post 레포지토리에서 post를 가져오거나 예외 처리하는 메서드
     * @param postId
     * @author 연상훈
     * @created 2024-11-30 오후 9:18
     * @info 자주 사용되니까 메서드로 만들어 버림
     */
    private Post getPostOrThrowException(Integer postId){
        return postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));
    }

    /**
     * post 객체를 프론트로 전달할 simpleDTO로 변환하는 메서드
     * @param username
     * @param post
     * @param page
     * @author 연상훈
     * @created 2024-11-30 오후 9:16
     * @info 자주 사용되니까 메서드로 만들어 버림
     * @info post.getUser().getUsername으로 하면 불필요한 호출 및 메서드 사용이기 때문에
     * 어차피 사용자 및 작성자 검증에서 사용되는 username을 매개변수로 받음
     */
    private PostSimpleDTO convertPostToSimpleDTO(String username, Post post, int page) {
        return PostSimpleDTO.builder()
            .postId(post.getPostId())
            .title(post.getTitle())
            .content(post.getContent())
            .published(post.isPublished())
            .username(username)
            .createdAt(post.getCreatedAt())
            .page(page)
            .build();
    }

}
