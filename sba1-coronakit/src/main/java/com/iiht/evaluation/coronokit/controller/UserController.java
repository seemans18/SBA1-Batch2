package com.iiht.evaluation.coronokit.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.iiht.evaluation.coronokit.dao.KitDao;
import com.iiht.evaluation.coronokit.dao.ProductMasterDao;
import com.iiht.evaluation.coronokit.exception.ProductException;
import com.iiht.evaluation.coronokit.model.CoronaKit;
import com.iiht.evaluation.coronokit.model.KitDetail;
import com.iiht.evaluation.coronokit.model.OrderSummary;
import com.iiht.evaluation.coronokit.model.ProductMaster;

@WebServlet("/user")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private KitDao kitDAO;
	private ProductMasterDao productMasterDao;

	public void setKitDAO(KitDao kitDAO) {
		this.kitDAO = kitDAO;
	}

	public void setProductMasterDao(ProductMasterDao productMasterDao) {
		this.productMasterDao = productMasterDao;
	}

	public void init(ServletConfig config) {
		
		String jdbcURL = config.getServletContext().getInitParameter("jdbcUrl");
		String jdbcUsername = config.getServletContext().getInitParameter("jdbcUsername");
		String jdbcPassword = config. getServletContext().getInitParameter("jdbcPassword");
		
		this.kitDAO = new KitDao(jdbcURL, jdbcUsername, jdbcPassword);
		this.productMasterDao = new ProductMasterDao(jdbcURL, jdbcUsername, jdbcPassword);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		
		String viewName = "";
		try {
			switch (action) {
			case "newuser":
				viewName = showNewUserForm(request, response);	//done
				break;
			case "insertuser":
				viewName = insertNewUser(request, response);	//done
				break;
			case "showproducts":
				viewName = showAllProducts(request, response);	//done
				break;	
			case "addnewitem":
				viewName = addNewItemToKit(request, response);	//done
				break;
			case "deleteitem":
				viewName = deleteItemFromKit(request, response);	//done
				break;
			case "showkit":
				viewName = showKitDetails(request, response);		//done
				break;
			case "placeorder":
				viewName = showPlaceOrderForm(request, response);	//done
				break;
			case "saveorder":
				viewName = saveOrderForDelivery(request, response);	//done
				break;	
			case "ordersummary":
				viewName = showOrderSummary(request, response);	//done
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

	
	private String showOrderSummary(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		String view="";
		try {
			HttpSession session = request.getSession();
			int coronaKitId = Integer.parseInt(request.getParameter("coronaKitId"));
			OrderSummary orderSummary ;
			
			setKitDAO(kitDAO);
			setProductMasterDao(productMasterDao);
			ProductMaster product;
			List<String> pNames = new ArrayList<String>();
			
			CoronaKit coronaKit = kitDAO.getCorKitById(coronaKitId);
			List<KitDetail> kits = kitDAO.getAll(coronaKitId);
			for(KitDetail k : kits) {
				product = productMasterDao.getById(k.getProductId());
				pNames.add(product.getProductName());
			}
			
			orderSummary = new OrderSummary(coronaKit, kits);
			session.setAttribute("orderSummary", orderSummary);
			session.setAttribute("coronaKit", coronaKit);
			session.setAttribute("pNames", pNames);
			session.setAttribute("kits", kits);
			view = "ordersummary.jsp";
		} catch (ProductException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errorPage.jsp";
		}
		
		return view;
	}

	private String saveOrderForDelivery(HttpServletRequest request, HttpServletResponse response) throws SQLException  {
		String view="";
		try {
			HttpSession session = request.getSession();
			
			CoronaKit coronaKit = new CoronaKit();
			
			int coronaKitId = Integer.parseInt(request.getParameter("coronaKitId"));
			double totAmt = Double.parseDouble(request.getParameter("totAmt"));
			String address =request.getParameter("address");
			
			coronaKit.setId(coronaKitId);
			coronaKit.setDeliveryAddress(address);
			coronaKit.setTotalAmount(totAmt);
			coronaKit.setOrderDate(LocalDate.now());
			coronaKit.setOrderFinalized(true);
			
			setKitDAO(kitDAO);
			boolean isSaved = kitDAO.updateOrderDetails(coronaKit);
			if(isSaved) {
				request.setAttribute("saveOrderMsg", "Order has been placed Successfully!!");
				view="placeorder.jsp";
			}else throw new ProductException("Saving Order details to the Database failed!!");
		} catch (ProductException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errorPage.jsp";
		}
		
		return view;
	}

	private String showPlaceOrderForm(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		//CoronaKit coronaKit = new CoronaKit();
		//coronaKit.setTotalAmount(totAmt);
		//int coronaKitId = Integer.parseInt(request.getParameter("coronaKitId"));
		
		double totAmt = Double.parseDouble(request.getParameter("totAmt"));
		session.setAttribute("totAmt", totAmt);		
		String view="placeorder.jsp";	
		return view;
	}

	private String showKitDetails(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		String view = "";
		
		try {
			HttpSession session = request.getSession();
			int coronaKitId = Integer.parseInt(request.getParameter("ckit"));
			
			List<String> pNames = new ArrayList<String>();
			ProductMaster product;
			setKitDAO(kitDAO);
			setProductMasterDao(productMasterDao);
			
			List<KitDetail> kits = kitDAO.getAll(coronaKitId);
			for(KitDetail k : kits) {
				product = productMasterDao.getById(k.getProductId());
				pNames.add(product.getProductName());
			}
			
			session.setAttribute("kits", kits); 
			session.setAttribute("productNames", pNames);
			session.setAttribute("coronaKitId", coronaKitId);
			view = "showkit.jsp";
		} catch (ProductException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errorPage.jsp";
		}
		return view;
	}

	private String deleteItemFromKit(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		String view="";
		try {
		HttpSession session = request.getSession();
		
		int kId = Integer.parseInt(request.getParameter("kId"));
		int pId = Integer.parseInt(request.getParameter("pId"));
		
		setKitDAO(kitDAO);
		kitDAO.deleteById(pId);
		//session.setAttribute("msg", "Item has been deleted from kit Successfully!!	<strong><a href=\"user?action=showproducts\">Refresh Product List</a></strong> before adding/deleting new item to kit");
		request.setAttribute("msg", "Item has been deleted from kit Successfully!!");
		request.setAttribute("qty", "0");
		request.setAttribute("msg1","Item deleted from kit");
		view="showproductstoadd.jsp";
		} catch (ProductException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errorPage.jsp";
		}
		return view;
	}

	private String addNewItemToKit(HttpServletRequest request, HttpServletResponse response) throws  SQLException {
		String view="";
		try {
		HttpSession session = request.getSession();
		int pId = Integer.parseInt(request.getParameter("pId"));
		int pQty = Integer.parseInt(request.getParameter("qty"));
		
		setKitDAO(kitDAO);
		setProductMasterDao(productMasterDao);
		double pCost = productMasterDao.getpCostBypId(pId);
		
		KitDetail kitDetail = new KitDetail();
		kitDetail.setProductId(pId);
		kitDetail.setQuantity(pQty);
		kitDetail.setAmount(pCost);
		
		String coronaKit_kitID;
		coronaKit_kitID = kitDAO.addNewItemToKit(kitDetail);
		
		int coronakitID= Integer.parseInt(coronaKit_kitID.split(";")[0]);
		int kitId= Integer.parseInt(coronaKit_kitID.split(";")[1]);
		kitDetail.setCoronaKitId(coronakitID);
		
		//session.setAttribute("msg", "Item has been added to kit Successfully!!	<strong><a href=\"user?action=showproducts\">Refresh Product List</a></strong> before adding/deleting new item to kit");
		session.setAttribute("coronakitID", coronakitID);
		request.setAttribute("msg", "Item has been added to kit Successfully!!");
		request.setAttribute("kitId",kitId);
		request.setAttribute("msg1","Item added to kit, Quantity:" + pQty);
		
		view = "showproductstoadd.jsp";
		} catch (ProductException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errorPage.jsp";
		}
		return view;	
		}

	private String showAllProducts(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		String view="";
		try{
			setProductMasterDao(productMasterDao);
			List<ProductMaster> products = productMasterDao.getAll();
			request.setAttribute("products",products);
			view=  "showproductstoadd.jsp";
		} catch (ProductException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errorPage.jsp";
		}
		return view;
	}

	private String insertNewUser(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		String view ="";
				
		try {
			HttpSession session = request.getSession(true);
			CoronaKit coronaKit = new CoronaKit();
			
			coronaKit.setPersonName(request.getParameter("pname"));
			coronaKit.setEmail(request.getParameter("pemail"));
			coronaKit.setContactNumber(request.getParameter("pcontact"));
			
			if(isValidUserDetails(coronaKit)) {
				setKitDAO(kitDAO);
				int coronakitId = kitDAO.addVisitorDetails(coronaKit);
				
				setProductMasterDao(productMasterDao);
				List<ProductMaster> products = productMasterDao.getAll();
				
				session.setAttribute("products",products);
				session.setAttribute("coronakitID",coronakitId);
				session.setAttribute("msg", "Your details have been saved Successfully!!");
				session.setAttribute("coronaKit", coronaKit);
				coronaKit.setId(coronakitId);
				
				view=  "showproductstoadd.jsp";
			}else {
				request.setAttribute("errMsg", "Details provided is invalid!!");
				view=  "errorPage.jsp";
			}
		} catch (ProductException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errorPage.jsp";
		}
		return view;
	}

	private String showNewUserForm(HttpServletRequest request, HttpServletResponse response) {
		String view="";
		//HttpSession session = request.getSession(true);
		//CoronaKit coronaKit = new CoronaKit();
		//session.setAttribute("coronaKit", coronaKit);
		view = "newuser.jsp";
		return view;
	}
	
	//Validation methods for the inputs
	private boolean isValidPersonName(String personName) {
		return personName!=null && (personName.length()>=3 || personName.length()<=30);
	}
	
	private boolean isValidEmail(String pEmail) {
		return pEmail != null;
	}
	
	private boolean isValidContact(String pContact) {
		return pContact!=null && pContact.matches("[1-9][0-9]{9}");
	}
	
	private boolean isValidUserDetails(CoronaKit coronaKit) throws ProductException{
		List<String> errMsgs = new ArrayList<>();
		boolean isValid=true;
		
		if(coronaKit!=null) {
			
			if(!isValidPersonName(coronaKit.getPersonName())) {
				isValid=false;
				errMsgs.add("Person name can not left blank and must be of 3 to 30 in length");
			}
			if(!isValidEmail(coronaKit.getEmail())) {
				isValid=false;
				errMsgs.add("Email cant be left blank");
			}
			if(!isValidContact(coronaKit.getContactNumber())) {
				isValid=false;
				errMsgs.add("Contact number cannot be left blank and the format should be correct");
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