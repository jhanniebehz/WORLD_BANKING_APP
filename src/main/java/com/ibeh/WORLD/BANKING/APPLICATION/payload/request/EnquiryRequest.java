package com.ibeh.WORLD.BANKING.APPLICATION.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnquiryRequest {

    private String accountNumber;


}
