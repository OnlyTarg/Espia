package com.pav.avdonin.sql;

import java.io.File;
import java.sql.*;

public class SQL {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./UsersHistory";

    static final String USER = "sa";
    static final String PASS = "";
    static final File file = new File("./UsersHistory.mv.db");


    static Connection conn = null;
    static Statement stmt = null;
    static PreparedStatement prestmt = null;


    public SQL() {

    }


    public void createSQL() {
        if (!file.exists()) {

            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                stmt = conn.createStatement();
                String sql = "CREATE TABLE   UsersHistory " +
                        "(id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                        " DayOfWeek VARCHAR(255), " +
                        " User VARCHAR(255), " +
                        " Name VARCHAR(255), " +
                        " Enter VARCHAR(255), " +
                        " Exit VARCHAR(255), " +
                        " HowExited VARCHAR(255),"+
                        " Reason VARCHAR(255),"+
                        " Hash LONG)";
                stmt.executeUpdate(sql);
                stmt.close();
                conn.close();

                System.out.println("Table created");
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
        else{
            System.out.println("Table has already exist");
        }

    }

    public  void addEntering(String dayOfWeek, String UserIP,String UserName, String date, long hash) {
        int i=0;
        try {

            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            String sql = "INSERT INTO UsersHistory VALUES (NULL,?,?,?,?,NULL,NULL,NULL,?)";
            prestmt = conn.prepareStatement(sql);
            prestmt.setString(1,dayOfWeek);
            prestmt.setString(2,UserIP);
            prestmt.setString(3,UserName);
            prestmt.setString(4,date);
            prestmt.setLong(5,hash);
            prestmt.execute();



           /* String sqladd = "INSERT INTO UsersHistory VALUES('1','test')"+UserIP+date;
            stmt.executeUpdate(sql);*/
            prestmt.close();
            conn.close();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }

    }

    public void exitFromSession(String UserIP, String date, int hash, String reason) {
        try {

            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            //String sql = "INSERT INTO UsersHistory (Exit) VALUES (?) WHERE User = ?";
            String sql = "UPDATE UsersHistory SET Exit=? WHERE User=? AND HASH = ?";
            prestmt = conn.prepareStatement(sql);
            prestmt.setString(1,date);
            prestmt.setString(2,UserIP);
            prestmt.setInt(3,hash);
            prestmt.execute();
            sql = "UPDATE UsersHistory SET Reason=? WHERE User=? AND HASH = ?";
            prestmt = conn.prepareStatement(sql);
            prestmt.setString(1,reason);
            prestmt.setString(2,UserIP);
            prestmt.setInt(3,hash);
            prestmt.execute();
            prestmt.close();
            conn.close();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }

    }

    static public void goodExitInformer (long hash) {
        int i=0;
        try {

            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            String sql ="SELECT id FROM UsersHistory WHERE Hash= ?";
            prestmt = conn.prepareStatement(sql);
            prestmt.setLong(1,hash);
            ResultSet rs = prestmt.executeQuery();
            while (rs.next()){
                i = rs.getInt("id");
            }
             sql = "UPDATE UsersHistory SET HowExited='good' WHERE id=? ";

            prestmt = conn.prepareStatement(sql);

            prestmt.setInt(1,i);
            prestmt.execute();
            prestmt.close();
            conn.close();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }

    }

    public static void main(String[] args) {
new SQL().createSQL();
    }
}