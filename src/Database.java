import java.sql.*;
import javax.swing.JOptionPane;

public class Database {
    public static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/school_admin_db", "schoolAdmin", "school");
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro fatal: Não foi possível se conectar com o banco de dados", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
