package com.grupo6.stockline.Entities;

import com.grupo6.stockline.Enum.EstadoOrdenCompra;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // codOrdenCompra

    private LocalDate fechaModificacionOrdenCompra;
    private LocalDate fechaOrdenCompra;

    @Enumerated(EnumType.STRING)
    private EstadoOrdenCompra estado;

    @OneToMany(mappedBy = "ordenCompra", cascade = CascadeType.ALL)
    private List<DetalleOrdenCompra> detalles;
}