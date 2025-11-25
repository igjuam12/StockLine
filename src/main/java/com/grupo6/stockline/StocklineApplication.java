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

			// ==================================================================
			// PROVEEDORES
			// ==================================================================
			Proveedor proveedor1 = new Proveedor();
			proveedor1.setNombreProveedor("TechZone S.A.");
			proveedor1.setMailProveedor("contacto@techzone.com");
			proveedor1.setFechaAlta(LocalDate.now());
			proveedorRepo.save(proveedor1);

			Proveedor proveedor2 = new Proveedor();
			proveedor2.setNombreProveedor("Distribuidora Central");
			proveedor2.setMailProveedor("ventas@dcentral.com");
			proveedor2.setFechaAlta(LocalDate.now());
			proveedorRepo.save(proveedor2);

			Proveedor proveedor3 = new Proveedor();
			proveedor3.setNombreProveedor("Insumos GAMER SRL");
			proveedor3.setMailProveedor("pedidos@insumosgamer.com.ar");
			proveedor3.setFechaAlta(LocalDate.now());
			proveedorRepo.save(proveedor3);

			// ==================================================================
			// ARTÍCULOS
			// ==================================================================
			Articulo articulo1 = new Articulo();
			articulo1.setNombreArticulo("Monitor Samsung 24\"");
			articulo1.setDescripcionArticulo("Monitor LED 24 pulgadas Full HD 75Hz");
			articulo1.setCostoCompra(185000);
			articulo1.setCostoAlmacenamiento(950);
			articulo1.setCostoPedido(5000);
			articulo1.setDemandaArticulo(40);
			articulo1.setStockActual(10);
			articulo1.setModeloInventario(ModeloInventario.LoteFijo);
			articulo1.setFechaModificacionArticulo(LocalDate.now());
			articulo1.setProveedorPredeterminado(proveedor1);
			articulo1.setFechaAlta(LocalDate.now());
			articuloRepo.save(articulo1);

			Articulo articulo2 = new Articulo();
			articulo2.setNombreArticulo("Teclado Mecánico Redragon");
			articulo2.setDescripcionArticulo("Teclado gamer retroiluminado RGB, switch blue");
			articulo2.setCostoCompra(75000);
			articulo2.setCostoAlmacenamiento(600);
			articulo2.setCostoPedido(2500);
			articulo2.setDemandaArticulo(70);
			articulo2.setStockActual(25);
			articulo2.setModeloInventario(ModeloInventario.IntervaloFijo);
			articulo2.setFechaModificacionArticulo(LocalDate.now());
			articulo2.setProveedorPredeterminado(proveedor3);
			articulo2.setFechaAlta(LocalDate.now());
			articuloRepo.save(articulo2);

			Articulo articulo3 = new Articulo();
			articulo3.setNombreArticulo("Mouse Logitech G203");
			articulo3.setDescripcionArticulo("Mouse inalámbrico ergonómico Lightspeed");
			articulo3.setCostoCompra(48000);
			articulo3.setCostoAlmacenamiento(400);
			articulo3.setCostoPedido(2000);
			articulo3.setDemandaArticulo(60);
			articulo3.setStockActual(5);
			articulo3.setModeloInventario(ModeloInventario.LoteFijo);
			articulo3.setFechaModificacionArticulo(LocalDate.now());
			articulo3.setProveedorPredeterminado(proveedor2);
			articulo3.setFechaAlta(LocalDate.now());
			articuloRepo.save(articulo3);

			Articulo articulo4 = new Articulo();
			articulo4.setNombreArticulo("Webcam Logitech C920");
			articulo4.setDescripcionArticulo("Webcam Full HD 1080p con micrófono");
			articulo4.setCostoCompra(95000);
			articulo4.setCostoAlmacenamiento(350);
			articulo4.setCostoPedido(4000);
			articulo4.setDemandaArticulo(25);
			articulo4.setStockActual(15);
			articulo4.setModeloInventario(ModeloInventario.IntervaloFijo);
			articulo4.setFechaModificacionArticulo(LocalDate.now());
			articulo4.setProveedorPredeterminado(proveedor2);
			articulo4.setFechaAlta(LocalDate.now());
			articuloRepo.save(articulo4);

			Articulo articulo5 = new Articulo();
			articulo5.setNombreArticulo("Auriculares HyperX Cloud II");
			articulo5.setDescripcionArticulo("Auriculares gamer 7.1 con micrófono desmontable");
			articulo5.setCostoCompra(120000);
			articulo5.setCostoAlmacenamiento(750);
			articulo5.setCostoPedido(5000);
			articulo5.setDemandaArticulo(30);
			articulo5.setStockActual(22);
			articulo5.setModeloInventario(ModeloInventario.LoteFijo);
			articulo5.setFechaModificacionArticulo(LocalDate.now());
			articulo5.setProveedorPredeterminado(proveedor3);
			articulo5.setFechaAlta(LocalDate.now());
			articuloRepo.save(articulo5);


			// ==================================================================
			// ARTÍCULO - PROVEEDOR (usando setters)
			// ==================================================================
			ArticuloProveedor ap1 = new ArticuloProveedor();
			ap1.setProveedor(proveedor1);
			ap1.setArticulo(articulo1);
			ap1.setPrecioArticulo(184500.0);
			ap1.setDemoraEntrega(5);
			ap1.setCargoPedido(5000);
			ap1.setFechaAlta(LocalDate.now());
			articuloProveedorRepo.save(ap1);

			ArticuloProveedor ap2 = new ArticuloProveedor();
			ap2.setProveedor(proveedor2);
			ap2.setArticulo(articulo1);
			ap2.setPrecioArticulo(186000.0);
			ap2.setDemoraEntrega(7);
			ap2.setCargoPedido(5500);
			ap2.setFechaAlta(LocalDate.now());
			articuloProveedorRepo.save(ap2);

			ArticuloProveedor ap3 = new ArticuloProveedor();
			ap3.setProveedor(proveedor3);
			ap3.setArticulo(articulo2);
			ap3.setPrecioArticulo(74500.0);
			ap3.setDemoraEntrega(3);
			ap3.setCargoPedido(2500);
			ap3.setFechaAlta(LocalDate.now());
			articuloProveedorRepo.save(ap3);

			ArticuloProveedor ap4 = new ArticuloProveedor();
			ap4.setProveedor(proveedor1);
			ap4.setArticulo(articulo2);
			ap4.setPrecioArticulo(75500.0);
			ap4.setDemoraEntrega(6);
			ap4.setCargoPedido(2800);
			ap4.setFechaAlta(LocalDate.now());
			articuloProveedorRepo.save(ap4);

			ArticuloProveedor ap5 = new ArticuloProveedor();
			ap5.setProveedor(proveedor2);
			ap5.setArticulo(articulo3);
			ap5.setPrecioArticulo(47800.0);
			ap5.setDemoraEntrega(4);
			ap5.setCargoPedido(2000);
			ap5.setFechaAlta(LocalDate.now());
			articuloProveedorRepo.save(ap5);

			ArticuloProveedor ap6 = new ArticuloProveedor();
			ap6.setProveedor(proveedor3);
			ap6.setArticulo(articulo3);
			ap6.setPrecioArticulo(48200.0);
			ap6.setDemoraEntrega(5);
			ap6.setCargoPedido(2200);
			ap6.setFechaAlta(LocalDate.now());
			articuloProveedorRepo.save(ap6);

			ArticuloProveedor ap7 = new ArticuloProveedor();
			ap7.setProveedor(proveedor2);
			ap7.setArticulo(articulo4);
			ap7.setPrecioArticulo(94000.0);
			ap7.setDemoraEntrega(8);
			ap7.setCargoPedido(4000);
			ap7.setFechaAlta(LocalDate.now());
			articuloProveedorRepo.save(ap7);

			ArticuloProveedor ap8 = new ArticuloProveedor();
			ap8.setProveedor(proveedor3);
			ap8.setArticulo(articulo5);
			ap8.setPrecioArticulo(119500.0);
			ap8.setDemoraEntrega(2);
			ap8.setCargoPedido(5000);
			ap8.setFechaAlta(LocalDate.now());
			articuloProveedorRepo.save(ap8);

			ArticuloProveedor ap9 = new ArticuloProveedor();
			ap9.setProveedor(proveedor1);
			ap9.setArticulo(articulo5);
			ap9.setPrecioArticulo(121000.0);
			ap9.setDemoraEntrega(5);
			ap9.setCargoPedido(5200);
			ap9.setFechaAlta(LocalDate.now());
			articuloProveedorRepo.save(ap9);


			// ==================================================================
			// DATOS MODELO INVENTARIO
			// ==================================================================
			List<Articulo> articulos = List.of(articulo1, articulo2, articulo3, articulo4, articulo5);
			for (Articulo art : articulos) {
				DatosModeloInventario datos = new DatosModeloInventario();
				datos.setArticulo(art);
				datos.setInventarioMaximo(art.getStockActual() * 3);
				datos.setLoteOptimo(art.getDemandaArticulo() / 2);
				datos.setPuntoPedido( (int) (art.getStockActual() * 0.75) );
				datos.setStockSeguridad( (int) (art.getStockActual() * 0.25) );
				datos.setModeloInventario(art.getModeloInventario());
				datos.setFechaAlta(LocalDate.now());
				datosModeloRepo.save(datos);
			}

			// ==================================================================
			// ORDENES DE COMPRA + DETALLES (usando setters)
			// ==================================================================
			OrdenCompra orden1 = new OrdenCompra();
			orden1.setProveedor(proveedor1);
			orden1.setFechaModificacionOrdenCompra(LocalDate.now());
			orden1.setEstadoOrdenCompra(EstadoOrdenCompra.Pendiente);
			orden1.setFechaAlta(LocalDate.now().minusDays(2));
			ordenCompraRepo.save(orden1);

			DetalleOrdenCompra doc1 = new DetalleOrdenCompra();
			doc1.setOrdenCompra(orden1);
			doc1.setArticulo(articulo1);
			doc1.setCantidad(20);
			doc1.setFechaAlta(LocalDate.now());
			detalleOCRepo.save(doc1);

			DetalleOrdenCompra doc2 = new DetalleOrdenCompra();
			doc2.setOrdenCompra(orden1);
			doc2.setArticulo(articulo2);
			doc2.setCantidad(10);
			doc2.setFechaAlta(LocalDate.now());
			detalleOCRepo.save(doc2);

			OrdenCompra orden2 = new OrdenCompra();
			orden2.setProveedor(proveedor2);
			orden2.setFechaModificacionOrdenCompra(LocalDate.now());
			orden2.setEstadoOrdenCompra(EstadoOrdenCompra.Enviada);
			orden2.setFechaAlta(LocalDate.now().minusDays(5));
			ordenCompraRepo.save(orden2);

			DetalleOrdenCompra doc3 = new DetalleOrdenCompra();
			doc3.setOrdenCompra(orden2);
			doc3.setArticulo(articulo3);
			doc3.setCantidad(15);
			doc3.setFechaAlta(LocalDate.now());
			detalleOCRepo.save(doc3);

			OrdenCompra orden3 = new OrdenCompra();
			orden3.setProveedor(proveedor3);
			orden3.setFechaModificacionOrdenCompra(LocalDate.now());
			orden3.setEstadoOrdenCompra(EstadoOrdenCompra.Finalizada);
			orden3.setFechaAlta(LocalDate.now().minusDays(10));
			ordenCompraRepo.save(orden3);

			DetalleOrdenCompra doc4 = new DetalleOrdenCompra();
			doc4.setOrdenCompra(orden3);
			doc4.setArticulo(articulo5);
			doc4.setCantidad(30);
			doc4.setFechaAlta(LocalDate.now());
			detalleOCRepo.save(doc4);

			// ==================================================================
			// VENTAS + DETALLES (usando setters)
			// ==================================================================
			Venta venta1 = new Venta();
			venta1.setTotalVenta(479000);
			venta1.setFechaAlta(LocalDate.now().minusDays(1));
			ventaRepo.save(venta1);

			DetalleVenta dv1 = new DetalleVenta();
			dv1.setVenta(venta1);
			dv1.setArticulo(articulo1);
			dv1.setCantidad(2);
			dv1.setSubTotal(370000);
			dv1.setFechaAlta(LocalDate.now());
			detalleVentaRepo.save(dv1);

			DetalleVenta dv2 = new DetalleVenta();
			dv2.setVenta(venta1);
			dv2.setArticulo(articulo2);
			dv2.setCantidad(1);
			dv2.setSubTotal(75000);
			dv2.setFechaAlta(LocalDate.now());
			detalleVentaRepo.save(dv2);

			Venta venta2 = new Venta();
			venta2.setTotalVenta(459500);
			venta2.setFechaAlta(LocalDate.now());
			ventaRepo.save(venta2);

			DetalleVenta dv3 = new DetalleVenta();
			dv3.setVenta(venta2);
			dv3.setArticulo(articulo4);
			dv3.setCantidad(3);
			dv3.setSubTotal(285000);
			dv3.setFechaAlta(LocalDate.now());
			detalleVentaRepo.save(dv3);

			DetalleVenta dv4 = new DetalleVenta();
			dv4.setVenta(venta2);
			dv4.setArticulo(articulo5);
			dv4.setCantidad(1);
			dv4.setSubTotal(120000);
			dv4.setFechaAlta(LocalDate.now());
			detalleVentaRepo.save(dv4);

			DetalleVenta dv5 = new DetalleVenta();
			dv5.setVenta(venta2);
			dv5.setArticulo(articulo3);
			dv5.setCantidad(1);
			dv5.setSubTotal(48000);
			dv5.setFechaAlta(LocalDate.now());
			detalleVentaRepo.save(dv5);
		};
	}*/

}
