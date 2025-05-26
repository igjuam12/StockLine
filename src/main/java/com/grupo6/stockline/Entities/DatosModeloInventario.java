package com.grupo6.stockline.Entities;

import com.grupo6.stockline.Enum.ModeloInventario;
import jakarta.persistence.*;
import lombok.*;

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

    @Enumerated(EnumType.STRING)
    private ModeloInventario modeloInventario;
}
