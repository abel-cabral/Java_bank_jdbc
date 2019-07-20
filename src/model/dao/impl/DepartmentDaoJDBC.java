package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;
import model.entities.Seller;

public class DepartmentDaoJDBC implements DepartmentDao {
	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO department (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			int RowsAffected = st.executeUpdate();

			// Test para saber se houve inserçao
			if (RowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys(); // Captura o id/ids retornados
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id); // Assim nosso obj ja fica atualizado com o id que foi salvo no bd
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Error Inesperado, nenhuma linha alterada"); // Forçamos uma excessao caso
				// nenhuma linha seja gravada
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Department obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE department SET Id = ?, Name = ? WHERE Id = ?");
			st.setInt(1, obj.getId());
			st.setString(2, obj.getName());
			st.setInt(3, obj.getId());
			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM department WHERE Id = ?");
			st.setInt(1, id);
			int rowsAffected = st.executeUpdate();

			if (rowsAffected == 0) {
				throw new DbException("ID: " + id + " não encontrada na base de dados.");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM department WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();

			// rs inicia no 0 da tabela, precisamos antes de avançar testar se nao é null
			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				return dep;
			}
			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM department");
			rs = st.executeQuery();

			if (rs.next()) {
				List<Department> department = new ArrayList<Department>();
				do {
					Department dep = instantiateDepartment(rs);
					department.add(dep);
				} while (rs.next());
				return department;
			}
			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	// Ao inves de montar manualmente em cada funcao, reutilizamos;
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department(rs.getInt("Id"), rs.getString("Name"));
		return dep;
	}

}
