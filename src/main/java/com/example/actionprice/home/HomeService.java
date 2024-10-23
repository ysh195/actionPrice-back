package com.example.actionprice.home;

import java.io.IOException;
import java.util.Map;

public interface HomeService {
  Map<String, String> fetchImages() throws IOException;
}
