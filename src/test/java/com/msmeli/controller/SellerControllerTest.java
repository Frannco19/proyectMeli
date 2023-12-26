package com.msmeli.controller;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.DisplayName;
//import org.springframework.http.ResponseEntity;
//import com.msmeli.service.services.SellerService;
//
//import java.util.List;
//
//import com.msmeli.exception.ResourceNotFoundException;
//import com.msmeli.service.services.ItemService;
//import com.msmeli.dto.response.TokenResposeDTO;
//
//import java.util.concurrent.TimeUnit;
//
//import com.msmeli.dto.request.EmployeeUpdateRequestDTO;
//
//import java.lang.reflect.Method;
//import java.lang.reflect.InvocationTargetException;
//import java.time.Duration;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mockStatic;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
//
//public final class SellerControllerTest {
//    ///region Test suites for executable com.msmeli.controller.SellerController.deleteEmployee
//
//    ///region SYMBOLIC EXECUTION: ERROR SUITE for method deleteEmployee(java.lang.Long)
//
//    /**
//     * @utbot.classUnderTest {@link SellerController}
//     * @utbot.methodUnderTest {@link SellerController#deleteEmployee(Long)}
//     * @utbot.invokes {@link com.msmeli.service.services.UserEntityService#deleteEmployee(Long)}
//     * @utbot.throwsException {@link NullPointerException} in: userEntityService.deleteEmployee(employeeId);
//     */
//    @Test
//    @DisplayName("deleteEmployee: userEntityService.deleteEmployee(employeeId) : True -> ThrowNullPointerException")
//    public void testDeleteEmployee_UserEntityServiceDeleteEmployee() {
//        SellerController sellerController = new SellerController(null, null, null);
//
//        /* This test fails because method [com.msmeli.controller.SellerController.deleteEmployee] produces [java.lang.NullPointerException]
//            com.msmeli.controller.SellerController.deleteEmployee(SellerController.java:91) */
//        sellerController.deleteEmployee(null);
//    }
//    ///endregion
//
//    ///region Errors report for deleteEmployee
//
//    public void testDeleteEmployee_errors() {
//        // Couldn't generate some tests. List of errors:
//        //
//        // 76 occurrences of:
//        // Default concrete execution failed
//
//    }
//    ///endregion
//
//    ///endregion
//
//    ///region Test suites for executable com.msmeli.controller.SellerController.getAllEmployees
//
//    ///region SYMBOLIC EXECUTION: SUCCESSFUL EXECUTIONS for method getAllEmployees()
//
//    /**
//     * @utbot.classUnderTest {@link SellerController}
//     * @utbot.methodUnderTest {@link SellerController#getAllEmployees()}
//     * @utbot.invokes {@link SellerService#getAllEmployees()}
//     * @utbot.invokes {@link ResponseEntity#ok(Object)}
//     * @utbot.returnsFrom {@code return ResponseEntity.ok(employeesList);}
//     */
//    @Test
//    @DisplayName("getAllEmployees: SellerServiceGetAllEmployees -> return ResponseEntity.ok(employeesList)")
//    public void testGetAllEmployees_ResponseEntityOk() {
//        org.mockito.MockedStatic mockedStatic = null;
//        try {
//            mockedStatic = mockStatic(ResponseEntity.class);
//            (mockedStatic.when(() -> ResponseEntity.ok(any()))).thenReturn(((ResponseEntity) null));
//            SellerService sellerServiceMock = mock(SellerService.class);
//            (when(sellerServiceMock.getAllEmployees())).thenReturn(((List) null));
//            SellerController sellerController = new SellerController(sellerServiceMock, null, null);
//
//            ResponseEntity actual = sellerController.getAllEmployees();
//
//            assertNull(actual);
//        } finally {
//            mockedStatic.close();
//        }
//    }
//    ///endregion
//
//    ///region SYMBOLIC EXECUTION: ERROR SUITE for method getAllEmployees()
//
//    /**
//     * @utbot.classUnderTest {@link SellerController}
//     * @utbot.methodUnderTest {@link SellerController#getAllEmployees()}
//     * @utbot.invokes {@link SellerService#getAllEmployees()}
//     * @utbot.throwsException {@link NullPointerException} in: List<EmployeesResponseDto> employeesList = sellerService.getAllEmployees();
//     */
//    @Test
//    @DisplayName("getAllEmployees: employeesList = sellerService.getAllEmployees() : True -> ThrowNullPointerException")
//    public void testGetAllEmployees_SellerServiceGetAllEmployees() {
//        SellerController sellerController = new SellerController(null, null, null);
//
//        /* This test fails because method [com.msmeli.controller.SellerController.getAllEmployees] produces [java.lang.NullPointerException]
//            com.msmeli.controller.SellerController.getAllEmployees(SellerController.java:121) */
//        sellerController.getAllEmployees();
//    }
//    ///endregion
//
//    ///region Errors report for getAllEmployees
//
//    public void testGetAllEmployees_errors() {
//        // Couldn't generate some tests. List of errors:
//        //
//        // 76 occurrences of:
//        // Default concrete execution failed
//
//    }
//    ///endregion
//
//    ///endregion
//
//    ///region Test suites for executable com.msmeli.controller.SellerController.getEmployeesBySellerId
//
//    ///region SYMBOLIC EXECUTION: SUCCESSFUL EXECUTIONS for method getEmployeesBySellerId()
//
//    /**
//     * @utbot.classUnderTest {@link SellerController}
//     * @utbot.methodUnderTest {@link SellerController#getEmployeesBySellerId()}
//     * @utbot.invokes {@link SellerService#getEmployeesBySellerId()}
//     * @utbot.invokes {@link ResponseEntity#ok(Object)}
//     * @utbot.returnsFrom {@code return ResponseEntity.ok(employeesList);}
//     */
//    @Test
//    @DisplayName("getEmployeesBySellerId: SellerServiceGetEmployeesBySellerId -> return ResponseEntity.ok(employeesList)")
//    public void testGetEmployeesBySellerId_ResponseEntityOk() throws ResourceNotFoundException {
//        org.mockito.MockedStatic mockedStatic = null;
//        try {
//            mockedStatic = mockStatic(ResponseEntity.class);
//            (mockedStatic.when(() -> ResponseEntity.ok(any()))).thenReturn(((ResponseEntity) null));
//            SellerService sellerServiceMock = mock(SellerService.class);
//            (when(sellerServiceMock.getEmployeesBySellerId())).thenReturn(((List) null));
//            SellerController sellerController = new SellerController(sellerServiceMock, null, null);
//
//            ResponseEntity actual = sellerController.getEmployeesBySellerId();
//
//            assertNull(actual);
//        } finally {
//            mockedStatic.close();
//        }
//    }
//    ///endregion
//
//    ///region SYMBOLIC EXECUTION: ERROR SUITE for method getEmployeesBySellerId()
//
//    /**
//     * @utbot.classUnderTest {@link SellerController}
//     * @utbot.methodUnderTest {@link SellerController#getEmployeesBySellerId()}
//     * @utbot.invokes {@link SellerService#getEmployeesBySellerId()}
//     * @utbot.throwsException {@link NullPointerException} in: List<EmployeesResponseDto> employeesList = sellerService.getEmployeesBySellerId();
//     */
//    @Test
//    @DisplayName("getEmployeesBySellerId: employeesList = sellerService.getEmployeesBySellerId() : True -> ThrowNullPointerException")
//    public void testGetEmployeesBySellerId_SellerServiceGetEmployeesBySellerId() throws ResourceNotFoundException {
//        SellerController sellerController = new SellerController(null, null, null);
//
//        /* This test fails because method [com.msmeli.controller.SellerController.getEmployeesBySellerId] produces [java.lang.NullPointerException]
//            com.msmeli.controller.SellerController.getEmployeesBySellerId(SellerController.java:115) */
//        sellerController.getEmployeesBySellerId();
//    }
//    ///endregion
//
//    ///region Errors report for getEmployeesBySellerId
//
//    public void testGetEmployeesBySellerId_errors() {
//        // Couldn't generate some tests. List of errors:
//        //
//        // 76 occurrences of:
//        // Default concrete execution failed
//
//    }
//    ///endregion
//
//    ///endregion
//
//    ///region Test suites for executable com.msmeli.controller.SellerController.prueba
//
//    ///region SYMBOLIC EXECUTION: SUCCESSFUL EXECUTIONS for method prueba()
//
//    /**
//     * @utbot.classUnderTest {@link SellerController}
//     * @utbot.methodUnderTest {@link SellerController#prueba()}
//     * @utbot.invokes {@link ItemService#saveAllItemForSeller()}
//     */
//    @Test
//    @DisplayName("prueba: -> ItemServiceSaveAllItemForSeller")
//    public void testPrueba_ItemServiceSaveAllItemForSeller() throws ResourceNotFoundException {
//        ItemService itemServiceMock = mock(ItemService.class);
//        SellerController sellerController = new SellerController(null, itemServiceMock, null);
//
//        sellerController.prueba();
//    }
//    ///endregion
//
//    ///region SYMBOLIC EXECUTION: ERROR SUITE for method prueba()
//
//    /**
//     * @utbot.classUnderTest {@link SellerController}
//     * @utbot.methodUnderTest {@link SellerController#prueba()}
//     * @utbot.invokes {@link ItemService#saveAllItemForSeller()}
//     * @utbot.throwsException {@link NullPointerException} in: itemService.saveAllItemForSeller();
//     */
//    @Test
//    @DisplayName("prueba: itemService.saveAllItemForSeller() : True -> ThrowNullPointerException")
//    public void testPrueba_ItemServiceSaveAllItemForSeller_1() throws ResourceNotFoundException {
//        SellerController sellerController = new SellerController(null, null, null);
//
//        /* This test fails because method [com.msmeli.controller.SellerController.prueba] produces [java.lang.NullPointerException]
//            com.msmeli.controller.SellerController.prueba(SellerController.java:53) */
//        sellerController.prueba();
//    }
//    ///endregion
//
//    ///region Errors report for prueba
//
//    public void testPrueba_errors() {
//        // Couldn't generate some tests. List of errors:
//        //
//        // 71 occurrences of:
//        // Default concrete execution failed
//
//    }
//    ///endregion
//
//    ///endregion
//
//    ///region Test suites for executable com.msmeli.controller.SellerController.refreshToken
//
//    ///region SYMBOLIC EXECUTION: ERROR SUITE for method refreshToken()
//
//    /**
//     * @utbot.classUnderTest {@link SellerController}
//     * @utbot.methodUnderTest {@link SellerController#refreshToken()}
//     * @utbot.invokes {@link SellerService#refreshToken()}
//     * @utbot.throwsException {@link NullPointerException} in: TokenResposeDTO refreshedToken = sellerService.refreshToken();
//     */
//    @Test
//    @DisplayName("refreshToken: refreshedToken = sellerService.refreshToken() : True -> ThrowNullPointerException")
//    public void testRefreshToken_SellerServiceRefreshToken() {
//        SellerController sellerController = new SellerController(null, null, null);
//
//        /* This test fails because method [com.msmeli.controller.SellerController.refreshToken] produces [java.lang.NullPointerException]
//            com.msmeli.controller.SellerController.refreshToken(SellerController.java:60) */
//        sellerController.refreshToken();
//    }
//    ///endregion
//
//    ///region Errors report for refreshToken
//
//    public void testRefreshToken_errors() {
//        // Couldn't generate some tests. List of errors:
//        //
//        // 72 occurrences of:
//        // Default concrete execution failed
//
//    }
//    ///endregion
//
//    ///endregion
//
//    ///region Test suites for executable com.msmeli.controller.SellerController.tokenForTg
//
//    ///region SYMBOLIC EXECUTION: SUCCESSFUL EXECUTIONS for method tokenForTg(java.lang.String)
//
//    /**
//     * @utbot.classUnderTest {@link SellerController}
//     * @utbot.methodUnderTest {@link SellerController#tokenForTg(String)}
//     * @utbot.invokes {@link SellerService#saveToken(String)}
//     */
//    @Test
//    @DisplayName("tokenForTg: -> SellerServiceSaveToken")
//    public void testTokenForTg_SellerServiceSaveToken() {
//        SellerService sellerServiceMock = mock(SellerService.class);
//        (when(sellerServiceMock.saveToken(any()))).thenReturn(((TokenResposeDTO) null));
//        SellerController sellerController = new SellerController(sellerServiceMock, null, null);
//
//        sellerController.tokenForTg(null);
//    }
//    ///endregion
//
//    ///region SYMBOLIC EXECUTION: ERROR SUITE for method tokenForTg(java.lang.String)
//
//    /**
//     * @utbot.classUnderTest {@link SellerController}
//     * @utbot.methodUnderTest {@link SellerController#tokenForTg(String)}
//     * @utbot.invokes {@link SellerService#saveToken(String)}
//     * @utbot.throwsException {@link NullPointerException} in: sellerService.saveToken(TG);
//     */
//    @Test
//    @DisplayName("tokenForTg: sellerService.saveToken(TG) : True -> ThrowNullPointerException")
//    public void testTokenForTg_SellerServiceSaveToken_1() {
//        SellerController sellerController = new SellerController(null, null, null);
//
//        /* This test fails because method [com.msmeli.controller.SellerController.tokenForTg] produces [java.lang.NullPointerException]
//            com.msmeli.controller.SellerController.tokenForTg(SellerController.java:48) */
//        sellerController.tokenForTg(null);
//    }
//    ///endregion
//
//    ///region Errors report for tokenForTg
//
//    public void testTokenForTg_errors() {
//        // Couldn't generate some tests. List of errors:
//        //
//        // 73 occurrences of:
//        // Default concrete execution failed
//
//    }
//    ///endregion
//
//    ///endregion
//
//    ///region Test suites for executable com.msmeli.controller.SellerController.updateAccessToken
//
//    ///region SYMBOLIC EXECUTION: ERROR SUITE for method updateAccessToken(java.lang.String)
//
//    /**
//     * @utbot.classUnderTest {@link SellerController}
//     * @utbot.methodUnderTest {@link SellerController#updateAccessToken(String)}
//     * @utbot.invokes {@link SellerService#updateAccessToken(String)}
//     * @utbot.throwsException {@link NullPointerException} in: sellerService.updateAccessToken(newAccessToken);
//     */
//    @Test
//    @DisplayName("updateAccessToken: sellerService.updateAccessToken(newAccessToken) : True -> ThrowNullPointerException")
//    public void testUpdateAccessToken_SellerServiceUpdateAccessToken() {
//        SellerController sellerController = new SellerController(null, null, null);
//
//        /* This test fails because method [com.msmeli.controller.SellerController.updateAccessToken] produces [java.lang.NullPointerException]
//            com.msmeli.controller.SellerController.updateAccessToken(SellerController.java:80) */
//        sellerController.updateAccessToken(null);
//    }
//    ///endregion
//
//    ///region Errors report for updateAccessToken
//
//    public void testUpdateAccessToken_errors() {
//        // Couldn't generate some tests. List of errors:
//        //
//        // 72 occurrences of:
//        // Default concrete execution failed
//
//    }
//    ///endregion
//
//    ///endregion
//
//    ///region Test suites for executable com.msmeli.controller.SellerController.updateEmployee
//
//    ///region SYMBOLIC EXECUTION: ERROR SUITE for method updateEmployee(java.lang.Long, com.msmeli.dto.request.EmployeeUpdateRequestDTO)
//
//    /**
//     * @utbot.classUnderTest {@link SellerController}
//     * @utbot.methodUnderTest {@link SellerController#updateEmployee(Long, EmployeeUpdateRequestDTO)}
//     * @utbot.invokes {@link com.msmeli.service.services.UserEntityService#updateEmployee(Long, EmployeeUpdateRequestDTO)}
//     * @utbot.throwsException {@link NullPointerException} in: UserResponseDTO updatedEmployee = userEntityService.updateEmployee(employeeId, employeeUpdateDTO);
//     */
//    @Test
//    @DisplayName("updateEmployee: updatedEmployee = userEntityService.updateEmployee(employeeId, employeeUpdateDTO) : True -> ThrowNullPointerException")
//    public void testUpdateEmployee_UserEntityServiceUpdateEmployee() {
//        SellerController sellerController = new SellerController(null, null, null);
//
//        /* This test fails because method [com.msmeli.controller.SellerController.updateEmployee] produces [java.lang.NullPointerException]
//            com.msmeli.controller.SellerController.updateEmployee(SellerController.java:102) */
//        sellerController.updateEmployee(null, null);
//    }
//    ///endregion
//
//    ///region FUZZER: TIMEOUTS for method updateEmployee(java.lang.Long, com.msmeli.dto.request.EmployeeUpdateRequestDTO)
//
//    /**
//     * @utbot.classUnderTest {@link SellerController}
//     * @utbot.methodUnderTest {@link SellerController#updateEmployee(Long, EmployeeUpdateRequestDTO)}
//     */
//    @Test
//    @DisplayName("updateEmployee: employeeId = zero, employeeUpdateDTO = EmployeeUpdateRequestDTO()")
//    @org.junit.jupiter.api.Timeout(value = 1000L, unit = TimeUnit.MILLISECONDS)
//    public void testUpdateEmployeeWithCornerCase() throws Throwable {
//        EmployeeUpdateRequestDTO employeeUpdateDTO = new EmployeeUpdateRequestDTO();
//        employeeUpdateDTO.setRol("");
//        employeeUpdateDTO.setEmail("");
//        employeeUpdateDTO.setPassword("");
//        employeeUpdateDTO.setNombre("\n\t\r");
//        employeeUpdateDTO.setRePassword("\n\t\r");
//        employeeUpdateDTO.setUsername("abc");
//
//        /* This execution may take longer than the 1000 ms timeout
//         and therefore fail due to exceeding the timeout. */
//        assertTimeoutPreemptively(Duration.ofMillis(1000L), () -> {
//            Class sellerControllerClazz = Class.forName("com.msmeli.controller.SellerController");
//            Class longType = Class.forName("java.lang.Long");
//            Class employeeUpdateDTOType = Class.forName("com.msmeli.dto.request.EmployeeUpdateRequestDTO");
//            Method updateEmployeeMethod = sellerControllerClazz.getDeclaredMethod("updateEmployee", longType, employeeUpdateDTOType);
//            updateEmployeeMethod.setAccessible(true);
//            Object[] updateEmployeeMethodArguments = new Object[2];
//            updateEmployeeMethodArguments[0] = 0L;
//            updateEmployeeMethodArguments[1] = employeeUpdateDTO;
//            try {
//                updateEmployeeMethod.invoke(null, updateEmployeeMethodArguments);
//            } catch (InvocationTargetException invocationTargetException) {
//                throw invocationTargetException.getTargetException();
//            }
//        });
//    }
//    ///endregion
//
//    ///region Errors report for updateEmployee
//
//    public void testUpdateEmployee_errors() {
//        // Couldn't generate some tests. List of errors:
//        //
//        // 74 occurrences of:
//        // Default concrete execution failed
//
//    }
//    ///endregion
//
//    ///endregion
//
//    ///region Test suites for executable com.msmeli.controller.SellerController.updateToken
//
//    ///region SYMBOLIC EXECUTION: ERROR SUITE for method updateToken(java.lang.String)
//
//    /**
//     * @utbot.classUnderTest {@link SellerController}
//     * @utbot.methodUnderTest {@link SellerController#updateToken(String)}
//     * @utbot.invokes {@link SellerService#updateToken(String)}
//     * @utbot.throwsException {@link NullPointerException} in: TokenResposeDTO updatedToken = sellerService.updateToken(TG);
//     */
//    @Test
//    @DisplayName("updateToken: updatedToken = sellerService.updateToken(TG) : True -> ThrowNullPointerException")
//    public void testUpdateToken_SellerServiceUpdateToken() {
//        SellerController sellerController = new SellerController(null, null, null);
//
//        /* This test fails because method [com.msmeli.controller.SellerController.updateToken] produces [java.lang.NullPointerException]
//            com.msmeli.controller.SellerController.updateToken(SellerController.java:70) */
//        sellerController.updateToken(null);
//    }
//    ///endregion
//    ///endregion
//
//    ///region Errors report for updateToken
//
//    public void testUpdateToken_errors() {
//        // Couldn't generate some tests. List of errors:
//        //
//        // 71 occurrences of:
//        // Default concrete execution failed
//
//    }
//    ///endregion
//
//}
