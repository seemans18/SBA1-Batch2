package com.iiht.evaluation.coronokit.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.iiht.evaluation.coronokit.exception.ProductException;
import com.iiht.evaluation.coronokit.model.ProductMaster;



public class ProductMasterDao {

	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection jdbcConnection;

	public ProductMasterDao(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

	protected void connect() throws SQLException {
		if (jdbcConnection == null || jdbcConnection.isClosed()) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				throw new SQLException(e);
			}
			jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		}
	}

	protected void disconnect() throws SQLException {
		if (jdbcConnection != null && !jdbcConnection.isClosed()) {
			jdbcConnection.close();
		}
	}
	
	// add DAO methods as per requirements
	
	public static final String INS_PROD_QRY = "INSERT INTO Products(pname,pcost,pdesc) VALUES(?,?,?)";
	public static final String UPD_PROD_QRY = "UPDATE Products set pname=?,pcost=?,pdesc=? WHERE pId=?";
	public static final String DEL_PROD_QRY = "DELETE FROM Products WHERE pId=?";
	public static final String GET_PROD_BY_ID_QRY = "SELECT pId,pname,pcost,pdesc FROM Products WHERE pId=?";
	public static final String GET_ALL_PRODS_QRY = "SELECT pId,pname,pcost,pdesc FROM Products";
	public static final String GET_PCOST_PRODUCT_QRY = "SELECT pcost FROM Products WHERE pId=? ";

	
	public void add(ProductMaster productmaster) throws ProductException, SQLException {

		if (productmaster != null) {
			connect();
			try (PreparedStatement pst = jdbcConnection.prepareStatement(INS_PROD_QRY);) {

				//pst.setInt(1, productmaster.getId());
				pst.setString(1, productmaster.getProductName());
				pst.setString(2, productmaster.getCost());
				pst.setString(3, productmaster.getProductDescription());

				pst.executeUpdate();
			} catch (SQLException exp) {
				throw new ProductException("Saving new product failed!");
			}
		}
		disconnect();
		//return productmaster;
	}
	
	public void save(ProductMaster productmaster) throws ProductException, SQLException {
		if (productmaster != null) {
			connect();
			try (PreparedStatement pst = jdbcConnection.prepareStatement(UPD_PROD_QRY);) {

				pst.setString(1, productmaster.getProductName());
				pst.setString(2, productmaster.getCost());
				pst.setString(3, productmaster.getProductDescription());
				pst.setInt(4, productmaster.getId());

				pst.executeUpdate();
			} catch (SQLException exp) {
				throw new ProductException("Upating the product failed!");
			}
		}
		disconnect();
		//return productmaster;
	}
	
	public void deleteById(int productId) throws ProductException, SQLException {
		boolean isDeleted = false;
		connect();
		try (PreparedStatement pst = jdbcConnection.prepareStatement(DEL_PROD_QRY);) {

			pst.setInt(1, productId);

			int rowsCount = pst.executeUpdate();

			isDeleted = rowsCount > 0;

		} catch (SQLException exp) {
			throw new ProductException("Deleting Product failed!");
		}
		disconnect();
		//return isDeleted;
	}
	
	public ProductMaster getById(int productId) throws ProductException, SQLException {
		ProductMaster productmaster = null;
		
		connect();
		try (PreparedStatement pst = jdbcConnection.prepareStatement(GET_PROD_BY_ID_QRY);) {

			pst.setInt(1, productId);
			
			ResultSet rs = pst.executeQuery();
			
			if(rs.next()) {
				productmaster = new ProductMaster();
				productmaster.setId(rs.getInt(1));
				productmaster.setProductName(rs.getString(2));
				productmaster.setCost(rs.getString(3));
				productmaster.setProductDescription(rs.getString(4));
			}
			rs.close();

		} catch (SQLException exp) {
			throw new ProductException("Fetching Product failed!");
		}
		disconnect();
		return productmaster;
	}

	public List<ProductMaster> getAll() throws ProductException, SQLException {
		List<ProductMaster> products = new ArrayList<>();
		connect();
		try (PreparedStatement pst = jdbcConnection.prepareStatement(GET_ALL_PRODS_QRY);) {
			
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				ProductMaster product = new ProductMaster();
				product.setId(rs.getInt(1));
				product.setProductName(rs.getString(2));
				product.setCost(rs.getString(3));
				product.setProductDescription(rs.getString(4));
				
				products.add(product);
			}
			rs.close();
			if(products.isEmpty()) {
				products=null;
			}

		} catch (SQLException exp) {
			throw new ProductException("Fetching Products failed!");
		}
		
		disconnect();
		return products;
	}
	
	public double getpCostBypId(int pID) throws SQLException, ProductException {
		double pCost=0;
		connect();
		try(PreparedStatement pst = jdbcConnection.prepareStatement(GET_PCOST_PRODUCT_QRY);){
			pst.setInt(1, pID);
			
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				pCost = Double.parseDouble(rs.getString(1));
			}
			rs.close();
		} catch (SQLException exp) {
			throw new ProductException("Fetching Cost of the Product failed!");
		}
		disconnect();
		return pCost;
	}

}