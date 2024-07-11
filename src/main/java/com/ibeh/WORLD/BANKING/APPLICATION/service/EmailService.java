package com.ibeh.WORLD.BANKING.APPLICATION.service;

import com.ibeh.WORLD.BANKING.APPLICATION.payload.request.EmailDetails;

public interface EmailService {

    void sendEmailAlert(EmailDetails emailDetails);

}
