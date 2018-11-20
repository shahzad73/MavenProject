/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hadoop.hbase;

import java.sql.*;

public class Phoenix1 {

    public static void main(String[] args) {
        // Create variables
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        System.out.println("connecting to only ---------------------");
        
        try {
            // Connect to the database
            
            
            connection = DriverManager.getConnection("jdbc:phoenix");
            
            // Create a JDBC statement
            statement = connection.createStatement();

            // Execute our statements
            statement.executeUpdate("create table javatest (mykey integer not null primary key, mycolumn varchar)");
            statement.executeUpdate("upsert into javatest values (1,'Hello')");
            statement.executeUpdate("upsert into javatest values (2,'Java Application')");
            connection.commit();

            // Query for table
            ps = connection.prepareStatement("select * from javatest");
            rs = ps.executeQuery();
            System.out.println("Table Values");
            while(rs.next()) {
                Integer myKey = rs.getInt(1);
                String myColumn = rs.getString(2);
                System.out.println("\tRow: " + myKey + " = " + myColumn);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(ps != null) {
                try {
                    ps.close();
                }
                catch(Exception e) {}
            }
            if(rs != null) {
                try {
                    rs.close();
                }
                catch(Exception e) {}
            }
            if(statement != null) {
                try {
                    statement.close();
                }
                catch(Exception e) {}
            }
            if(connection != null) {
                try {
                    connection.close();
                }
                catch(Exception e) {}
            }
        }
    }
}

