package com.msmeli.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierStockRequestDTO {
    @NotBlank(message = "Ingrese un sku valido")
    private String sku;
    @NotNull(message = "Ingrese un precio")
    private Double price;
    @NotNull(message = "ingrese una cantidad")
    private Integer availableQuantity;

}
