/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appventa.model;

import appventa.model.Producto;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author loizp
 */
public class Pedido {
    private long id;
    private String nombre;
    private Timestamp fechaEmision;
    private String estado;
    private List<Producto> productos = new ArrayList<>();

    public Pedido() {
        this.estado = "S";
        this.nombre = "";
        this.fechaEmision = new Timestamp(System.currentTimeMillis());
    }
    
    public Pedido(long id, String nombre, Timestamp fechaEmision, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.fechaEmision = fechaEmision;
        this.estado = estado;
    }

    public Pedido(String nombre) {
        this.nombre = nombre;
        this.fechaEmision = new Timestamp(System.currentTimeMillis());
        this.estado = "S";
    }
    
    public Pedido(long id, String estado) {
        this.id = id;
        this.estado = estado;
    }

    public long getId() {
        return id;
    }

    public String getCodigo() {
        return this.id + "-" + new SimpleDateFormat("yyyyMMdd").format(fechaEmision);
    }

    public String getNombre() {
        return nombre;
    }

    public Timestamp getFechaEmision() {
        return fechaEmision;
    }

    public String getEstado() {
        return estado;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFechaEmision(Timestamp fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
    
    public BigDecimal getMontoTotal() {
        BigDecimal monto = BigDecimal.valueOf(0.00);
        for(Producto producto : this.productos){
            monto = monto.add(producto.getPrecio().multiply(new BigDecimal(producto.getCantidad())));
        }
        return monto;
    }
}
