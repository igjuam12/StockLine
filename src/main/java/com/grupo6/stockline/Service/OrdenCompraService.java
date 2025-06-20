package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.Articulo;
import com.grupo6.stockline.Entities.DatosModeloInventario;
import com.grupo6.stockline.Entities.OrdenCompra;
import com.grupo6.stockline.Enum.EstadoOrdenCompra;

import java.util.List;

public interface OrdenCompraService extends BaseService<OrdenCompra, Long> {

    OrdenCompra crearOrdenCompra(OrdenCompra ordenCompra, Long articuloId) throws Exception;

    OrdenCompra modificarOrdenCompra(Long idOrdenCompra, Long idProveedorNuevo, Integer nuevaCantidad) throws Exception;

    OrdenCompra cancelarOrdenCompra(Long ordenCompraId) throws Exception;

    OrdenCompra enviarOrdenCompra(Long ordenCompraId) throws Exception;

    OrdenCompra finalizarOrdenCompra(Long ordenCompraId) throws Exception;

    boolean existeOrdenCompraActivaParaArticulo(Long articuloId) throws Exception;

    List<OrdenCompra> findOrdenCompraByEstadoAndArticulo(EstadoOrdenCompra estado, Long articuloId) throws Exception;

    void crearOrdenCompraAutomatica() throws Exception;

    OrdenCompra crearOrdenAutomaticaParaLoteFijo(Articulo articulo, DatosModeloInventario datosModelo) throws Exception;
}
