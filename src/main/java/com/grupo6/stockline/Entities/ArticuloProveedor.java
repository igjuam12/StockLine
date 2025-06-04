package com.grupo6.stockline.Entities;

import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloProveedor extends Base{

    private BigDecimal cargoPedido;
    private Integer demoraEntrega;
    private BigDecimal precioArticulo;

    @ManyToOne
    @JoinColumn(name = "articulo_id")
    private Articulo articulo;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;
}
