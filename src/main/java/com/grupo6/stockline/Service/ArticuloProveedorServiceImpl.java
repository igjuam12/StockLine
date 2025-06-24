package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.Articulo;
import com.grupo6.stockline.Entities.ArticuloProveedor;
import com.grupo6.stockline.Entities.Proveedor;
import com.grupo6.stockline.Enum.EstadoOrdenCompra;
import com.grupo6.stockline.Repositories.ArticuloProveedorRepository;
import com.grupo6.stockline.Repositories.BaseRepository;
import com.grupo6.stockline.Repositories.DetalleOrdenCompraRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticuloProveedorServiceImpl extends BaseServiceImpl<ArticuloProveedor, Long> implements ArticuloProveedorService{
    @Autowired
    ArticuloProveedorRepository articuloProveedorRepository;
    @Autowired
    DetalleOrdenCompraRepository detalleOrdenCompraRepository;
    @Autowired
    ArticuloService articuloService;
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
    public List<ArticuloProveedor> obtenerProveedoresPorArticulo(Long articuloId) {
        return articuloProveedorRepository.findByArticuloId(articuloId);
    }

    @Override
    @Transactional
    public void save(ArticuloProveedor entity) throws Exception {
        Proveedor proveedor = entity.getProveedor();
        Articulo articulo = entity.getArticulo();

        if (proveedor == null || articulo == null) {
            throw new Exception("La asociación debe tener un Proveedor y un Artículo asignados.");
        }

        if (articuloProveedorRepository.existsByProveedorAndArticuloAndFechaBajaIsNull(proveedor, articulo)) {
            throw new Exception("El proveedor '" + proveedor.getNombreProveedor() +
                    "' ya está asociado  al artículo '" + articulo.getNombreArticulo() + "'.");
        }

        super.save(entity);
    }

    @Override
    @Transactional
    public void update(Long id, ArticuloProveedor articuloProveedor) throws Exception {
        ArticuloProveedor articuloProveedorExistente = articuloProveedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Asociacion no encontrada"));
        articuloProveedorExistente.setCostoPedido(articuloProveedor.getCostoPedido());
        articuloProveedorExistente.setDemoraEntrega(articuloProveedor.getDemoraEntrega());
        articuloProveedorExistente.setCostoCompra(articuloProveedor.getCostoCompra());

        articuloProveedorRepository.save(articuloProveedorExistente);
        articuloService.calcularModeloInventario(articuloProveedor.getArticulo().getId());
    }

    @Override
    @Transactional
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
        List<EstadoOrdenCompra> estadosActivos = List.of(EstadoOrdenCompra.PENDIENTE, EstadoOrdenCompra.ENVIADA);
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
        asociacion.setFechaBaja(LocalDateTime.now());
        articuloProveedorRepository.save(asociacion);
    }

}