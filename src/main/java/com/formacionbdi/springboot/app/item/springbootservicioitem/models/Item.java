package com.formacionbdi.springboot.app.item.springbootservicioitem.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Producto producto;
    private Integer cantidad;

    public Double getTotal(){
        return producto.getPrecio() * cantidad.doubleValue();
    }
}
