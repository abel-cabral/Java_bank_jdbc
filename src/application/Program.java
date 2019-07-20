package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) throws ParseException {		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("=============TEST 1: seller findById==============");
		Seller seller1 = sellerDao.findById(4);
		System.out.println(seller1);
		
		
		System.out.println("=============TEST 2: seller findByDepartament==============");
		List<Seller> seller2 = sellerDao.findByDepartment(2);		
		for (Seller ls : seller2) {
			System.out.println(ls);
		}
		
		System.out.println("=============TEST 3: seller findAll==============");
		List<Seller> seller3 = sellerDao.findAll();		
		for (Seller ls : seller3) {
			System.out.println(ls);
		}
		
		System.out.println("=============TEST 4: seller insert==============");
		Seller novo = new Seller(null, "Miguel de Freitas Reis", "miguelitosp@id.uff.br", sdf.parse("26/06/2019"), 2800.00, new Department(3, null));		
		//sellerDao.insert(novo);
		//System.out.println("Foi inserido no ID: " + novo.getId());
		
		System.out.println("=============TEST 5: seller update==============");
		novo = new Seller(13, "Ramones de Paula", "ramonPacheco.uff.br", sdf.parse("25/01/1976"), 5800.00, new Department(3, null));		
		//sellerDao.update(novo);
		//System.out.println("Alterado");
		
		System.out.println("=============TEST 5: seller delete==============");
		sellerDao.deleteById(8);		
		System.out.println("Sucesso ao deletar entradas");
	}

}
