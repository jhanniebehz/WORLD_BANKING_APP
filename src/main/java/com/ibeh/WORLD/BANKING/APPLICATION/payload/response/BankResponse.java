package com.ibeh.WORLD.BANKING.APPLICATION.payload.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class BankResponse<T> {

    private String responseCode;
    private String responseMessage;
    private AccountInfo accountInfo;

    public BankResponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public BankResponse(String responseCode, String responseMessage, AccountInfo accountInfo) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.accountInfo = accountInfo;
    }


    public BankResponse(String responseMessage, String url) {
        this.responseMessage = responseMessage;
    }


}
