package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;
import model.entities.Seller;

public class Program2 {

	public static void main(String[] args) {
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
				
		System.out.println("=============TEST 1: department findById==============");
		Department department = departmentDao.findById(4);
		System.out.println(department);
		
		System.out.println("=============TEST 2: department insert==============");
		//Department test = new Department(null, "Toys");
		//departmentDao.insert(test);
		//System.out.println(test.getId());
		
		System.out.println("=============TEST 2: department update==============");
		//test = new Department(5, "Fisk");
		//departmentDao.update(test);
		//System.out.println("Alterado");		
		
		System.out.println("=============TEST 2: department delete==============");
		//departmentDao.deleteById(5);
		//System.out.println("Sucesso ao deletar entradas");
		
		System.out.println("=============TEST 1: department findAll==============");
		List<Department> dept = departmentDao.findAll();		
		for (Department ls : dept) {
			System.out.println(ls);
		}
	}

}
