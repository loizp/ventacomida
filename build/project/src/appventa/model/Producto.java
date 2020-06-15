/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appventa.model;

import java.math.BigDecimal;

/**
 *
 * @author loizp
 */
public class Producto {
    private long id;
    private String nombre;
    private BigDecimal precio = BigDecimal.valueOf(0.00);
    private String urlimg;
    private Integer cantidad;

    public Producto() {
    }

    public Producto(long id, String nombre, String urlimg, BigDecimal precio, Integer cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.urlimg = urlimg;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public void setUrlimg(String urlimg) {
        this.urlimg = urlimg;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public String getUrlimg() {
        return urlimg;
    }
}
