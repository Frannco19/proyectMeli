package com.msmeli.service.implement;

import com.msmeli.exception.ResourceNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    public void testSendMailSuccess() throws Exception {
        when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));

        String result = emailService.sendMail("to@example.com", "subject", "body");

        assertEquals("Mail sent succesfully", result);
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }

    /**
     * Prueba unitaria para el método sendMail en la clase EmailService cuando ocurre un fallo.
     *
     * Esta prueba verifica el comportamiento del método sendMail en EmailService cuando
     * javaMailSender.createMimeMessage() lanza una excepción. Se espera que el método maneje
     * la excepción correctamente y no intente enviar el mensaje de correo.
     */
    @Test
    public void testSendMailFailure() {
        when(javaMailSender.createMimeMessage()).thenThrow(new RuntimeException("error"));

        assertThrows(Exception.class, () -> {
            emailService.sendMail("to@example.com", "subject", "body");
        });

        verify(javaMailSender, times(0)).send(any(MimeMessage.class));
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
        MockitoAnnotations.initMocks(this);

        String to = "recipient@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        doReturn(null).when(javaMailSender).createMimeMessage();

        assertThrows(ResourceNotFoundException.class, () -> emailService.sendMail(to, subject, body));
    }
}
