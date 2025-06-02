package com.grupo6.stockline.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor extends Base{

    private LocalDate fechaAlta;
    private LocalDate fechaBaja;
    private String mailProveedor;
    private String nombreProveedor;


    @OneToMany(mappedBy = "proveedor")
    private List<ArticuloProveedor> articuloProveedor;

    @OneToMany(mappedBy = "proveedorPredeterminado")
    private List<Articulo> articuloPredeterminado;

    @OneToMany(mappedBy = "proveedor")
    private List<OrdenCompra> ordenCompra;
}
