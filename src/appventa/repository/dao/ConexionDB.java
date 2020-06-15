/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appventa.repository.dao;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;  
import java.sql.DriverManager;  

/**
 *
 * @author loizp
 */
public class ConexionDB {
    private Connection conexion = null;
    public Connection getConexion(){
        if (conexion == null){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/pventa","root","1234567890");
            } catch (Exception ex) {
                Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }       
        return conexion;
    }
}
