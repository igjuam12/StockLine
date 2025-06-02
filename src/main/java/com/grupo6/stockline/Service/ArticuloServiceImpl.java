package com.grupo6.stockline.Service;
import com.grupo6.stockline.DTOs.DTOArticulo;
import com.grupo6.stockline.Entities.Articulo;
import com.grupo6.stockline.Entities.DatosModeloInventario;
import com.grupo6.stockline.Entities.DetalleOrdenCompra;
import com.grupo6.stockline.Enum.EstadoOrdenCompra;
import com.grupo6.stockline.Repositories.ArticuloRepository;
import com.grupo6.stockline.Repositories.BaseRepository;
import com.grupo6.stockline.Repositories.DetalleOrdenCompraRepository;
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
            if(articuloRepository.existsById(id)){
                articuloRepository.save(articulo);
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<DTOArticulo> listarArticulos() throws Exception {
        try {
            return articuloRepository.listarArticulos();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void bajaArticulo(Long id) throws Exception {
        try {
            Articulo articuloExistente = articuloRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Articulo no encontrado"));

            Long idArticulo = articuloExistente.getId();
            List<DetalleOrdenCompra> detallesArticulos =
                    detalleOrdenCompraRepository.obtenerDetallesPorArticulo(idArticulo);

            //Verifico que el Articulo no este en ninguna Orden de compra con Estado "Pendiente" o "Enviada"
            Boolean sinOrdenes = true;
            for (DetalleOrdenCompra dOC : detallesArticulos) {
                if (dOC.getOrdenCompra().getEstadoOrdenCompra() == EstadoOrdenCompra.Pendiente
                        || dOC.getOrdenCompra().getEstadoOrdenCompra() == EstadoOrdenCompra.Enviada) {
                    sinOrdenes = false;
                }
            }

            if(articuloExistente.getStockActual() == 0 && sinOrdenes){
                articuloExistente.setFechaBaja(LocalDate.now());
                save(articuloExistente);
            }

        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public DTOArticulo listarArticuloById(Long id) throws Exception {
        try {
            return articuloRepository.listarArticuloById(id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<DTOArticulo> listarArticulosReponer() throws Exception {
        try {
            List<DTOArticulo> listaArticulosAReponer = new ArrayList<>();

            //Obtengo todos los articulos que no esten dados de baja
            List<Articulo> articuloList = articuloRepository.findAll();

            for (Articulo a : articuloList) {
                List<DatosModeloInventario> datosInventario = a.getDatosModeloInventario();

                Long idArticulo = a.getId();
                List<DetalleOrdenCompra> detallesArticulos =
                        detalleOrdenCompraRepository.obtenerDetallesPorArticulo(idArticulo);

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
                    if (dmi.getFechaBaja() != null && a.getStockActual() <= dmi.getPuntoPedido() && sinOrdenes) {
                        DTOArticulo dtoArticulo = new DTOArticulo();

                        dtoArticulo.setId(a.getId());
                        dtoArticulo.setNombre(a.getNombreArticulo());
                        dtoArticulo.setNombreProveedor(a.getProveedorPredeterminado().getNombreProveedor());
                        dtoArticulo.setStockActual(a.getStockActual());
                        dtoArticulo.setStockSeguridad(dmi.getStockSeguridad());
                        dtoArticulo.setInventarioMaximo(dmi.getInventarioMaximo());
                        dtoArticulo.setValorPuntoPedido(dmi.getPuntoPedido());

                        listaArticulosAReponer.add(dtoArticulo);

                    }
                }
            }

            return listaArticulosAReponer;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<DTOArticulo> listarArticulosFaltantes() throws Exception {
        try {
            List<DTOArticulo> listaArticulosFaltantes = new ArrayList<>();

            //Me traigo todos los articulos
            List<Articulo> articuloList = articuloRepository.findAll();

            //Por cada Articulo me traigo todos los DatosModeloInventario relacionados
            for(Articulo a: articuloList){
                List<DatosModeloInventario> datosArticulo = a.getDatosModeloInventario();

                for(DatosModeloInventario dmi: datosArticulo){
                    //Verifico que fechaBajaDatosModeloInventario sea null y que el stockActual sea menor al StockSeguridad
                    if(dmi.getFechaBaja() != null && a.getStockActual() <= dmi.getStockSeguridad()){
                        DTOArticulo dtoArticulo = new DTOArticulo();

                        dtoArticulo.setId(a.getId());
                        dtoArticulo.setNombre(a.getNombreArticulo());
                        dtoArticulo.setNombreProveedor(a.getProveedorPredeterminado().getNombreProveedor());
                        dtoArticulo.setStockActual(a.getStockActual());
                        dtoArticulo.setStockSeguridad(dmi.getStockSeguridad());
                        dtoArticulo.setInventarioMaximo(dmi.getInventarioMaximo());
                        dtoArticulo.setValorPuntoPedido(dmi.getPuntoPedido());

                        listaArticulosFaltantes.add(dtoArticulo);
                    }
                }
            }

            return listaArticulosFaltantes;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }


}
