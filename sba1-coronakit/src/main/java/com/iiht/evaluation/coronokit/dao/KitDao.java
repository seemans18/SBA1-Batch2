package com.iiht.evaluation.coronokit.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.iiht.evaluation.coronokit.exception.ProductException;
import com.iiht.evaluation.coronokit.model.CoronaKit;
import com.iiht.evaluation.coronokit.model.KitDetail;
import com.iiht.evaluation.coronokit.model.ProductMaster;



public class KitDao {

	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection jdbcConnection;

	public KitDao(String jdbcURL, String jdbcUsername, String jdbcPassword) {
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
	public static final String INS_VIS_DET_CORKIT_QRY = "INSERT INTO coronaKit(pname,pemail,pcontact) VALUES(?,?,?)";
	public static final String INS_NEW_ITEM_KIT_QRY = "INSERT INTO kitDetails(coronaKitId,pId,pQty,amt) VALUES(?,?,?,?)";
	public static final String DEL_ITEM_KIT_QRY = "DELETE FROM kitDetails WHERE pId=?";
	public static final String GET_ALL_KITS_QRY = "SELECT pId,pQty,amt FROM kitDetails WHERE coronaKitId =?";
	public static final String GET_CORKIT_BY_ID_QRY = "SELECT coronaKitId,pname,pemail,pcontact,totalBillAmt,vAddress,orderDate,orderFinalized FROM coronaKit WHERE coronaKitId =?";
	public static final String UPD_CORKIT_QRY = "UPDATE coronaKit set totalBillAmt=?,vAddress=?,orderDate=?,orderFinalized=? WHERE coronaKitId=?";
		
	
	public int addVisitorDetails(CoronaKit coronaKit) throws ProductException, SQLException {
		int coronakitID=0;
		if (coronaKit != null) {
			connect();
			try (PreparedStatement pst = jdbcConnection.prepareStatement(INS_VIS_DET_CORKIT_QRY);
				PreparedStatement pst1 = jdbcConnection.prepareStatement("SELECT coronaKitId FROM coronaKit ORDER BY coronaKitId DESC");) {

				//pst.setInt(1, productmaster.getId());
				pst.setString(1, coronaKit.getPersonName());
				pst.setString(2, coronaKit.getEmail());
				pst.setString(3, coronaKit.getContactNumber());

				pst.executeUpdate();
				
				ResultSet rs = pst1.executeQuery();
				if(rs.next()) {
					coronakitID = rs.getInt(1);
				}	
				rs.close();
			} catch (SQLException exp) {
				throw new ProductException("Saving Visitor details failed!");
			}
		}
		disconnect();
		return coronakitID;
	}

	public String addNewItemToKit(KitDetail kitDetail) throws ProductException, SQLException {
		int coronaKitID = 0;
		int kitID = 0;
		if (kitDetail != null) {
			connect();
			
			try (PreparedStatement pst = jdbcConnection.prepareStatement(INS_NEW_ITEM_KIT_QRY);
				PreparedStatement pst1 = jdbcConnection.prepareStatement("SELECT coronaKitId FROM coronaKit ORDER BY coronaKitId DESC");	
				PreparedStatement pst2 = jdbcConnection.prepareStatement("SELECT kitId FROM kitDetails ORDER BY kitId DESC");) {
				
				ResultSet rs = pst1.executeQuery();
				if(rs.next()) {
					coronaKitID = rs.getInt(1);
				}	
				rs.close();
				
				if(coronaKitID!=0) {
					pst.setInt(1, coronaKitID);
					pst.setInt(2, kitDetail.getProductId());
					pst.setInt(3, kitDetail.getQuantity());				
					pst.setDouble(4, kitDetail.getAmount()*kitDetail.getQuantity());
					
					pst.executeUpdate();
					
					//String kitID_Query = "SELECT kitId FROM kitDetails";
					ResultSet rs1 = pst2.executeQuery();
					if(rs1.next()) {
						kitID = rs1.getInt(1);
					}	
					rs1.close();
				}else throw new ProductException("Corona Kit ID is not valid");
			} catch (SQLException exp) {
				throw new ProductException("Adding new item to the Kit failed!");
			}
		}
		disconnect();	
		return coronaKitID + ";" + kitID;
	}
	
	public void deleteById(int productId) throws ProductException, SQLException {
		boolean isDeleted = false;
		connect();
		try (PreparedStatement pst = jdbcConnection.prepareStatement(DEL_ITEM_KIT_QRY);) {
			
			//pst.setInt(1, kitId);
			pst.setInt(1, productId);

			int rowsCount = pst.executeUpdate();

			isDeleted = rowsCount > 0;

		} catch (SQLException exp) {
			throw new ProductException("Deleting Product failed!");
		}
		disconnect();
		//return isDeleted;
	}

	public List<KitDetail> getAll(int coronaKitId) throws ProductException, SQLException {
		List<KitDetail> KitDetails = new ArrayList<>();
		connect();
		try (PreparedStatement pst = jdbcConnection.prepareStatement(GET_ALL_KITS_QRY);) {
			
			//(SELECT pname FROM Products WHERE pId=?)
			
			pst.setInt(1, coronaKitId);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				KitDetail kitDetail = new KitDetail();
				kitDetail.setProductId(rs.getInt(1));
				kitDetail.setQuantity(rs.getInt(2));
				kitDetail.setAmount(Double.parseDouble(rs.getString(3)));
				
				KitDetails.add(kitDetail);
			}
			rs.close();
			if(KitDetails.isEmpty()) {
				KitDetails=null;
			}

		} catch (SQLException exp) {
			throw new ProductException("Fetching Kit Details failed!");
		}
		
		disconnect();
		return KitDetails;
	}


	public boolean updateOrderDetails(CoronaKit coronaKitId) throws ProductException, SQLException {
		boolean isSaved = false;
		String isOrderFinalized = "no";
		connect();
		try (PreparedStatement pst = jdbcConnection.prepareStatement(UPD_CORKIT_QRY);) {
			//PreparedStatement pst1 = jdbcConnection.prepareStatement("UPDATE  CURRDATE()")
			
			//ResultSet rs = pst1.executeQuery();
			//rs.next();
			
			pst.setDouble(1, coronaKitId.getTotalAmount());
			pst.setString(2, coronaKitId.getDeliveryAddress());
			pst.setDate(3, Date.valueOf(coronaKitId.getOrderDate()));
			if(coronaKitId.isOrderFinalized())
				isOrderFinalized= "yes";
			pst.setString(4,isOrderFinalized);
			pst.setDouble(5, coronaKitId.getId());
			
			int rowsCount = pst.executeUpdate();

			isSaved = rowsCount > 0;
			//rs.close();
		} catch (SQLException exp) {
			throw new ProductException("Updating Order details failed!");
		}
		disconnect();
		return isSaved;
	}
	
	public CoronaKit getCorKitById(int coronaKitId) throws ProductException, SQLException {
		CoronaKit coronakit = null;
		Boolean OrderFinalized;
		
		connect();
		try (PreparedStatement pst = jdbcConnection.prepareStatement(GET_CORKIT_BY_ID_QRY);) {

			pst.setInt(1, coronaKitId);
			
			ResultSet rs = pst.executeQuery();
			
			if(rs.next()) {
				coronakit = new CoronaKit();
				coronakit.setId(rs.getInt(1));
				coronakit.setPersonName(rs.getString(2));
				coronakit.setEmail(rs.getString(3));
				coronakit.setContactNumber(rs.getString(4));
				coronakit.setTotalAmount(rs.getDouble(5));
				coronakit.setDeliveryAddress(rs.getString(6));
				coronakit.setOrderDate(rs.getDate(7).toLocalDate());
				if(rs.getString(8).equals("yes"))
					OrderFinalized = true;
				else
					OrderFinalized = false;
				coronakit.setOrderFinalized(OrderFinalized);	
			}
			rs.close();

		} catch (SQLException exp) {
			throw new ProductException("Fetching Product failed!");
		}
		disconnect();
		return coronakit;
	}

}