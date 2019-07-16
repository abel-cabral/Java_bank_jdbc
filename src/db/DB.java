package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {
	private static Connection conn = null;

	// Inicia/referencia uma conexao com o BD
	public static Connection getConnection() {
		// Se nao tiver conexao vamos iniciar o metodo de conectar
		if (conn == null) {
			try {
				Properties props = loadProperties();
				String url = props.getProperty("dburl");
				conn = DriverManager.getConnection(url, props);
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}

		}
		return conn;
	}

	// Finaliza a conexao com o BD
	public static void closeConection() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}

		}
	}

	// Carrega as propriedades
	private static Properties loadProperties() {
		try (FileInputStream fs = new FileInputStream("db.properties")) {
			Properties props = new Properties();
			props.load(fs);
			return props;
		} catch (IOException e) {
			throw new DbException(e.getMessage());
		}
	}

	public static void closeStatement(Statement x) {
		try {
			if (x != null) {
				x.close();
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	public static void closeResultSet(ResultSet n) {
		try {
			if (n != null) {
				n.close();
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}
}
