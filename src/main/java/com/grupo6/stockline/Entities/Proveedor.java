package com.grupo6.stockline.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor extends Base{

    private String mailProveedor;
    private String nombreProveedor;

    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL)
    private List<ArticuloProveedor> articuloProveedor;

    @OneToMany(mappedBy = "proveedorPredeterminado")
    private List<Articulo> articuloPredeterminado;

    @OneToMany(mappedBy = "proveedor")
    private List<OrdenCompra> ordenCompra;

    public void asociarArticuloProveedor() {
        if (articuloProveedor != null) {
            for (ArticuloProveedor ap : articuloProveedor) {
                ap.setProveedor(this);
            }
        }
    }

}
