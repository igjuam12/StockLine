package com.grupo6.stockline.Entities;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloProveedor extends Base{

    private Integer cargoPedido;
    private Integer demoraEntrega;
    private LocalDate fechaAltaArticuloProveedor;
    private Integer precioArticulo;

    @ManyToOne
    @JoinColumn(name = "articulo_id")
    private Articulo articulo;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;
}
