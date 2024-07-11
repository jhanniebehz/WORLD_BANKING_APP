package com.ibeh.WORLD.BANKING.APPLICATION.service;

import com.ibeh.WORLD.BANKING.APPLICATION.payload.response.BankResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface GeneralUserService {

    ResponseEntity<BankResponse<String>> uploadProfilePics(Long id, MultipartFile multipartFile);


}
