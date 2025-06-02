package com.grupo6.stockline.DTOs;

public class DTOArticulo {

    private Long id;
    private String nombre;
    private String nombreProveedor;
    private Integer stockActual;
    private Integer valorLoteOptimo;
    private Integer valorPuntoPedido;
    private Integer inventarioMaximo;
    private Integer stockSeguridad;

    public DTOArticulo() {
    }

    public DTOArticulo(Long id, String nombre, String nombreProveedor,
                       Integer stockActual, Integer valorLoteOptimo, Integer valorPuntoPedido,
                       Integer inventarioMaximo, Integer stockSeguridad) {
        this.id = id;
        this.nombre = nombre;
        this.nombreProveedor = nombreProveedor;
        this.stockActual = stockActual;
        this.valorLoteOptimo = valorLoteOptimo;
        this.valorPuntoPedido = valorPuntoPedido;
        this.inventarioMaximo = inventarioMaximo;
        this.stockSeguridad = stockSeguridad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    public Integer getValorLoteOptimo() {
        return valorLoteOptimo;
    }

    public void setValorLoteOptimo(Integer valorLoteOptimo) {
        this.valorLoteOptimo = valorLoteOptimo;
    }

    public Integer getValorPuntoPedido() {
        return valorPuntoPedido;
    }

    public void setValorPuntoPedido(Integer valorPuntoPedido) {
        this.valorPuntoPedido = valorPuntoPedido;
    }

    public Integer getInventarioMaximo() {
        return inventarioMaximo;
    }

    public void setInventarioMaximo(Integer inventarioMaximo) {
        this.inventarioMaximo = inventarioMaximo;
    }

    public Integer getStockSeguridad() {
        return stockSeguridad;
    }

    public void setStockSeguridad(Integer stockSeguridad) {
        this.stockSeguridad = stockSeguridad;
    }
}
