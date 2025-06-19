package com.grupo6.stockline.Entities;

import com.grupo6.stockline.Enum.EstadoOrdenCompra;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenCompra extends Base{

    private LocalDateTime fechaModificacionOrdenCompra;


    @Enumerated(EnumType.STRING)
    private EstadoOrdenCompra estadoOrdenCompra;

    @OneToMany(mappedBy = "ordenCompra", cascade = CascadeType.ALL)
    private List<DetalleOrdenCompra> detalleOrdenCompra;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;
}