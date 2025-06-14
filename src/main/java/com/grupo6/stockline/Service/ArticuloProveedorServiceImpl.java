package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.Articulo;
import com.grupo6.stockline.Entities.ArticuloProveedor;
import com.grupo6.stockline.Enum.EstadoOrdenCompra;
import com.grupo6.stockline.Repositories.ArticuloProveedorRepository;
import com.grupo6.stockline.Repositories.BaseRepository;
import com.grupo6.stockline.Repositories.DetalleOrdenCompraRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ArticuloProveedorServiceImpl extends BaseServiceImpl<ArticuloProveedor, Long> implements ArticuloProveedorService{
    @Autowired
    ArticuloProveedorRepository articuloProveedorRepository;
    @Autowired
    DetalleOrdenCompraRepository detalleOrdenCompraRepository;
    @PersistenceContext
    private EntityManager entityManager;
    public ArticuloProveedorServiceImpl(BaseRepository<ArticuloProveedor, Long> baseRepository,
                                        ArticuloProveedorRepository articuloProveedorRepository, DetalleOrdenCompraRepository detalleOrdenCompraRepository) {
        super(baseRepository);
    }

    @Override
    public List<ArticuloProveedor> obtenerArticulosPorProveedor(Long proveedorId){
        return articuloProveedorRepository.findByProveedorId(proveedorId);
    }

    @Override
    public void update(Long id, ArticuloProveedor articuloProveedor){
        ArticuloProveedor articuloProveedorExistente = articuloProveedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Asociacion no encontrada"));
        articuloProveedorExistente.setCargoPedido(articuloProveedor.getCargoPedido());
        articuloProveedorExistente.setDemoraEntrega(articuloProveedor.getDemoraEntrega());
        articuloProveedorExistente.setPrecioArticulo(articuloProveedor.getPrecioArticulo());
        articuloProveedorRepository.save(articuloProveedorExistente);
    }

    @Override
    public void delete(Long id) throws Exception {
        ArticuloProveedor asociacion = articuloProveedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Asociacion no encontrada"));
        if (asociacion.getFechaBaja() != null) {
            throw new IllegalStateException("Esta asociación ya fue dada de baja anteriormente.");
        }
        Articulo articuloAsociado = asociacion.getArticulo();
        if (articuloAsociado.getProveedorPredeterminado() != null &&
                articuloAsociado.getProveedorPredeterminado().getId().equals(asociacion.getProveedor().getId())) {
            throw new IllegalStateException("No se puede dar de baja: el proveedor es el predeterminado para este artículo. Por favor, asigne otro proveedor predeterminado primero.");
        }
        List<EstadoOrdenCompra> estadosActivos = List.of(EstadoOrdenCompra.Pendiente, EstadoOrdenCompra.Enviada);
        boolean tieneOCsActivas = detalleOrdenCompraRepository.existsByArticuloIdAndOrdenCompraProveedorIdAndOrdenCompraEstadoOrdenCompraIn(
                asociacion.getArticulo().getId(),
                asociacion.getProveedor().getId(),
                estadosActivos
        );
        if (tieneOCsActivas) {
            throw new IllegalStateException("No se puede dar de baja: existen órdenes de compra pendientes o enviadas para esta asociación.");
        }
        long countAsociacionesActivas = articuloProveedorRepository.countByArticuloIdAndFechaBajaIsNull(articuloAsociado.getId());
        if (countAsociacionesActivas <= 1) {
            throw new IllegalStateException("No se puede dar de baja: el artículo no puede quedar sin proveedores.");
        }
        asociacion.setFechaBaja(LocalDate.now());
        articuloProveedorRepository.save(asociacion);
    }

}