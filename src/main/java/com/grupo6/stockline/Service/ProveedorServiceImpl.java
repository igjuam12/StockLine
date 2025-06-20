package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.ArticuloProveedor;
import com.grupo6.stockline.Entities.Proveedor;
import com.grupo6.stockline.Enum.EstadoOrdenCompra;
import com.grupo6.stockline.Repositories.ArticuloRepository;
import com.grupo6.stockline.Repositories.BaseRepository;
import com.grupo6.stockline.Repositories.OrdenCompraRepository;
import com.grupo6.stockline.Repositories.ProveedorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProveedorServiceImpl extends BaseServiceImpl<Proveedor, Long> implements ProveedorService{
    @Autowired
    ProveedorRepository proveedorRepository;
    @Autowired
    ArticuloRepository articuloRepository;
    @Autowired
    OrdenCompraRepository ordenCompraRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public ProveedorServiceImpl(BaseRepository<Proveedor, Long> baseRepository, ProveedorRepository proveedorRepository,
                                ArticuloRepository articuloRepository, OrdenCompraRepository ordenCompraRepository) {
        super(baseRepository);
    }

    @Override
    public void update(Long id, Proveedor proveedor){
        Proveedor proveedorExistente = proveedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado"));
        proveedorExistente.setNombreProveedor(proveedor.getNombreProveedor());
        proveedorExistente.setMailProveedor(proveedor.getMailProveedor());
        List<ArticuloProveedor> articuloProveedors = proveedorExistente.getArticuloProveedor();
        proveedorRepository.save(proveedorExistente);
    }

    @Override
    public void delete(Long id) throws Exception {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado"));

        if (proveedor.getFechaBaja() != null) {
            throw new IllegalStateException("Este proveedor ya fue dado de baja anteriormente.");
        }

        boolean esPredeterminado = articuloRepository.existsByProveedorPredeterminadoId(id);
        if (esPredeterminado) {
            throw new IllegalStateException("No se puede dar de baja: proveedor predeterminado en al menos un artículo.");
        }

        boolean tieneOCs = ordenCompraRepository.existsByProveedorIdAndEstadoOrdenCompraIn(id, List.of(EstadoOrdenCompra.PENDIENTE, EstadoOrdenCompra.ENVIADA));
        if (tieneOCs) {
            throw new IllegalStateException("No se puede dar de baja: proveedor con órdenes de compra pendientes o enviadas.");
        }

        proveedor.setFechaBaja(LocalDateTime.now());
        proveedorRepository.save(proveedor);
    }

}
