package com.ibeh.WORLD.BANKING.APPLICATION.infrastructure.controller;

import com.ibeh.WORLD.BANKING.APPLICATION.payload.request.CreditAndDebitRequest;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.request.TransferRequest;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.response.BankResponse;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.request.EnquiryRequest;
import com.ibeh.WORLD.BANKING.APPLICATION.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private  final UserService userService;

    @GetMapping("/balance-enquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.balanceEnquiry(enquiryRequest);
    }

    @GetMapping("/name-enquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.nameEnquiry(enquiryRequest);
    }

    @PostMapping("/credit-account")
    public  BankResponse creditAccount(@RequestBody CreditAndDebitRequest request){
        return userService.creditAccount(request);
    }

    @PostMapping("/debit-account")
    public  BankResponse debitAccount(@RequestBody CreditAndDebitRequest request){
        return userService.debitAccount(request);
    }

    @PostMapping("/transfer")
    public  BankResponse transfer(@RequestBody TransferRequest request){
        return userService.transfer(request);
    }

}
