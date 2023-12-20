package com.msmeli.controller;


<<<<<<< HEAD
import com.msmeli.dto.request.EmployeeUpdateRequestDTO;
=======
import com.msmeli.dto.response.EmployeesResponseDto;
>>>>>>> bba0f1eea6de5ed291955afb0477259f221ca04e
import com.msmeli.dto.response.TokenResposeDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Employee;
import com.msmeli.service.services.ItemService;
import com.msmeli.service.services.SellerService;
<<<<<<< HEAD
import com.msmeli.service.services.UserEntityService;
=======
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
>>>>>>> bba0f1eea6de5ed291955afb0477259f221ca04e
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/seller")
public class SellerController {
    private final SellerService sellerService;
    private final ItemService itemService;


    private final UserEntityService userEntityService;

    public SellerController(SellerService sellerService, ItemService itemService, UserEntityService userEntityService) {
        this.sellerService = sellerService;
        this.itemService = itemService;
        this.userEntityService = userEntityService;
    }

    @PostMapping("/tokenForTG")
    public void tokenForTg(@RequestParam String TG){
        sellerService.saveToken(TG);
    }

    @PostMapping("/saveAllItemForSeller")
    public void prueba() throws ResourceNotFoundException {
        itemService.saveAllItemForSeller();

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResposeDTO> refreshToken() {
        try {
            TokenResposeDTO refreshedToken = sellerService.refreshToken();
            return new ResponseEntity<>(refreshedToken, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/update-token")
    public ResponseEntity<TokenResposeDTO> updateToken(@RequestParam String TG) {
        try {
            TokenResposeDTO updatedToken = sellerService.updateToken(TG);
            return new ResponseEntity<>(updatedToken, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/update-access-token")
    public ResponseEntity<String> updateAccessToken(@RequestParam String newAccessToken) {
        try {
            sellerService.updateAccessToken(newAccessToken);
            return new ResponseEntity<>("Access Token actualizado exitosamente.", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("No se encontró al vendedor en la base de datos.", HttpStatus.NOT_FOUND);
        }
    }

<<<<<<< HEAD
    @DeleteMapping("/eliminar/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long employeeId) {
        try {
            userEntityService.deleteEmployee(employeeId);
            return new ResponseEntity<>("Empleado eliminado con éxito", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("Empleado no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/editar/{employeeId}")
    public ResponseEntity<UserResponseDTO> updateEmployee(@PathVariable Long employeeId,
                                                          @RequestBody EmployeeUpdateRequestDTO employeeUpdateDTO) {
        try {
            UserResponseDTO updatedEmployee = userEntityService.updateEmployee(employeeId, employeeUpdateDTO);
            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }


=======
        @GetMapping("/getEmployeesBySellerId")
    public ResponseEntity<List<EmployeesResponseDto>> getEmployeesBySellerId() throws ResourceNotFoundException {
        List<EmployeesResponseDto> employeesList = sellerService.getEmployeesBySellerId();
        return ResponseEntity.ok(employeesList);
    }

    @GetMapping("/getAllEmployees")
    public ResponseEntity<List<EmployeesResponseDto>> getAllEmployees() {
        List<EmployeesResponseDto> employeesList = sellerService.getAllEmployees();
        return ResponseEntity.ok(employeesList);
    }

>>>>>>> bba0f1eea6de5ed291955afb0477259f221ca04e
}
