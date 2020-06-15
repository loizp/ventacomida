/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appventa.repository;

import appventa.model.Pedido;
import appventa.model.Producto;
import appventa.repository.dao.ConexionDB;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author loizp
 */
public class ProductoRepository {
    public List<Producto> allProductos() throws SQLException{
        ConexionDB conexionDB = new ConexionDB();
        Connection conn = conexionDB.getConexion();
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT productos.id, productos.nombre, productos.precio FROM productos;";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            productos.add(new Producto(rs.getLong(1),rs.getString(2),"",rs.getBigDecimal(3),0));
        }
        conn.close();
        return productos;
    }
}
