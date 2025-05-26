package com.grupo6.stockline.Entities;

import com.grupo6.stockline.Enum.ModeloInventario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Articulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer costoAlmacenamiento;
    private Integer costoCompra;
    private Integer costoPedido;
    private Integer demandaArticulo;
    private String  descripcionArticulo;
    private LocalDate fechaAltaArticulo;
    private LocalDate fechaBajaArticulo;
    private LocalDate fechaModificacionArticulo;
    private String  nombreArticulo;
    private Integer stockActual;

    /* Relaciones ---------------------------------------------------------- */

    // Default supplier (“PREDETERMINADO”)
    @ManyToOne
    @JoinColumn(name = "proveedor_predeterminado_id")
    private Proveedor proveedorPredeterminado;

    @OneToMany(mappedBy = "articulo")
    private List<DetalleOrdenCompra> detallesOrdenCompra;

    @OneToMany(mappedBy = "articulo")
    private List<DetalleVenta> detallesVenta;

    @OneToMany(mappedBy = "articulo")
    private List<ArticuloProveedor> articulosProveedores;

    @OneToMany(mappedBy = "articulo")
    private List<DatosModeloInventario> datosModelosInventario;

    @Enumerated(EnumType.STRING)
    private ModeloInventario modeloInventario;
}
