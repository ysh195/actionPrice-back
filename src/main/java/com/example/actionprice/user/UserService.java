package com.example.actionprice.user;

import com.example.actionprice.user.forms.UserRegisterForm;

public interface UserService {
  String createUser(UserRegisterForm userRegisterForm);
  boolean checkUserExistsWithUsername(String username);
  boolean checkUserExistsWithEmail(String email);
}
