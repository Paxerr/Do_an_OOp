package DataBase;
 
 import java.sql.Connection;
 import java.sql.DriverManager;
 import java.sql.SQLException;
 
 public class JDBCUtil { 
     private static final String URL = "jdbc:mysql://localhost:3306/quanliguixe";
     private static final String USER = "root";
     private static final String PASSWORD = "quyet1110";
 
     public static Connection getConnection() {
         Connection c = null;
         try {
             
             Class.forName("com.mysql.cj.jdbc.Driver");
            
             c = DriverManager.getConnection(URL, USER, PASSWORD);
         } catch (ClassNotFoundException e) {
             System.err.println("Không tìm thấy driver MySQL: " + e.getMessage());
             e.printStackTrace();
         } catch (SQLException e) {
             System.err.println("Kết nối thất bại: " + e.getMessage());
             e.printStackTrace();
         }
         return c;
     }
 
     public static void closeConnection(Connection connection) {
         if (connection != null) {
             try {
                 connection.close();
             } catch (SQLException e) {
                 System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
                 e.printStackTrace();
             }
         }
     }
 }