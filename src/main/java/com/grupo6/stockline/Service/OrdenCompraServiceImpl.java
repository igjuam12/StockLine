package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.Articulo;
import com.grupo6.stockline.Entities.DetalleOrdenCompra;
import com.grupo6.stockline.Entities.DatosModeloInventario;
import com.grupo6.stockline.Entities.OrdenCompra;
import com.grupo6.stockline.Entities.Proveedor;
import com.grupo6.stockline.Enum.EstadoOrdenCompra;
import com.grupo6.stockline.Enum.ModeloInventario;
import com.grupo6.stockline.Repositories.ArticuloRepository;
import com.grupo6.stockline.Repositories.OrdenCompraRepository;
import com.grupo6.stockline.Repositories.ProveedorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdenCompraServiceImpl extends BaseServiceImpl<OrdenCompra, Long> implements OrdenCompraService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final ArticuloRepository articuloRepository; // Necesario para buscar artículos
    private final ProveedorRepository proveedorRepository; // Necesario para buscar proveedores

    public OrdenCompraServiceImpl(OrdenCompraRepository ordenCompraRepository,
                                  ArticuloRepository articuloRepository,
                                  ProveedorRepository proveedorRepository) {
        super(ordenCompraRepository);
        this.ordenCompraRepository = ordenCompraRepository;
        this.articuloRepository = articuloRepository;
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    @Transactional
    public OrdenCompra crearOrdenCompra(OrdenCompra ordenCompra, Long articuloId) throws Exception {
        try {
            if (existeOrdenCompraActivaParaArticulo(articuloId)) {
                throw new Exception("Ya existe una orden de compra activa (Pendiente o Enviada) para el artículo con ID: " + articuloId);
            }

            Articulo articulo = articuloRepository.findById(articuloId)
                    .orElseThrow(() -> new EntityNotFoundException("Artículo no encontrado con ID: " + articuloId));

            if (ordenCompra.getProveedor() == null && articulo.getProveedorPredeterminado() != null) {
                ordenCompra.setProveedor(articulo.getProveedorPredeterminado());
            }

            if (ordenCompra.getProveedor() == null) {
                throw new Exception("La orden de compra debe tener un proveedor asignado");
            }

            if (ordenCompra.getDetalleOrdenCompra() == null || ordenCompra.getDetalleOrdenCompra().isEmpty()) {
                throw new Exception("La orden de compra debe contener al menos un detalle");
            } else {
                for (DetalleOrdenCompra detalle : ordenCompra.getDetalleOrdenCompra()) {
                    if (detalle.getArticulo() == null) {
                        detalle.setArticulo(articulo);
                    }
                    detalle.setOrdenCompra(ordenCompra);
                }
            }

            if (ordenCompra.getDetalleOrdenCompra() != null && !ordenCompra.getDetalleOrdenCompra().isEmpty()) {
                DetalleOrdenCompra detalle = ordenCompra.getDetalleOrdenCompra().get(0);
                Integer cantidadSolicitada = detalle.getCantidad();

                if (cantidadSolicitada == null || cantidadSolicitada <= 0) {
                    throw new Exception("La cantidad solicitada debe ser mayor a cero");
                }

                if (articulo.getDatosModeloInventario() != null
                        && !articulo.getDatosModeloInventario().isEmpty()) {
                    Integer inventarioMaximo = articulo.getDatosModeloInventario().get(0).getInventarioMaximo();
                    Integer stockActual = articulo.getStockActual();
                    if (inventarioMaximo != null && stockActual != null
                            && stockActual + cantidadSolicitada > inventarioMaximo) {
                        int exceso = stockActual + cantidadSolicitada - inventarioMaximo;
                        throw new Exception("La cantidad solicitada supera el inventario máximo por " + exceso + " unidades");
                    }

                    if (articulo.getModeloInventario() == ModeloInventario.LoteFijo) {
                        Integer puntoPedido = articulo.getDatosModeloInventario().get(0).getPuntoPedido();
                        if (puntoPedido != null && stockActual != null
                                && stockActual + cantidadSolicitada <= puntoPedido) {
                            throw new Exception("La cantidad solicitada no supera el Punto de Pedido de "
                                    + puntoPedido + " unidades para el artículo con ID: " + articuloId);
                        }
                    }
                }
            }


            ordenCompra.setEstadoOrdenCompra(EstadoOrdenCompra.PENDIENTE);
            ordenCompra.setFechaModificacionOrdenCompra(LocalDateTime.now());
            ordenCompra.setAutomatica(false);

            return ordenCompraRepository.save(ordenCompra);
        } catch (Exception e) {
            throw new Exception("Error al crear la orden de compra: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public OrdenCompra modificarOrdenCompra(Long idOrdenCompra, Long idProveedorNuevo, Integer nuevaCantidad) throws Exception {
        try {
            OrdenCompra ordenExistente = ordenCompraRepository.findById(idOrdenCompra)
                    .orElseThrow(() -> new EntityNotFoundException("Orden de Compra no encontrada con ID: " + idOrdenCompra));

            if (ordenExistente.getEstadoOrdenCompra() != EstadoOrdenCompra.PENDIENTE) {
                throw new Exception("Solo se pueden modificar órdenes de compra en estado PENDIENTE.");
            }

            if (idProveedorNuevo != null) {
                Proveedor nuevoProveedor = proveedorRepository.findById(idProveedorNuevo)
                        .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con ID: " + idProveedorNuevo));
                ordenExistente.setProveedor(nuevoProveedor);
            }

            if (ordenExistente.getProveedor() == null) {
                throw new Exception("La orden de compra debe tener un proveedor asignado");
            }

            if (nuevaCantidad != null && ordenExistente.getDetalleOrdenCompra() != null && !ordenExistente.getDetalleOrdenCompra().isEmpty()) {
                DetalleOrdenCompra primerDetalle = ordenExistente.getDetalleOrdenCompra().get(0);
                Articulo articulo = primerDetalle.getArticulo();
                if (articulo != null && articulo.getDatosModeloInventario() != null && !articulo.getDatosModeloInventario().isEmpty()) {
                    Integer inventarioMaximo = articulo.getDatosModeloInventario().get(0).getInventarioMaximo();
                    Integer stockActual = articulo.getStockActual();
                    if (nuevaCantidad <= 0) {
                        throw new Exception("La cantidad solicitada debe ser mayor a cero");
                    }
                    if (inventarioMaximo != null && stockActual != null && stockActual + nuevaCantidad > inventarioMaximo) {
                        int exceso = stockActual + nuevaCantidad - inventarioMaximo;
                        throw new Exception("La cantidad solicitada supera el inventario máximo por " + exceso + " unidades");
                    }

                    if (articulo.getModeloInventario() == ModeloInventario.LoteFijo) {
                        Integer puntoPedido = articulo.getDatosModeloInventario().get(0).getPuntoPedido();
                        if (puntoPedido != null && stockActual != null && stockActual + nuevaCantidad <= puntoPedido) {
                            throw new Exception("La cantidad solicitada no supera el Punto de Pedido de "
                                    + puntoPedido + " unidades para el artículo con ID: " + articulo.getId());
                        }
                    }
                }
                primerDetalle.setCantidad(nuevaCantidad);
            } else if (nuevaCantidad != null) {
                throw new Exception("No se puede modificar la cantidad porque la orden no tiene detalles o la lista de detalles está vacía.");
            }

            ordenExistente.setFechaModificacionOrdenCompra(LocalDateTime.now());
            return ordenCompraRepository.save(ordenExistente);
        } catch (Exception e) {
            throw new Exception("Error al modificar la orden de compra: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public OrdenCompra cancelarOrdenCompra(Long ordenCompraId) throws Exception {
        try {
            OrdenCompra ordenCompra = ordenCompraRepository.findById(ordenCompraId)
                    .orElseThrow(() -> new EntityNotFoundException("Orden de Compra no encontrada con ID: " + ordenCompraId));

            if (ordenCompra.getEstadoOrdenCompra() != EstadoOrdenCompra.PENDIENTE) {
                throw new Exception("Solo se pueden cancelar órdenes de compra en estado PENDIENTE.");
            }
            ordenCompra.setEstadoOrdenCompra(EstadoOrdenCompra.CANCELADA);
            ordenCompra.setFechaModificacionOrdenCompra(LocalDateTime.now());
            ordenCompra.setFechaBaja(LocalDateTime.now());
            return ordenCompraRepository.save(ordenCompra);
        } catch (Exception e) {
            throw new Exception("Error al cancelar la orden de compra: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public OrdenCompra enviarOrdenCompra(Long ordenCompraId) throws Exception {
        try {
            OrdenCompra ordenCompra = ordenCompraRepository.findById(ordenCompraId)
                    .orElseThrow(() -> new EntityNotFoundException("Orden de Compra no encontrada con ID: " + ordenCompraId));

            if (ordenCompra.getEstadoOrdenCompra() != EstadoOrdenCompra.PENDIENTE) {
                throw new Exception("Solo se pueden enviar órdenes de compra que están en estado PENDIENTE.");
            }
            ordenCompra.setEstadoOrdenCompra(EstadoOrdenCompra.ENVIADA);
            ordenCompra.setFechaModificacionOrdenCompra(LocalDateTime.now());
            return ordenCompraRepository.save(ordenCompra);
        } catch (Exception e) {
            throw new Exception("Error al enviar la orden de compra: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public OrdenCompra finalizarOrdenCompra(Long ordenCompraId) throws Exception {
        try {
            OrdenCompra ordenCompra = ordenCompraRepository.findById(ordenCompraId)
                    .orElseThrow(() -> new EntityNotFoundException("Orden de Compra no encontrada con ID: " + ordenCompraId));

            if (ordenCompra.getEstadoOrdenCompra() != EstadoOrdenCompra.ENVIADA) {
                throw new Exception("Solo se pueden finalizar órdenes de compra que están en estado ENVIADA.");
            }

            if (ordenCompra.getDetalleOrdenCompra() != null) {
                for (DetalleOrdenCompra detalle : ordenCompra.getDetalleOrdenCompra()) {
                    Articulo articulo = detalle.getArticulo();
                    if (articulo != null) {
                        Integer cantidadComprada = detalle.getCantidad();
                        articulo.setStockActual(articulo.getStockActual() + cantidadComprada);
                        articuloRepository.save(articulo);

                        if (articulo.getModeloInventario() == ModeloInventario.LoteFijo
                                && articulo.getDatosModeloInventario() != null
                                && !articulo.getDatosModeloInventario().isEmpty()) {
                            Integer puntoPedido = articulo.getDatosModeloInventario().get(0).getPuntoPedido();
                            if (puntoPedido != null && articulo.getStockActual() <= puntoPedido) {
                                System.out.println("AVISO: El stock del artículo " + articulo.getNombreArticulo()
                                        + " no supera el Punto de Pedido de " + puntoPedido + " unidades");
                            }
                        }
                    }
                }
            }

            ordenCompra.setEstadoOrdenCompra(EstadoOrdenCompra.FINALIZADA);
            ordenCompra.setFechaModificacionOrdenCompra(LocalDateTime.now());
            return ordenCompraRepository.save(ordenCompra);
        } catch (Exception e) {
            throw new Exception("Error al finalizar la orden de compra: " + e.getMessage());
        }
    }


    @Override
    public boolean existeOrdenCompraActivaParaArticulo(Long articuloId) throws Exception {
        try {
            return ordenCompraRepository.existeOrdenCompraActivaArticulo(
                    articuloId,
                    EstadoOrdenCompra.PENDIENTE,
                    EstadoOrdenCompra.ENVIADA
            );
        } catch (Exception e) {
            throw new Exception("Error al verificar si existe orden de compra activa para el artículo: " + e.getMessage());
        }
    }


    @Override
    public List<OrdenCompra> findOrdenCompraByEstadoAndArticulo(EstadoOrdenCompra estado, Long articuloId) throws Exception {
        try {
            if (estado == null && articuloId == null) {
                return ordenCompraRepository.findOrdenCompraByEstadoAndArticulo(null, null);
            }
            return ordenCompraRepository.findOrdenCompraByEstadoAndArticulo(estado, articuloId);
        } catch (Exception e) {
            throw new Exception("Error al filtrar órdenes de compra: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public OrdenCompra crearOrdenAutomaticaParaLoteFijo(Articulo articulo, DatosModeloInventario datosModelo) throws Exception {

        if (articulo.getProveedorPredeterminado() == null) {
            throw new Exception("No se puede generar OC automática: El artículo '" +
                    articulo.getNombreArticulo() + "' no tiene proveedor predeterminado.");
        }
        if (datosModelo.getLoteOptimo() == null || datosModelo.getLoteOptimo() <= 0) {
            throw new Exception("No se puede generar OC automática: El artículo '" +
                    articulo.getNombreArticulo() + "' no tiene un Lote Óptimo definido.");
        }

        OrdenCompra nuevaOC = new OrdenCompra();
        nuevaOC.setProveedor(articulo.getProveedorPredeterminado());
        nuevaOC.setEstadoOrdenCompra(EstadoOrdenCompra.PENDIENTE);
        nuevaOC.setFechaModificacionOrdenCompra(LocalDateTime.now());
        nuevaOC.setAutomatica(true);

        DetalleOrdenCompra detalle = new DetalleOrdenCompra();
        detalle.setArticulo(articulo);
        detalle.setCantidad(datosModelo.getLoteOptimo());
        detalle.setOrdenCompra(nuevaOC);

        List<DetalleOrdenCompra> detalles = new ArrayList<>();
        detalles.add(detalle);
        nuevaOC.setDetalleOrdenCompra(detalles);

        return ordenCompraRepository.save(nuevaOC);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void crearOrdenCompraAutomatica() throws Exception {
        List<Articulo> articulos = articuloRepository
                .findByFechaBajaIsNullAndModeloInventario(ModeloInventario.IntervaloFijo);

        LocalDateTime ahora = LocalDateTime.now();

        for (Articulo articulo : articulos) {
            Integer tiempoRevision = articulo.getTiempoRevision();
            LocalDateTime ultimaRevision = articulo.getFechaUltimaRevision();

            boolean debeRevisarse = false;
            if (tiempoRevision != null) {
                if (ultimaRevision == null) {
                    debeRevisarse = true;
                } else {
                    debeRevisarse = !ultimaRevision.plusDays(tiempoRevision).isAfter(ahora);
                }
            }

            if (debeRevisarse) {
                Integer loteOptimo = null;
                if (articulo.getDatosModeloInventario() != null) {
                    for (DatosModeloInventario datos : articulo.getDatosModeloInventario()) {
                        if (datos.getModeloInventario() == ModeloInventario.IntervaloFijo) {
                            loteOptimo = datos.getLoteOptimo();
                            break;
                        }
                    }
                }

                if (loteOptimo != null && articulo.getProveedorPredeterminado() != null) {
                    if (existeOrdenCompraActivaParaArticulo(articulo.getId())) {
                        continue;
                    }
                    int cantidadNecesaria = loteOptimo - articulo.getStockActual();
                    if (cantidadNecesaria > 0) {
                        OrdenCompra oc = new OrdenCompra();
                        oc.setProveedor(articulo.getProveedorPredeterminado());
                        oc.setEstadoOrdenCompra(EstadoOrdenCompra.PENDIENTE);
                        oc.setFechaModificacionOrdenCompra(ahora);
                        oc.setAutomatica(true);

                        DetalleOrdenCompra detalle = new DetalleOrdenCompra();
                        detalle.setArticulo(articulo);
                        detalle.setCantidad(cantidadNecesaria);
                        detalle.setOrdenCompra(oc);

                        List<DetalleOrdenCompra> detalles = new ArrayList<>();
                        detalles.add(detalle);
                        oc.setDetalleOrdenCompra(detalles);

                        ordenCompraRepository.save(oc);
                    }
                }

                articulo.setFechaUltimaRevision(ahora);
                articuloRepository.save(articulo);
            }
        }
    }
}

