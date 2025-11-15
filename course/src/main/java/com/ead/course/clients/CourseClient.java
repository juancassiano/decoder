package com.ead.course.clients;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.ead.course.dtos.CourseDto;
import com.ead.course.dtos.ResponsePageDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.services.UtilsService;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class CourseClient {
  
  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private UtilsService utilsService;

  private String REQUEST_URI = "http://localhost:8087";
  
  public Page<UserDto> getAllUsersByCourse(UUID courseId, Pageable pageable){
    List<UserDto> searchResult = null;
    String url = utilsService.createUrl(courseId, pageable);

    log.debug("GET getAllUsersByCourse: {}", url);
    log.info("GET getAllUsersByCourse: {}", url);
    try {
      ParameterizedTypeReference<ResponsePageDto<UserDto>> responseType = new ParameterizedTypeReference<ResponsePageDto<UserDto>>() {};
      ResponseEntity<ResponsePageDto<UserDto>> result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

      searchResult = result.getBody().getContent();

      log.debug("Response Number of Elements: {}", result.getBody().getContent().size());

    } catch (HttpStatusCodeException e) {      
      log.error("Error request /courses {}", e);
    }
    log.info("Ending request /users courseId {}", courseId);

    return new PageImpl<>(searchResult);
  }

}
