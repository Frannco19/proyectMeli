package com.msmeli.service.services;

import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;

public interface EmailService {
    String sendMail(String to, String subject, String body) throws AppException;
}
