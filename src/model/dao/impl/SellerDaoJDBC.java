package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO seller\n"
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) VALUES\n" + "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
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
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE seller\n"
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? WHERE Id = ?");
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub

	}

	// Retorna dos dados de um funcionario
	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName\n" + "FROM seller INNER JOIN department\n"
							+ "ON seller.DepartmentId = department.Id\n" + "WHERE seller.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			// rs inicia no 0 da tabela, precisamos antes de avançar testar se nao é null
			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				Seller sel = instantiateSeller(rs, dep);
				return sel;
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
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller sel = new Seller(rs.getInt("Id"), rs.getString("Name"), rs.getString("Email"), rs.getDate("BirthDate"),
				rs.getDouble("BaseSalary"), dep);
		return sel;
	}

	// Ao inves de montar manualmente em cada funcao, reutilizamos;
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department(rs.getInt("DepartmentId"), rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT seller.*,department.Name as DepName FROM seller INNER JOIN department\n"
					+ "ON seller.DepartmentId = department.Id ORDER BY Name");
			rs = st.executeQuery();

			if (rs.next()) {
				Map<Integer, Department> map = new HashMap<Integer, Department>();
				List<Seller> seller = new ArrayList<Seller>();

				do {
					Department dep = map.get(rs.getInt("DepartmentId")); // Busca o id informado no map

					if (dep == null) {
						dep = instantiateDepartment(rs); // Gera um departamento desse ID
						map.put(rs.getInt("DepartmentId"), dep); // Add index com valor ao map
					}

					Seller aux = instantiateSeller(rs, dep);
					seller.add(aux);
				} while (rs.next());
				return seller;
			}
			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	// Retorna uma lista contendo todos funcionarios associados a um determinado
	// setor
	@Override
	public List<Seller> findByDepartment(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT seller.*,department.Name as DepName FROM seller INNER JOIN department\n"
					+ "ON seller.DepartmentId = department.Id WHERE DepartmentId = ?\n" + "ORDER BY Name");
			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				List<Seller> seller = new ArrayList<Seller>();
				do {
					Seller aux = instantiateSeller(rs, dep);
					seller.add(aux);
				} while (rs.next());
				return seller;
			}
			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
