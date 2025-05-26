package com.grupo6.stockline.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;           // ≈ codProveedor

    private LocalDate fechaAltaProveedor;
    private LocalDate fechaBajaProveedor;
    private String    mailProveedor;
    private String    nombreProveedor;

    /* Relaciones */
    @OneToMany(mappedBy = "proveedor")
    private List<ArticuloProveedor> articulosProveedores;

    @OneToMany(mappedBy = "proveedorPredeterminado")
    private List<Articulo> articulosPredeterminados;
}
