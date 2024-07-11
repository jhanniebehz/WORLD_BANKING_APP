package com.ibeh.WORLD.BANKING.APPLICATION.service.impl;

import com.ibeh.WORLD.BANKING.APPLICATION.payload.request.CreditAndDebitRequest;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.request.EmailDetails;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.request.TransferRequest;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.response.AccountInfo;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.response.BankResponse;
import com.ibeh.WORLD.BANKING.APPLICATION.repository.UserRepository;
import com.ibeh.WORLD.BANKING.APPLICATION.service.EmailService;
import com.ibeh.WORLD.BANKING.APPLICATION.utils.AccountUtils;
import com.ibeh.WORLD.BANKING.APPLICATION.domain.entity.UserEntity;
import com.ibeh.WORLD.BANKING.APPLICATION.payload.request.EnquiryRequest;
import com.ibeh.WORLD.BANKING.APPLICATION.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final EmailService emailService;

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExits = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());


        if(!isAccountExits) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_MESSAGE)
                    .build();
        }

        UserEntity foundUserAccount = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());


        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_NUMBER_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_NUMBER_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUserAccount.getAccountBalance())
                        .accountNumber(enquiryRequest.getAccountNumber())
                        .bankName("WORLD BANKING APP")
                        .accountName(foundUserAccount.getFirstName() +
                                " " + foundUserAccount.getOtherName() +
                                " " + foundUserAccount.getLastName())
                                .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExits = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());


        if(!isAccountExits) {
            return AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_MESSAGE;
        }

        UserEntity foundUserAccount = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUserAccount.getFirstName() +
                " " + foundUserAccount.getOtherName() +
                " " + foundUserAccount.getLastName();

    }

    @Override
    public BankResponse creditAccount(CreditAndDebitRequest request) {
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());

        if(!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_MESSAGE)
                    .build();
        }

        UserEntity userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());

        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));

       /*userToCredit.setFirstName(request.getFirstName());
      * userToCredit.setLastName(request.getLastName());
      */

        userRepository.save(userToCredit);

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT NOTIFICATION")
                .recipient(userToCredit.getEmail())
                .messageBody("Your account has been credited with " + request.getAmount() +
                        " from" + userToCredit.getFirstName() +
                        " your current account balance is " +
                        userToCredit.getAccountBalance())
                .build();

        emailService.sendEmailAlert(creditAlert);


        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " +  userToCredit.getOtherName() + " " + userToCredit.getLastName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(userToCredit.getAccountNumber())
                        .bankName("WORLD BANKING APP")
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditAndDebitRequest request) {
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());

        if(!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_MESSAGE)
                    .build();
        }

        UserEntity userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());

        //check for insufficient balance

        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();

        if(availableBalance.intValue() < debitAmount.intValue()) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_FAILURE_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_FAILURE_MESSAGE)
                    .accountInfo(null)
                    .build();
        } else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));

            userRepository.save(userToDebit);


            EmailDetails debitAlert = EmailDetails.builder()
                    .subject("DEBIT ALERT NOTIFICATION")
                    .recipient(userToDebit.getEmail())
                    .messageBody("The sum of " + request.getAmount() +
                            " has been debited from your account! Your current account balance is " +
                            userToDebit.getAccountBalance())
                    .build();

            emailService.sendEmailAlert(debitAlert);
        }

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToDebit.getFirstName() + userToDebit.getOtherName() + userToDebit.getLastName())
                        .accountBalance(userToDebit.getAccountBalance())
                        .accountNumber(userToDebit.getAccountNumber())
                        .bankName("WORLD BANKING APP")
                        .build())
                .build();
    }

    @Override
    public BankResponse transfer(TransferRequest request) {
        /*
        * 1. first check if the destination account number exists
        * 2. then check if amount to send is available
        * 3.then deduct the amount to send from sender balance
        * 4. then add the sender amount to receiver balance
        * 5. then send a debit and credit alert to both sender and receiver
        *
        * */
        boolean isDestinationAccountExists = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());

        if(!isDestinationAccountExists) {
            return BankResponse.builder()
                    .responseCode("008")
                    .responseMessage("Account Number does not exists")
                    .build();
        }
        UserEntity sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());

        if (request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0){
            return BankResponse.builder()
                    .responseCode("009")
                    .responseMessage("INSUFFICIENT BALANCE")
                    .build();
        }

        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));

        userRepository.save(sourceAccountUser);

        String sourceUsername = sourceAccountUser.getFirstName()
                + " " + sourceAccountUser.getOtherName()
                + " " + sourceAccountUser.getLastName();

        EmailDetails debitAlert = EmailDetails.builder()
                .subject("DEBIT ALERT")
                .recipient(sourceAccountUser.getEmail())
                .messageBody("The Sum of " + request.getAmount() +
                        "has been deducted from your account. Your current balance is " +
                        sourceAccountUser.getAccountBalance())
                .build();

        emailService.sendEmailAlert(debitAlert);

        UserEntity destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));


        userRepository.save(destinationAccountUser);

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(destinationAccountUser.getEmail())
                .messageBody("Your account has been credited with " + " " + request.getAmount()
                + " from" + sourceUsername + "your current account balance is " + destinationAccountUser.getAccountBalance())
                .build();

        emailService.sendEmailAlert(creditAlert);

        return BankResponse.builder()
                .responseCode("200")
                .responseMessage("Transfer Successful")
                .accountInfo(null)
                .build();

    }
}
