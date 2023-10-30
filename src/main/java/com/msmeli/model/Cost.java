package com.msmeli.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cost {

    @Id
    private String id;

    //Los obtenemos del item
    private String sku;
    private String title;
    private String status;
    private int available_quantity;
    private Double price;
    //los obtenemos de feign
    private double comision_fee;
    private double comision_discount;
    private double shipping;
    //hardcodeadas
    private double replacement_cost;
    private double profit;
    private boolean published;
    private double total_cost;
    private double margin;
    /*Crear servicio Costo: (sku, item_name, status, stock_ml, comision_fee,costo_compra, ganancia, envio, precio_final, publicado booleano) itemService, stockService, feign shipping y comision.*/

}
