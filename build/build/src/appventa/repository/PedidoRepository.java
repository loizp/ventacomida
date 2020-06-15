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
import java.sql.SQLException;
import java.sql.Statement;  
import java.sql.ResultSet; 
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author loizp
 */
public class PedidoRepository {
        
    public Pedido insertPedido(Pedido pedido) throws SQLException{
        ConexionDB conexionDB = new ConexionDB();
        Connection conn = conexionDB.getConexion();
        String sql = "INSERT INTO pedidos (nombre, fechaemision, estado)\n" +
                        "VALUES ('" + pedido.getNombre() + "', '" + pedido.getFechaEmision() + "', '" + pedido.getEstado() +"');";
        Statement st = conn.prepareStatement(sql); 
        st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        long key = -1L;
        ResultSet rs = st.getGeneratedKeys(); 
        if (rs != null && rs.next()) {
            key = rs.getLong(1);
        }
        pedido.setId(key);
        insertDetalle(pedido, conn);
        conn.close();
        return pedido;
    }
    
    private void insertDetalle (Pedido pedido, Connection conn) {
        try {
            Statement st = conn.createStatement();
            String sql = "DELETE FROM detventa WHERE idpedido = '" + pedido.getId() + "';";
            st.executeUpdate(sql);
            for(Producto pr : pedido.getProductos()) {
                sql = "INSERT INTO detventa (idpedido, idproducto, cantidad)\n"
                        + "VALUES ('" + pedido.getId() + "', '" + pr.getId() + "', '" + pr.getCantidad() +"');";
                st = conn.createStatement();
                st.executeUpdate(sql);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PedidoRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Pedido selectPedido(Long idpedido) throws SQLException{
        ConexionDB conexionDB = new ConexionDB();
        Connection conn = conexionDB.getConexion();
        Pedido pedido = new Pedido();
        String sql = "SELECT pedidos.id, pedidos.nombre, pedidos.fechaemision, pedidos.estado, "
                + "productos.id, productos.nombre, productos.precio, detventa.cantidad \n"
                + "FROM pedidos INNER JOIN detventa ON pedidos.id = detventa.idpedido \n"
                + "INNER JOIN productos ON detventa.idproducto = productos.id WHERE pedidos.id = " + idpedido + ";";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            pedido.setId(rs.getLong(1));
            pedido.setNombre(rs.getString(2));
            pedido.setFechaEmision(rs.getTimestamp(3));
            pedido.setEstado(rs.getString(4));
            Producto producto = new Producto(rs.getLong(5),rs.getString(6),"",rs.getBigDecimal(7),rs.getInt(8));
            pedido.getProductos().add(producto);
        }
        conn.close();
        return pedido;
    }
    
    public List<Pedido> allPedidosAtender() throws SQLException{
        ConexionDB conexionDB = new ConexionDB();
        Connection conn = conexionDB.getConexion();
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT pedidos.id, pedidos.nombre, pedidos.fechaemision, pedidos.estado FROM pedidos WHERE pedidos.estado IN ('S','P') ORDER BY pedidos.estado;";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            pedidos.add(new Pedido(rs.getLong(1),rs.getString(2),rs.getTimestamp(3),rs.getString(4)));
        }
        conn.close();
        return pedidos;
    }
    
    public void updateEstadoPedido(Pedido pedido) throws SQLException{
        ConexionDB conexionDB = new ConexionDB();
        Connection conn = conexionDB.getConexion();
        String sql = "UPDATE pedidos SET estado ='" + pedido.getEstado() + "'"
                + "WHERE id = "+ pedido.getId() +";";
        Statement st = conn.createStatement();
        st.executeUpdate(sql);
        conn.close();
    }
    
    public void updatePedido(Pedido pedido) throws SQLException{
        ConexionDB conexionDB = new ConexionDB();
        Connection conn = conexionDB.getConexion();
        String sql = "UPDATE pedidos SET nombre = '"+ pedido.getNombre() +"', estado ='" + pedido.getEstado() + "' "
                + "WHERE id = '"+ pedido.getId() +"';";
        Statement st = conn.createStatement();
        st.executeUpdate(sql);
        insertDetalle(pedido, conn);
        conn.close();
    }
}
