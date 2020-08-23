<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Corona Kit-Place Order(user)</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<hr/>

<c:if test="${saveOrderMsg != null }">
	<p><strong>${saveOrderMsg }</strong> <em> <a href="user?action=ordersummary&coronaKitId=${coronaKitId}">Show Order Summary</a></em></p>
</c:if>
<br>
<label>Corona Kit ID Number: </label>
<input type="text" value="${coronaKitId}"  readonly>

<label>Total Bill Amount: </label>
<input type="text" value="${totAmt}"  readonly>
 
<h3>Enter your address</h3>
	<form action="user?action=saveorder&coronaKitId=${coronaKitId}&totAmt=${totAmt}" method="POST">
		<div><label for="address">Enter Address</label> </div>
		<div><input type="text" id="address" name="address" size="300"> </div>
		<button>Save and Confirm Order</button>
	</form>
	<span></span>
	<a href="user?action=showproducts"><button>Cancel</button></a>
		
<br>

<hr/>	
	<jsp:include page="footer.jsp"/>
</body>
</html>