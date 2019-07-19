package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub

	}

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
		// TODO Auto-generated method stub
		return null;
	}

}
