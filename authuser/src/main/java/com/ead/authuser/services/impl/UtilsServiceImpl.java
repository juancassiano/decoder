package com.ead.authuser.services.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ead.authuser.services.UtilsService;

@Service
public class UtilsServiceImpl implements UtilsService {
  @Value("${ead.api.url.courses}")
  private String REQUEST_URL_COURSE;

  public String createUrl(UUID userId, Pageable pageable){
    return "/courses?userId=" + userId + "&page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize()
      + "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");
  }


  
}
