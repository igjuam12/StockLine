package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.Articulo;
import com.grupo6.stockline.Entities.DatosModeloInventario;
import com.grupo6.stockline.Entities.DetalleVenta;
import com.grupo6.stockline.Entities.Venta;
import com.grupo6.stockline.Enum.EstadoOrdenCompra;
import com.grupo6.stockline.Enum.ModeloInventario;
import com.grupo6.stockline.Repositories.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaServiceImpl extends BaseServiceImpl<Venta, Long> implements VentaService {
    @Autowired
    VentaRepository ventaRepository;
    @Autowired
    ArticuloRepository articuloRepository;
    @Autowired
    DetalleVentaRepository detalleVentaRepository;
    @Autowired
    DatosModeloInventarioRepository datosModeloInventarioRepository;
    @Autowired
    OrdenCompraRepository ordenCompraRepository;
    @Autowired
    OrdenCompraService ordenCompraService;
    @PersistenceContext
    private EntityManager entityManager;

    public VentaServiceImpl(BaseRepository<Venta, Long> baseRepository,
                            VentaRepository ventaRepository,
                            ArticuloRepository articuloRepository,
                            DetalleVentaRepository detalleVentaRepository,
                            DatosModeloInventarioRepository datosModeloInventarioRepository,
                            OrdenCompraRepository ordenCompraRepository,
                            OrdenCompraService ordenCompraService) {
        super(baseRepository);
    }

    @Override
    @Transactional
    public Venta crearVenta(Venta venta) throws Exception {
        double totalVenta = 0;

        for (DetalleVenta detalle : venta.getDetalleVenta()) {
            Articulo articulo = articuloRepository.findById(detalle.getArticulo().getId())
                    .orElseThrow(() -> new Exception("El artículo no existe."));

            if (detalle.getCantidad() > articulo.getStockActual()) {
                throw new Exception("Stock insuficiente para el artículo: " + articulo.getNombreArticulo() +
                        ". Stock disponible: " + articulo.getStockActual() + ", se solicitan: " + detalle.getCantidad());
            }
        }

        Venta ventaGuardada = ventaRepository.save(venta);

        for (DetalleVenta detalle : venta.getDetalleVenta()) {
            Articulo articulo = articuloRepository.findById(detalle.getArticulo().getId()).get();

            int nuevoStock = articulo.getStockActual() - detalle.getCantidad();
            articulo.setStockActual(nuevoStock);
            articuloRepository.save(articulo);

            detalle.setSubTotal(articulo.getPrecioVenta() * detalle.getCantidad());
            totalVenta += detalle.getSubTotal();

            detalle.setVenta(ventaGuardada);
            detalleVentaRepository.save(detalle);

            verificarYGenerarOrdenDeCompra(articulo);
        }

        ventaGuardada.setTotalVenta(totalVenta);
        return ventaRepository.save(ventaGuardada);
    }

    private void verificarYGenerarOrdenDeCompra(Articulo articulo) throws Exception {

        if (articulo.getModeloInventario() != ModeloInventario.LoteFijo) {
            return;
        }

        DatosModeloInventario datosModelo = datosModeloInventarioRepository.findByArticulo(articulo)
                .orElseThrow(() -> new Exception("No se encontraron datos de modelo de inventario para el artículo: " + articulo.getNombreArticulo()));

        boolean necesitaReposicion = articulo.getStockActual() <= datosModelo.getPuntoPedido();

        if (necesitaReposicion) {
            List<EstadoOrdenCompra> estadosActivos = List.of(EstadoOrdenCompra.PENDIENTE, EstadoOrdenCompra.ENVIADA);
            boolean yaTieneOrdenActiva = ordenCompraRepository.existsByDetalleOrdenCompra_ArticuloAndEstadoOrdenCompraIn(articulo, estadosActivos);

            if (!yaTieneOrdenActiva) {
                ordenCompraService.crearOrdenAutomaticaParaLoteFijo(articulo, datosModelo);
            }
        }
    }

}
