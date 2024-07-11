package com.ibeh.WORLD.BANKING.APPLICATION.service.impl;

import com.ibeh.WORLD.BANKING.APPLICATION.utils.AccountUtils;
import com.ibeh.WORLD.BANKING.APPLICATION.domain.entity.UserEntity;
import com.ibeh.WORLD.BANKING.APPLICATION.domain.enums.Role;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.request.EmailDetails;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.request.LoginRequest;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.request.UserRequest;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.response.AccountInfo;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.response.ApiResponse;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.response.BankResponse;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.response.JwtAuthResponse;
import com.ibeh.WORLD.BANKING.APPLICATION.repository.UserRepository;
import com.ibeh.WORLD.BANKING.APPLICATION.service.AuthService;
import com.ibeh.WORLD.BANKING.APPLICATION.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final EmailService emailService;

    @Override
    public BankResponse registerUser(UserRequest userRequest) {

        if(userRepository.existsByEmail(userRequest.getEmail())){
            BankResponse response = BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .build();

            return response;
        }

        UserEntity newUser = UserEntity.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .gender(userRequest.getGender())
                .phoneNumber(userRequest.getPhoneNumber())
                .address(userRequest.getAddress())
                .BVN(userRequest.getBVN())
                .pin(userRequest.getPin())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccount())
                .bankName("World Bank Limited")
                .accountBalance(BigDecimal.ZERO)
                .status("ACTIVE")
                .profilePicture("https://res.cloudinary.com/dpfqbb9pl/image/upload/v1701260428/maleprofile_ffeep9.png")
                .role(Role.USER)
                .build();

        UserEntity savedUser = userRepository.save(newUser);

        //Add email alert here
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("CONGRATULATIONS!!! your Account Has Been successfully Created. \n " +
                                "Your Account Details: \n" +
                        "Account Name : " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName() +
                                "\n Account Number: " + savedUser.getAccountNumber())
                .build();

        emailService.sendEmailAlert(emailDetails);


        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .bankName(savedUser.getBankName())
                        .accountName(savedUser.getFirstName() + " " +
                                savedUser.getLastName() + " " +
                                savedUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public ResponseEntity<ApiResponse<JwtAuthResponse>> loginUser(LoginRequest loginRequest) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(loginRequest.getEmail());

        EmailDetails loginAlert = EmailDetails.builder()
                .subject("You are logged in")
                .recipient(loginRequest.getEmail())
                .messageBody("You logged into your account. If you did" + "not initiate this request, contact support desk.")
                .build();

        emailService.sendEmailAlert(loginAlert);

        UserEntity user = userEntityOptional.get();

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Login Successfully",
                                JwtAuthResponse.builder()
                                        .accessToken("generate token here")
                                        .tokenType("Bearer")
                                        .id(user.getId())
                                        .email(user.getEmail())
                                        .gender(user.getGender())
                                        .firstName(user.getFirstName())
                                        .lastName(user.getLastName())
                                        .profilePicture(user.getProfilePicture())
                                        .build()
                        )
                );

    }
}
