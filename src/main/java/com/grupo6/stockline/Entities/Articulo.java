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
public class Articulo extends Base{

    private Integer costoAlmacenamiento;
    private Integer costoCompra;
    private Integer costoPedido;
    private Integer demandaArticulo;
    private String  descripcionArticulo;
    private LocalDate fechaAlta;
    private LocalDate fechaBaja;
    private LocalDate fechaModificacionArticulo;
    private String  nombreArticulo;
    private Integer stockActual;

    @ManyToOne
    @JoinColumn(name = "proveedor_predeterminado_id")
    private Proveedor proveedorPredeterminado;

    @OneToMany(mappedBy = "articulo")
    private List<DetalleOrdenCompra> detalleOrdenCompra;

    @OneToMany(mappedBy = "articulo")
    private List<DetalleVenta> detalleVenta;

    @OneToMany(mappedBy = "articulo")
    private List<ArticuloProveedor> articuloProveedor;

    @OneToMany(mappedBy = "articulo")
    private List<DatosModeloInventario> datosModeloInventario;

    @Enumerated(EnumType.STRING)
    private ModeloInventario modeloInventario;
}
