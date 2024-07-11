package com.ibeh.WORLD.BANKING.APPLICATION.service;

import com.ibeh.WORLD.BANKING.APPLICATION.payload.request.CreditAndDebitRequest;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.request.TransferRequest;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.response.BankResponse;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.request.EnquiryRequest;

public interface UserService {

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditAndDebitRequest request);

    BankResponse debitAccount(CreditAndDebitRequest request);

    BankResponse transfer(TransferRequest request);

}
