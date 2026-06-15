package RegistoFolgas;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String DATABASE_NAME = "banco_de_dados.db";
    private static final String DATABASE_PATH = "data/AgendamentoFolgas.db";
    
    public static Connection getConnection() throws Exception {
        try {
            Class.forName(DRIVER);
            String dbPath = Database.class.getClassLoader()
                    .getResource(DATABASE_NAME)
                    .getPath();
            String connectionString = "jdbc:sqlite:" + dbPath;
            return DriverManager.getConnection(connectionString);
        } catch (ClassNotFoundException | java.sql.SQLException e) {
            throw new Exception("Erro ao conectar à base de dados: " + e.getMessage());
        }
    }
    
    public static Connection getLocalConnection() throws Exception {
        try {
            Class.forName(DRIVER);
            String connectionString = "jdbc:sqlite:" + DATABASE_PATH;
            return DriverManager.getConnection(connectionString);
        } catch (ClassNotFoundException | java.sql.SQLException e) {
            throw new Exception("Erro ao conectar à base de dados local: " + e.getMessage());
        }
    }
}
