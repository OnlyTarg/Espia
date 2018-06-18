import java.io.File;
import java.sql.*;

public class SQL {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./UsersHistory";

    static final String USER = "sa";
    static final String PASS = "";
    static final File file = new File("./UsersHistory.mv.db");


    Connection conn = null;
    Statement stmt = null;


    public SQL() {
        if (!file.exists()) {
        createSQL();
        System.out.println("Table created");
        }
        else{
            System.out.println("Table has already exist");
        }
    }


    public void createSQL() {
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL,USER,PASS);
                stmt = conn.createStatement();
                String sql =  "CREATE TABLE   UsersHistory " +
                        "(id INTEGER not NULL, " +
                        " DayOfWeek VARCHAR(255), " +
                        " User VARCHAR(255), " +
                        " Enter VARCHAR(255), " +
                        " Exit VARCHAR(255), " +
                        " HowExited VARCHAR(255))";
                stmt.executeUpdate(sql);
                stmt.close();
                conn.close();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }

    }

    public void addUser(String UserURL) {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql =  "CREATE TABLE   UsersHistory " +
                    "(id INTEGER not NULL, " +
                    " DayOfWeek VARCHAR(255), " +
                    " User VARCHAR(255), " +
                    " Enter VARCHAR(255), " +
                    " Exit VARCHAR(255), " +
                    " HowExited VARCHAR(255))";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }

    }
    public static void main(String[] args) {
new SQL();
    }
}