package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("=============TEST 1: seller findById==============");
		Seller seller1 = sellerDao.findById(4);
		System.out.println(seller1);
		
		
		System.out.println("=============TEST 2: seller findByDepartament==============");
		List<Seller> seller2 = sellerDao.findByDepartment(2);		
		for (Seller ls : seller2) {
			System.out.println(ls);
		}
	}

}
