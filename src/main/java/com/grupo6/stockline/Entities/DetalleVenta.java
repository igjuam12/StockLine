package com.grupo6.stockline.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVenta extends Base{

    private Integer cantidad;
    private Integer subTotal;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "articulo_id")
    private Articulo articulo;
}