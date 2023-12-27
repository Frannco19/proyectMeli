package com.msmeli.service.implement;

import com.msmeli.exception.ResourceNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void testSendMail() throws ResourceNotFoundException, MessagingException {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);

        // Mocking data for the test
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        // Create a spy of JavaMailSenderImpl
        JavaMailSenderImpl mockJavaMailSender = spy(new JavaMailSenderImpl());

        // Mocking a non-null MimeMessage when JavaMailSender's createMimeMessage method is called
        doReturn(new MimeMessage((Session) null)).when(mockJavaMailSender).createMimeMessage();

        // Use the spy in the emailService
        doReturn(mockJavaMailSender).when(emailService).sendMail("fff","fff","fff");

        // Call the method to test
        String result = emailService.sendMail(to, subject, body);

        // Verify the results
        assertEquals("Mail sent successfully", result);

        // Verify that JavaMailSender's send method was called with the expected parameters
        verify(mockJavaMailSender).send(any(MimeMessage.class));
    }


    @Test
    void testSendMailException() throws MessagingException {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);

        // Mocking data for the test
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        // Mocking an exception when JavaMailSender's send method is called
        doReturn(null).when(javaMailSender).createMimeMessage();  // Mock a null MimeMessage

        // Call the method to test and verify that ResourceNotFoundException is thrown
        assertThrows(ResourceNotFoundException.class, () -> emailService.sendMail(to, subject, body));
    }
}
