package com.grupo6.stockline.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatosModeloInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer inventarioMaximo;
    private Integer loteOptimo;
    private Integer puntoPedido;
    private Integer stockSeguridad;

    @ManyToOne
    @JoinColumn(name = "articulo_id")
    private Articulo articulo;

    @ManyToOne @JoinColumn(name = "modelo_inventario_id")
    private ModeloInventario modeloInventario;
}
