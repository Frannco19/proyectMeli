package com.msmeli.service.services;

import com.msmeli.exception.ResourceNotFoundException;

public interface IEmailService {
    String sendMail(String to, String subject, String body) throws ResourceNotFoundException;
}
