package com.msmeli.service.implement;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.msmeli.model.Token;
import com.msmeli.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.util.List;

public class TokenServiceImplTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenServiceImpl tokenService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveToken_FirstTime() {
        // Arrange
        when(tokenRepository.findAll()).thenReturn(List.of());
        when(tokenService.getAccessToken("ADMIN")).thenReturn("someAccessToken");

        // Act
        tokenService.saveToken();

        // Assert
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    public void testSaveToken_ExistingToken() {
        // Arrange
        when(tokenRepository.findAll()).thenReturn(List.of(new Token()));
        when(tokenService.getAccessToken("ADMIN")).thenReturn("existingAccessToken");

        // Act
        tokenService.saveToken();

        // Assert
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    public void testUpdateToken() {
        // Arrange
        Token token = new Token();
        token.setAccess_token("oldAccessToken");
        when(tokenRepository.findByUsername("ADMIN")).thenReturn(token);

        // Act
        tokenService.updateToken("newAccessToken");

        // Assert
        assertEquals("newAccessToken", token.getAccess_token());
        verify(tokenRepository, times(1)).save(token);
    }

    @Test
    public void testGetRefreshToken() {
        // Arrange
        Token token = new Token();
        token.setRefresh_token("someRefreshToken");
        when(tokenRepository.findByUsername("someUser")).thenReturn(token);

        // Act
        String refreshToken = tokenService.getRefreshToken("someUser");

        // Assert
        assertEquals("someRefreshToken", refreshToken);
    }

    @Test
    public void testGetAccessToken() {
        // Arrange
        Token token = new Token();
        token.setAccess_token("someAccessToken");
        when(tokenRepository.findByUsername("someUser")).thenReturn(token);

        // Act
        String accessToken = tokenService.getAccessToken("someUser");

        // Assert
        assertEquals("someAccessToken", accessToken);
    }

    // Add more test cases as needed

}
