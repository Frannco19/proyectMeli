package com.msmeli.service.implement;

import com.msmeli.exception.ResourceNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

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

    /**
     * Prueba unitaria para verificar el comportamiento del método sendMail en EmailService al lanzar una excepción de tipo ResourceNotFoundException.
     * Se inicializan los mocks necesarios para la prueba.
     * Se simula la información de correo electrónico para la prueba, incluyendo el destinatario, el asunto y el cuerpo del mensaje.
     * Se simula el lanzamiento de una excepción al llamar al método send de JavaMailSender.
     * Se llama al método que se está probando y se verifica que se lance la excepción esperada de tipo ResourceNotFoundException.
     *
     * @throws MessagingException Excepción lanzada por el método send de JavaMailSender (simulada para la prueba).
     */
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
