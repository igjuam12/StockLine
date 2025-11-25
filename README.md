# StockLine – Sistema de Inventarios

StockLine es un prototipo funcional para la gestión de inventarios en comercios minoristas.  
Incluye módulos de artículos, proveedores, ventas y órdenes de compra, integrando modelos cuantitativos como EOQ, Punto de Pedido, Stock de Seguridad e Índice de Costo Global de Inventario (CGI).

Este repositorio es una versión personal adaptada para mostrar mis aportes dentro del proyecto.

---

## ✨ Funcionalidades principales

### 🧾 Artículos
- ABM de artículos.
- Parámetros de inventario configurables: demanda, costos, proveedor predeterminado, modelo aplicado.
- Cálculos automáticos:
  - Lote Óptimo (EOQ)  
  - Punto de Pedido  
  - Stock de Seguridad  
  - Inventario Máximo  
  - CGI (Costo Global de Inventario)

### 🤝 Proveedores
- ABM completo de proveedores.
- Asociación proveedor–artículo:
  - Demora de entrega  
  - Precio unitario  
  - Costo por pedido  
- Validaciones que impiden eliminar proveedores en uso.

### 📦 Órdenes de Compra
- Alta de órdenes con sugerencia de proveedor y tamaño de lote.
- Estados: Pendiente, Enviada, Cancelada, Finalizada.
- Verificación de existencia de órdenes activas.
- Actualización automática de stock al finalizar una OC.

### 🛒 Ventas
- Registro de ventas con control de stock disponible.
- Disparador automático de OC según modelo configurado.

---

## 🧱 Tecnologías utilizadas

- **Java**
- **Spring Boot**
- **MySQL**
- **Thymeleaf** (si el proyecto incluye vistas)
- Arquitectura con capas separadas: controladores, servicios, repositorios.

---

## 👤 Aportes realizados en este proyecto

- Desarrollo completo del **módulo de Proveedores**:
  - ABM  
  - Asociación proveedor–artículo  
  - Validaciones comerciales y restricciones de eliminación  
- Implementación de la **generación automática de Órdenes de Compra**:
  - Evaluación de stock y Punto de Pedido  
  - Control de órdenes activas  
  - Creación automática según modelo EOQ / Intervalo Fijo  
  - Integración con módulo de ventas  
- Organización y adaptación del repositorio para mostrarlo como portfolio.

---

## 🗂 Estructura general

- `src/main/java/...` — Servicios, controladores, entidades y lógica de negocio.  
- `src/main/resources/application.properties` — Configuración (DB, puerto, etc.)  
- `src/main/resources/templates` — Vistas (si aplica).

---

## 🚀 Cómo ejecutar el proyecto

1. Clonar este repositorio.  
2. Crear una base de datos (ej. `stockline`).  
3. Configurar credenciales en la aplicación (usuario, contraseña, URL).  
4. Ejecutar desde el IDE o mediante el comando correspondiente.  
5. Acceder desde el navegador a la URL local generada por Spring Boot.
