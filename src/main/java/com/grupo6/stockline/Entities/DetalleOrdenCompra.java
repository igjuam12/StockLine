package com.grupo6.stockline.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleOrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "orden_compra_id")
    private OrdenCompra ordenCompra;

    @ManyToOne @JoinColumn(name = "articulo_id")
    private Articulo articulo;
}