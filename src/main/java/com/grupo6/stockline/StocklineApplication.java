package com.grupo6.stockline;

import com.grupo6.stockline.Entities.*;
import com.grupo6.stockline.Enum.EstadoOrdenCompra;
import com.grupo6.stockline.Enum.ModeloInventario;
import com.grupo6.stockline.Repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class StocklineApplication {

	public static void main(String[] args) {
		SpringApplication.run(StocklineApplication.class, args);
	}

	/*@Bean
	public CommandLineRunner cargarDatosDePrueba(ArticuloRepository articuloRepo,
												 ProveedorRepository proveedorRepo,
												 OrdenCompraRepository ordenCompraRepo,
												 VentaRepository ventaRepo,
												 DetalleOrdenCompraRepository detalleOCRepo,
												 DetalleVentaRepository detalleVentaRepo,
												 ArticuloProveedorRepository articuloProveedorRepo,
												 DatosModeloInventarioRepository datosModeloRepo) {
		return args -> {

			// PROVEEDORES
			Proveedor proveedor1 = new Proveedor();
			proveedor1.setNombreProveedor("TechZone S.A.");
			proveedor1.setMailProveedor("contacto@techzone.com");
                        proveedor1.setFechaAlta(LocalDateTime.now());
			proveedorRepo.save(proveedor1);

			Proveedor proveedor2 = new Proveedor();
			proveedor2.setNombreProveedor("Distribuidora Central");
			proveedor2.setMailProveedor("ventas@dcentral.com");
                        proveedor2.setFechaAlta(LocalDateTime.now());
			proveedorRepo.save(proveedor2);

			// ARTÍCULOS
			Articulo articulo1 = new Articulo();
			articulo1.setNombreArticulo("Monitor Samsung 24\"");
			articulo1.setDescripcionArticulo("Monitor LED 24 pulgadas Full HD");
			articulo1.setCostoCompra(80000);
			articulo1.setCostoAlmacenamiento(800);
			articulo1.setCostoPedido(300);
                        articulo1.setDemandaArticulo(40);
                        articulo1.setStockActual(10);
                        articulo1.setModeloInventario(ModeloInventario.LoteFijo);
                        articulo1.setFechaModificacionArticulo(LocalDate.now());
                        articulo1.setFechaUltimaRevision(LocalDateTime.now());
                        articulo1.setTiempoRevision(1);
                        articulo1.setProveedorPredeterminado(proveedor1);
                        articulo1.setFechaAlta(LocalDateTime.now());
                        articuloRepo.save(articulo1);

			Articulo articulo2 = new Articulo();
			articulo2.setNombreArticulo("Teclado Mecánico Redragon");
			articulo2.setDescripcionArticulo("Teclado gamer retroiluminado");
			articulo2.setCostoCompra(25000);
			articulo2.setCostoAlmacenamiento(500);
			articulo2.setCostoPedido(150);
			articulo2.setDemandaArticulo(70);
                        articulo2.setStockActual(25);
                        articulo2.setModeloInventario(ModeloInventario.IntervaloFijo);
                        articulo2.setFechaModificacionArticulo(LocalDate.now());
                        articulo2.setFechaUltimaRevision(LocalDateTime.now().minusDays(2));
                        articulo2.setTiempoRevision(1);
                        articulo2.setProveedorPredeterminado(proveedor1);
                        articulo2.setFechaAlta(LocalDateTime.now());
                        articuloRepo.save(articulo2);

			Articulo articulo3 = new Articulo();
			articulo3.setNombreArticulo("Mouse Logitech");
			articulo3.setDescripcionArticulo("Mouse inalámbrico ergonómico");
			articulo3.setCostoCompra(18000);
			articulo3.setCostoAlmacenamiento(400);
			articulo3.setCostoPedido(120);
			articulo3.setDemandaArticulo(60);
                        articulo3.setStockActual(30);
                        articulo3.setModeloInventario(ModeloInventario.LoteFijo);
                        articulo3.setFechaModificacionArticulo(LocalDate.now());
                        articulo3.setFechaUltimaRevision(LocalDateTime.now());
                        articulo3.setTiempoRevision(1);
                        articulo3.setProveedorPredeterminado(proveedor2);
                        articulo3.setFechaAlta(LocalDateTime.now());
                        articuloRepo.save(articulo3);

			// ARTÍCULO - PROVEEDOR
			List<Articulo> articulos = List.of(articulo1, articulo2, articulo3);
			for (Articulo art : articulos) {
				ArticuloProveedor ap = new ArticuloProveedor();
				ap.setArticulo(art);
				ap.setProveedor(art.getProveedorPredeterminado());
				ap.setCargoPedido(art.getCostoPedido());
				ap.setDemoraEntrega(5);
				ap.setPrecioArticulo(art.getCostoCompra() - 1000); // precio menor al costo por ejemplo
                                ap.setFechaAlta(LocalDateTime.now());
				articuloProveedorRepo.save(ap);
			}

			// DATOS MODELO INVENTARIO
			for (Articulo art : articulos) {
				DatosModeloInventario datos = new DatosModeloInventario();
				datos.setArticulo(art);
				datos.setInventarioMaximo(120);
				datos.setLoteOptimo(20);
				datos.setPuntoPedido(15);
				datos.setStockSeguridad(5);
				datos.setModeloInventario(art.getModeloInventario());
                                datos.setFechaAlta(LocalDateTime.now());
				datosModeloRepo.save(datos);
			}

			// ORDENES DE COMPRA + DETALLES
			OrdenCompra orden1 = new OrdenCompra();
			orden1.setProveedor(proveedor1);
                        orden1.setFechaModificacionOrdenCompra(LocalDateTime.now());
			orden1.setEstadoOrdenCompra(EstadoOrdenCompra.PENDIENTE);
                        orden1.setFechaAlta(LocalDateTime.now());
			ordenCompraRepo.save(orden1);

			DetalleOrdenCompra doc1 = new DetalleOrdenCompra();
			doc1.setOrdenCompra(orden1);
			doc1.setArticulo(articulo1);
			doc1.setCantidad(20);
                        doc1.setFechaAlta(LocalDateTime.now());
			detalleOCRepo.save(doc1);

			OrdenCompra orden2 = new OrdenCompra();
			orden2.setProveedor(proveedor2);
                        orden2.setFechaModificacionOrdenCompra(LocalDateTime.now());
			orden2.setEstadoOrdenCompra(EstadoOrdenCompra.ENVIADA);
                        orden2.setFechaAlta(LocalDateTime.now());
			ordenCompraRepo.save(orden2);

			DetalleOrdenCompra doc2 = new DetalleOrdenCompra();
			doc2.setOrdenCompra(orden2);
			doc2.setArticulo(articulo3);
			doc2.setCantidad(15);
                        doc2.setFechaAlta(LocalDateTime.now());
			detalleOCRepo.save(doc2);

			// VENTAS + DETALLES
			Venta venta1 = new Venta();
			venta1.setTotalVenta(270000);
                        venta1.setFechaAlta(LocalDateTime.now());
			ventaRepo.save(venta1);

			DetalleVenta dv1 = new DetalleVenta();
			dv1.setVenta(venta1);
			dv1.setArticulo(articulo1);
			dv1.setCantidad(2);
			dv1.setSubTotal(160000);
                        dv1.setFechaAlta(LocalDateTime.now());
			detalleVentaRepo.save(dv1);

			DetalleVenta dv2 = new DetalleVenta();
			dv2.setVenta(venta1);
			dv2.setArticulo(articulo3);
			dv2.setCantidad(3);
			dv2.setSubTotal(110000);
                        dv2.setFechaAlta(LocalDateTime.now());
			detalleVentaRepo.save(dv2);
		};
	}*/

}
