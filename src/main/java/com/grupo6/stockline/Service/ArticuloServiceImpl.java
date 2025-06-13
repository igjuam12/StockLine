package com.grupo6.stockline.Service;
import com.grupo6.stockline.DTOs.DTOArticulo;
import com.grupo6.stockline.Entities.Articulo;
import com.grupo6.stockline.Entities.DatosModeloInventario;
import com.grupo6.stockline.Entities.DetalleOrdenCompra;
import com.grupo6.stockline.Entities.Proveedor;
import com.grupo6.stockline.Enum.EstadoOrdenCompra;
import com.grupo6.stockline.Enum.ModeloInventario;
import com.grupo6.stockline.Repositories.ArticuloRepository;
import com.grupo6.stockline.Repositories.BaseRepository;
import com.grupo6.stockline.Repositories.DetalleOrdenCompraRepository;
import com.grupo6.stockline.Repositories.ProveedorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



@Service
public class ArticuloServiceImpl extends BaseServiceImpl<Articulo, Long> implements ArticuloService {

    @Autowired
    ArticuloRepository articuloRepository;
    @Autowired
    DetalleOrdenCompraRepository detalleOrdenCompraRepository;
    @Autowired
    ProveedorRepository proveedorRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public ArticuloServiceImpl(BaseRepository<Articulo, Long> baseRepository,
                               ArticuloRepository articuloRepository) {
        super(baseRepository);
    }

    @Override
    public void save(Articulo articulo) throws Exception {
        try {
            if (articuloRepository.existsById(articulo.getId())) {
                throw new Exception("No se pudo crear el artículo: ya existe");
            }
            articuloRepository.save(articulo);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void update(Long id, Articulo articulo) throws Exception {
        try {
            if (!articuloRepository.existsById(id)) {
                throw new Exception("No se puede actualizar: entidad no encontrada con ID: " + id);
            }
            articulo.setId(id);
            articuloRepository.save(articulo);
        }catch (Exception e){
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
                if (dOC.getOrdenCompra().getEstadoOrdenCompra() == EstadoOrdenCompra.Pendiente
                        || dOC.getOrdenCompra().getEstadoOrdenCompra() == EstadoOrdenCompra.Enviada) {
                    tieneOrdenes = true;
                }
            }

            if(tieneOrdenes){
                throw new IllegalStateException("No se puede dar de baja: El articulo tiene ordenes pendiente");
            }

            if(articuloExistente.getStockActual() > 0){
                throw new IllegalStateException("No se puede dar de baja: Todavia hay unidades en Stock");
            }

            articuloExistente.setFechaBaja(LocalDate.now());
            save(articuloExistente);

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
                Boolean sinOrdenes = true;
                for (DetalleOrdenCompra dOC : detallesArticulos) {
                    if (dOC.getOrdenCompra().getEstadoOrdenCompra() == EstadoOrdenCompra.Pendiente
                            || dOC.getOrdenCompra().getEstadoOrdenCompra() == EstadoOrdenCompra.Enviada) {
                        sinOrdenes = false;
                    }
                }

                //Por cada Articulo me traigo todos los DatosModeloInventario y obtengo el ultimo(FechaBaja es Null)
                for (DatosModeloInventario dmi : datosInventario) {
                    //Verifico que StockActual <= PP y sinOrdenes sea Verdadero
                    if (dmi.getFechaBaja() == null &&
                            a.getStockActual() <= dmi.getPuntoPedido() && sinOrdenes) {
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

            List<DetalleOrdenCompra> ordenes = articulo.getDetalleOrdenCompra();
            Boolean tieneOrdenes = false;
            for(DetalleOrdenCompra dOC: ordenes)
            if (dOC.getOrdenCompra().getEstadoOrdenCompra() == EstadoOrdenCompra.Pendiente
                    || dOC.getOrdenCompra().getEstadoOrdenCompra() == EstadoOrdenCompra.Enviada) {
                tieneOrdenes = true;
            }
            if(tieneOrdenes){
                throw new IllegalStateException("No se puede asginar el consultor: El Articulo tiene una orden de compra " +
                        "Pendiente/Enviada");
            }

            articulo.setProveedorPredeterminado(proveedor);
            articuloRepository.save(articulo);

        }catch (IllegalStateException e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    public void calcularModeloInventario(Articulo articulo) throws Exception{
        try {
            if(articulo.getModeloInventario() == ModeloInventario.LoteFijo){
                modeloLoteFijo(articulo);
            } else if (articulo.getModeloInventario() == ModeloInventario.IntervaloFijo) {
                modeloIntervaloFijo(articulo);
            }
        }catch (IllegalStateException e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    public static void modeloLoteFijo(Articulo articulo) throws Exception{
        List<DatosModeloInventario> datosLista = articulo.getDatosModeloInventario();

        //Me traigo el ultimo DatosModelo Inventario y lo doy de baja
        for (DatosModeloInventario d: datosLista){
            if(d.getFechaBaja() == null){
                d.setFechaBaja(LocalDate.now());
            }
        }

        DatosModeloInventario datosNuevo = new DatosModeloInventario();

        Integer loteOptimo = 0;
        Integer puntoPedido = 0;
        Integer stockSeguridad = 0;

        datosNuevo.setLoteOptimo(loteOptimo);
        datosNuevo.setPuntoPedido(puntoPedido);
        datosNuevo.setStockSeguridad(stockSeguridad);
        datosNuevo.setFechaAlta(LocalDate.now());

        datosLista.add(datosNuevo);

        articulo.setDatosModeloInventario(datosLista);



    }

    public static void modeloIntervaloFijo(Articulo articulo) throws Exception{}


}
