package com.grupo6.stockline.Service;
import com.grupo6.stockline.Entities.*;
import com.grupo6.stockline.Enum.EstadoOrdenCompra;
import com.grupo6.stockline.Enum.ModeloInventario;
import com.grupo6.stockline.Repositories.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;
import static java.lang.Math.sqrt;


@Service
public class ArticuloServiceImpl extends BaseServiceImpl<Articulo, Long> implements ArticuloService {

    @Autowired
    ArticuloRepository articuloRepository;
    @Autowired
    DetalleOrdenCompraRepository detalleOrdenCompraRepository;
    @Autowired
    ProveedorRepository proveedorRepository;
    @Autowired
    ArticuloProveedorRepository articuloProveedorRepository;
    @Autowired
    DatosModeloInventarioRepository datosRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public ArticuloServiceImpl(BaseRepository<Articulo, Long> baseRepository,
                               ArticuloRepository articuloRepository) {
        super(baseRepository);
    }

    @Override
    public void save(Articulo articulo) throws Exception {
        try {
            articulo.setFechaAlta(LocalDateTime.now());
            articulo.setStockActual(0);
            articuloRepository.save(articulo);
        } catch (Exception e) {
            throw new Exception("Error al guardar el artículo: " + e.getMessage());
        }
    }

    @Override
    public void update(Long id, Articulo articulo) throws Exception {
        try {
            if (!articuloRepository.existsById(id)) {
                throw new Exception("No se puede actualizar: entidad no encontrada con ID: " + id);
            }
            // 4. Aquí, le asignas el ID al objeto nuevo que viene del formulario.
            articulo.setId(id);

            calcularModeloInventario(articulo.getId());

            // 5. ESTA LÍNEA ES LA FUENTE DEL ERROR.
            articuloRepository.save(articulo);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void bajaArticulo(Long id) throws Exception {
        try {
            Articulo articuloExistente = articuloRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("No se puede dar de baja: Articulo no encontrado"));

            Long idArticulo = articuloExistente.getId();

            List<DetalleOrdenCompra> detallesArticulos =
                    detalleOrdenCompraRepository.obtenerDetallesPorArticulo(idArticulo);

            //Verifico que el Articulo no este en ninguna Orden de compra con Estado "Pendiente" o "Enviada"
            Boolean tieneOrdenes = false;

            for (DetalleOrdenCompra dOC : detallesArticulos) {
                if (dOC.getOrdenCompra().getEstadoOrdenCompra() == EstadoOrdenCompra.PENDIENTE
                        || dOC.getOrdenCompra().getEstadoOrdenCompra() == EstadoOrdenCompra.ENVIADA) {
                    tieneOrdenes = true;
                }
            }

            if(tieneOrdenes){
                throw new IllegalStateException("No se puede dar de baja: El articulo tiene ordenes pendiente");
            }

            if(articuloExistente.getStockActual() > 0){
                throw new IllegalStateException("No se puede dar de baja: Todavia hay unidades en Stock");
            }

            articuloRepository.darDeBajaPorId(articuloExistente.getId());

        }catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    public List<Articulo> listarArticulosReponer() throws Exception {
        try {
            List<Articulo> listaArticulosAReponer = new ArrayList<>();

            //Obtengo todos los articulos que no esten dados de baja
            List<Articulo> articuloList = articuloRepository.findAll();

            for (Articulo a : articuloList) {
                List<DatosModeloInventario> datosInventario = a.getDatosModeloInventario();
                List<DetalleOrdenCompra> detallesArticulos = a.getDetalleOrdenCompra();

                //Verifico que el Articulo no este en ninguna Orden de compra con Estado "Pendiente" o "Enviada"
                boolean sinOrdenes = true;
                for (DetalleOrdenCompra dOC : detallesArticulos) {
                    if (dOC.getOrdenCompra().getEstadoOrdenCompra() == EstadoOrdenCompra.PENDIENTE
                            || dOC.getOrdenCompra().getEstadoOrdenCompra() == EstadoOrdenCompra.ENVIADA) {
                        sinOrdenes = false;
                        break;
                    }
                }

                //Por cada Articulo me traigo todos los DatosModeloInventario y obtengo el ultimo(FechaBaja es Null)
                for (DatosModeloInventario dmi : datosInventario) {
                    //Verifico que StockActual <= PP y sinOrdenes sea Verdadero

                    if (dmi.getFechaBaja() == null &&
                            dmi.getPuntoPedido() != null &&
                            a.getStockActual() <= dmi.getPuntoPedido() &&
                            sinOrdenes &&
                            a.getModeloInventario() == ModeloInventario.LoteFijo) {
                        listaArticulosAReponer.add(a);
                    }
                }
            }
            return listaArticulosAReponer;

        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    @Override
    public List<Articulo> listarArticulosFaltantes() throws Exception {
        try {
            List<Articulo> listaArticulosFaltantes = new ArrayList<>();

            //Me traigo todos los articulos
            List<Articulo> articuloList = articuloRepository.findAll();

            //Por cada Articulo me traigo todos los DatosModeloInventario relacionados
            for(Articulo a: articuloList){
                List<DatosModeloInventario> datosArticulo = a.getDatosModeloInventario();

                for(DatosModeloInventario dmi: datosArticulo){
                    //Verifico que fechaBajaDatosModeloInventario sea null y que el stockActual sea menor al StockSeguridad
                    if(dmi.getFechaBaja() == null &&
                            a.getStockActual() <= dmi.getStockSeguridad()){
                        listaArticulosFaltantes.add(a);
                    }
                }
            }

            return listaArticulosFaltantes;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }

    }

    @Override
    public void asignarProveedorPredeterminado(Articulo articulo, Long idProveedor) throws Exception{
        try {
            Proveedor proveedor = proveedorRepository.findById(idProveedor).
                    orElseThrow(() -> new IllegalArgumentException("No se pudo asignar el proveedor: No existe"));

            if(proveedor.getFechaBaja() != null){
                throw new IllegalStateException("No se pudo asignar el proveedor: Proveedor dado de baja");
            }

            articulo.setProveedorPredeterminado(proveedor);
            calcularModeloInventario(articulo.getId());
            articuloRepository.save(articulo);

        }catch (IllegalStateException e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    public Double calcularCGI(Long id) throws Exception{
        try {

            //Busco al articulo
            Articulo articulo = articuloRepository.findById(id).orElseThrow(() ->
                    new IllegalArgumentException("No se pudo calcular el CGI: Articulo no Encontrado"));

            //Busco el ArticuloProveedor del ProveedorPredeterminado
            ArticuloProveedor articuloProveedor = articuloProveedorRepository.findByProveedorAndArticulo(articulo.
                    getProveedorPredeterminado().getId(), id);

            //Buscos los DatosModeloInventario del modelo que no este dado de baja
            DatosModeloInventario datos = null;
            List<DatosModeloInventario> listaDatos = articulo.getDatosModeloInventario();
            for (DatosModeloInventario dmi : listaDatos) {
                if (dmi.getFechaBaja() == null) {
                    datos = dmi;
                }
            }

            Integer demandaArticulo = articulo.getDemandaArticulo();
            Integer costoAlmacenamiento = articulo.getCostoAlmacenamiento();

            if (articulo.getProveedorPredeterminado() == null){
                throw new IllegalStateException("No se ha asignado un proveedor predeterminado");
            }

            if (articuloProveedor.getCostoCompra() == 0 || articuloProveedor.getCostoPedido() == 0 || datos.getLoteOptimo() == null) {
                throw new IllegalStateException("No se puede calcular el CGI: datos incompletos");
            }

            double costoArticulo = articuloProveedor.getCostoCompra();
            double costoPedido = articuloProveedor.getCostoPedido();

            Integer loteOptimo = 0;
            if(articulo.getModeloInventario() == ModeloInventario.LoteFijo){
                loteOptimo = datos.getLoteOptimo();
            } else if (articulo.getModeloInventario() == ModeloInventario.IntervaloFijo) {
                loteOptimo = datos.getInventarioMaximo() - articulo.getStockActual();
            }

            //CGI = D*C + Cp*D/Q + Ca*Q/2
            Double cgi = (double) ((demandaArticulo*costoArticulo) + ((costoPedido*demandaArticulo)/loteOptimo) +
                    ((costoAlmacenamiento*loteOptimo)/2));

            System.out.println("CGI: " + cgi);

            return cgi;

        }catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    public void realizarAjuste(Long id, Integer cantAjuste) throws Exception {
        try {
            Articulo articulo = articuloRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("No se pudo realizar el ajuste: Artículo no encontrado con ID: " + id));

            if (articulo.getStockActual() < cantAjuste) {
                throw new IllegalStateException("No se puede realizar el ajuste: El stock actual ("
                        + articulo.getStockActual() + ") es menor que la cantidad a reducir (" + cantAjuste + ").");
            }


            articulo.setStockActual(articulo.getStockActual() - cantAjuste);
            articuloRepository.save(articulo);

        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    //Calcular los datos del modelo para cada proveedor del articulo o mostrarlo para el predeterminado
    public void calcularModeloInventario(Long id) throws Exception{
        try {
            Articulo articulo = articuloRepository.findById(id).orElseThrow(() ->
                    new IllegalArgumentException("No se pudo calcular el ModeloInventario: Articulo no Encontrado"));

            if(articulo.getModeloInventario() == ModeloInventario.LoteFijo){
                calcularModeloLoteFijo(articulo);
            } else if (articulo.getModeloInventario() == ModeloInventario.IntervaloFijo) {
                calcularModeloIntervaloFijo(articulo);
            }
        }catch (IllegalStateException e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void calcularModeloLoteFijo(Articulo articulo) throws Exception{
        try{
            List<DatosModeloInventario> datosLista = articulo.getDatosModeloInventario();

            //Me traigo el ultimo DatosModelo Inventario y lo doy de baja
            for (DatosModeloInventario d: datosLista){
                if(d.getFechaBaja() == null){
                    d.setFechaBaja(LocalDateTime.now());
                    datosRepository.save(d);
                }
            }

            ArticuloProveedor articuloProveedor = articuloProveedorRepository.findByProveedorAndArticulo(articulo.getProveedorPredeterminado().getId(),
                    articulo.getId());

            DatosModeloInventario datosNuevo = new DatosModeloInventario();

            //Q = sqrt(2*D*Cp/Ca)
            Integer loteOptimo = Math.toIntExact(round((sqrt(((2 * articulo.getDemandaArticulo()
                    * articuloProveedor.getCostoPedido()) / (articulo.getCostoAlmacenamiento()))))));

            //SS = z*de
            Integer stockSeguridad = Math.toIntExact(round((1.64 * sqrt(5 * articuloProveedor.getDemoraEntrega()))));

            //PP
            Integer puntoPedido = ((articulo.getDemandaArticulo()*articuloProveedor.getDemoraEntrega())/360)
                    + stockSeguridad;

            datosNuevo.setLoteOptimo(loteOptimo);
            datosNuevo.setPuntoPedido(puntoPedido);
            datosNuevo.setStockSeguridad(stockSeguridad);
            datosNuevo.setArticulo(articulo);
            datosNuevo.setModeloInventario(articulo.getModeloInventario());

            datosNuevo.setFechaAlta(LocalDateTime.now());
            datosLista.add(datosNuevo);
            articulo.setDatosModeloInventario(datosLista);
            datosRepository.save(datosNuevo);
            articuloRepository.save(articulo);

        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void calcularModeloIntervaloFijo(Articulo articulo) throws Exception{
        try {
            List<DatosModeloInventario> datosLista = articulo.getDatosModeloInventario();

            //Me traigo el ultimo DatosModelo Inventario y lo doy de baja
            for (DatosModeloInventario d : datosLista) {
                if (d.getFechaBaja() == null) {
                    d.setFechaBaja(LocalDateTime.now());
                    datosRepository.save(d);
                }
            }

            ArticuloProveedor articuloProveedor = articuloProveedorRepository.findByProveedorAndArticulo(articulo.getProveedorPredeterminado().getId(),
                    articulo.getId());


            DatosModeloInventario datosNuevo = new DatosModeloInventario();

            int stockSeguridad = Math.toIntExact(round(1.64 * 5 *
                    sqrt((articuloProveedor.getDemoraEntrega() + articulo.getTiempoRevision()))));


            int invMax = round((articulo.getDemandaArticulo()/360)*
                            (articuloProveedor.getDemoraEntrega() + articulo.getTiempoRevision()) + stockSeguridad);


            datosNuevo.setStockSeguridad(stockSeguridad);
            datosNuevo.setInventarioMaximo(invMax);

            datosNuevo.setArticulo(articulo);
            datosNuevo.setModeloInventario(articulo.getModeloInventario());

            datosNuevo.setFechaAlta(LocalDateTime.now());
            datosLista.add(datosNuevo);
            articulo.setDatosModeloInventario(datosLista);


            datosRepository.save(datosNuevo);

            articuloRepository.save(articulo);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}
