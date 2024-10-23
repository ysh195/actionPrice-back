package com.example.actionprice.common;

import java.io.IOException;
import java.util.Map;

public interface CommonService {
  Map<String, String> fetchImages() throws IOException;
}
