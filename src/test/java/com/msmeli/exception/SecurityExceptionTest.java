package com.msmeli.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecurityExceptionTest {

    /**
     * Prueba la creación de una instancia de SecurityException y verifica que los valores se han establecido correctamente
     * utilizando los métodos getter.
     */
    @Test
    public void testSecurityException() {
        // Crear una instancia de SecurityException
        SecurityException securityException = new SecurityException("Mensaje de error", "Zona", 123);

        // Verificar que los valores se han establecido correctamente utilizando los métodos getter
        assertEquals("Mensaje de error", securityException.getMessage());
        assertEquals("Zona", securityException.getZone());
        assertEquals(123, securityException.getCode());
    }

    /**
     * Prueba la creación de una instancia de SecurityException con causa y verifica que los valores se han establecido
     * correctamente utilizando los métodos getter. Además, se verifica que la causa también se ha establecido correctamente.
     */
    @Test
    public void testSecurityExceptionWithCause() {
        // Crear una instancia de SecurityException con causa
        SecurityException securityExceptionWithCause = new SecurityException("Mensaje de error", new RuntimeException("Causa"), "Zona", 123);

        // Verificar que los valores se han establecido correctamente utilizando los métodos getter
        assertEquals("Mensaje de error", securityExceptionWithCause.getMessage());
        assertEquals("Zona", securityExceptionWithCause.getZone());
        assertEquals(123, securityExceptionWithCause.getCode());

        // Verificar que la causa también se ha establecido correctamente
        assertEquals("Causa", securityExceptionWithCause.getCause().getMessage());
    }
}
