package com.msmeli.service.implement;

import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements com.msmeli.service.services.EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Metodo que se encarga de envidar un correo electronico a
     * @param to Dirrecion de Email a quien va dirigido
     * @param subject
     * @param body Cuerpo del mensaje
     * @return mensaje de confirmacion
     * @throws AppException
     */
    @Override
    public String sendMail(String to, String subject, String body) throws AppException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body);
            javaMailSender.send(mimeMessage);
        } catch (Exception ex) {
            throw new AppException("Error al enviar Email ", "EmailServiceImpl",000,500);
        }
        return "Mail sent succesfully";
    }
}
