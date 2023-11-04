package com.msmeli.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockBySupplierRequestDTO {
    @NotNull(message = "Ingrese un supplier id")
    @Positive(message = "Ingrese un supplier id valido")
    private Long supplierId;
    @NotEmpty(message = "Ingrese al menos un item en el stock de supplier")
    @Valid
    private List<SupplierStockRequestDTO> supplierStock;
}
