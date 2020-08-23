package com.iiht.evaluation.coronokit.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iiht.evaluation.coronokit.dao.ProductMasterDao;
import com.iiht.evaluation.coronokit.exception.ProductException;
import com.iiht.evaluation.coronokit.model.ProductMaster;

@WebServlet("/admin")
public class AdminController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProductMasterDao productMasterDao;
	//private ProductMasterService productService;
	
	public void setProductMasterDao(ProductMasterDao productMasterDao) {
		this.productMasterDao = productMasterDao;
	}

	public void init(ServletConfig config) {
		String jdbcURL = config.getServletContext().getInitParameter("jdbcUrl");
		String jdbcUsername = config.getServletContext().getInitParameter("jdbcUsername");
		String jdbcPassword = config.getServletContext().getInitParameter("jdbcPassword");

		//this.productMasterDao = new ProductMasterDao("jdbc:mysql://localhost:3306/ProductsDB", "root", "root");
		this.productMasterDao = new ProductMasterDao(jdbcURL, jdbcUsername, jdbcPassword);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action =  request.getParameter("action");
		String viewName = "";
		try {
			switch (action) {
			case "login" : 
				viewName = adminLogin(request, response);	//done
				break;
			case "newproduct":
				viewName = showNewProductForm(request, response);	//done
				break;
			case "insertproduct":
				viewName = insertProduct(request, response);	//done
				break;
			case "deleteproduct":
				viewName = deleteProduct(request, response);	//done
				break;
			case "editproduct":
				viewName = showEditProductForm(request, response);	//done
				break;
			case "updateproduct":
				viewName = updateProduct(request, response);	//done
				break;
			case "list":
				viewName = listAllProducts(request, response);	//done  
				break;	
			case "logout":
				viewName = adminLogout(request, response);	
				break;	
			default : viewName = "notfound.jsp"; break;		
			}
		} catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}
		RequestDispatcher dispatch = 
					request.getRequestDispatcher(viewName);
		dispatch.forward(request, response);
		
		
	}

	private String adminLogout(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return "";
	}

	private String listAllProducts(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		String view = "";

		try {
			List<ProductMaster> products = this.productMasterDao.getAll();
			request.setAttribute("products", products);
			view = "listproducts.jsp";
		} catch (ProductException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errorPage.jsp";
		}
		return view;
	}

	private String updateProduct(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		ProductMaster product = new ProductMaster();

		product.setId(Integer.parseInt(request.getParameter("pId")));
		product.setProductName(request.getParameter("pname"));
		product.setCost(request.getParameter("pcost"));
		product.setProductDescription(request.getParameter("pdesc"));

		String view = "";

		try {
			if(isValidproduct(product)) {
				this.productMasterDao.save(product);
				request.setAttribute("msg", "Product Saved Successfully!	<a href=\"admin?action=list\">Product List</a>");
				view = "editproduct.jsp";
			}else {
				request.setAttribute("errMsg", "Product is invalid!!	<a href=\"admin?action=list\">Product List</a>");
				view=  "errorPage.jsp";
			}
		} catch (ProductException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errorPage.jsp";
		}
		return view;
	}

	private String showEditProductForm(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		String view = "";
		int pId = Integer.parseInt(request.getParameter("pId"));

		try {
			ProductMaster product = this.productMasterDao.getById(pId);
			request.setAttribute("product", product);
			view = "editproduct.jsp";
		} catch (ProductException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errorPage.jsp";
		}
		return view;
	}

	private String deleteProduct(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		int pId = Integer.parseInt(request.getParameter("pId"));
		String view = "";

		try {
			this.productMasterDao.deleteById(pId);	
			request.setAttribute("msg","Product deleted Successfully!!	<strong><a href=\"admin?action=list\">Refresh Product List</a></strong> before adding new product");
			view = "listproducts.jsp";
		} catch (ProductException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errorPage.jsp";
		}

		return view;
	}
	
	private String insertProduct(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		String view ="";
		ProductMaster product = new ProductMaster();

		//product.setId(Integer.parseInt(request.getParameter("pId")));
		product.setProductName(request.getParameter("pname"));
		product.setCost(request.getParameter("pcost"));
		product.setProductDescription(request.getParameter("pdesc"));
		
		try {
			if(isValidproduct(product)) {
				//setProductMasterDao(productMasterDao);
				this.productMasterDao.add(product);
				request.setAttribute("msg", "Product Saved Successfully!!	<a href=\"admin?action=list\">Product List</a>");
				view=  "newproduct.jsp";
			}else {
				request.setAttribute("errMsg", "Product is invalid!!	<a href=\"admin?action=list\">Product List</a>");
				view=  "errorPage.jsp";
			}
		} catch (ProductException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errorPage.jsp";
		}
		return view;
	}

	private String showNewProductForm(HttpServletRequest request, HttpServletResponse response) {
		ProductMaster product = new ProductMaster();
		request.setAttribute("product", product);
		String view = "newproduct.jsp";
		return view;
	}

	private String adminLogin(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		String view = "";
		
		//request.setAttribute("password", "admin");
		try {
			List<ProductMaster> products = this.productMasterDao.getAll();
			request.setAttribute("products", products);
			view = "listproducts.jsp";
		} catch (ProductException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errorPage.jsp";
		}
		return view;
	}

	//Validation methods for the inputs
	//private boolean isValidProductId(int productID) {
	//	return productID!=0 && productID>0;
	//}
	
	private boolean isValidProductName(String productName) {
		return productName!=null && (productName.length()>=3 || productName.length()<=30);
	}
	
	private boolean isValidProductCost(String productCost) {
		return Double.parseDouble(productCost)!=0.0 && Double.parseDouble(productCost)>0;
	}
	
	private boolean isValidProductDesc(String productDesc) {
		return productDesc!= null && productDesc.length()<100;
	}
	
	private boolean isValidproduct(ProductMaster productMaster) throws ProductException{
		List<String> errMsgs = new ArrayList<>();
		boolean isValid=true;
		
		if(productMaster!=null) {
			/*if(!isValidProductId(productMaster.getId())) {
				isValid=false;
				errMsgs.add("Product id can not be left blank and must be a positive number, Duplicates are not allowed");
			}*/
			if(!isValidProductName(productMaster.getProductName())) {
				isValid=false;
				errMsgs.add("Product name can not left blank and must be of 3 to 30 in length");
			}
			if(!isValidProductCost(productMaster.getCost())) {
				isValid=false;
				errMsgs.add("Product Cost can not be left blank and must be a positive number");
			}
			if(!isValidProductDesc(productMaster.getProductDescription())) {
				isValid=false;
				errMsgs.add("Product Description can not be left blank and must be of lenght <100 characters");
			}
			
			if(!errMsgs.isEmpty()) {
				throw new ProductException("Invalid Product: "+errMsgs);
			}
		}else {
			isValid=false;
			throw new ProductException("Product details are not supplied");
		}
				
		return isValid;
	}
		
}