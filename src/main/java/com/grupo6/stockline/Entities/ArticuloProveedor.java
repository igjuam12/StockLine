package com.grupo6.stockline.Entities;

import jakarta.persistence.*;
import lombok.*;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloProveedor extends Base{

    private Integer cargoPedido;
    private Integer demoraEntrega;
    private Integer precioArticulo;

    @ManyToOne
    @JoinColumn(name = "articulo_id")
    private Articulo articulo;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;
}
