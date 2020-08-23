<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
  <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Corona Kit-Order Summary(user)</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<hr/>

<h3>Order Summary</h3>
<!-- <% Object coronaKit = request.getParameter("orderSummary"); %> -->

<h4>Hello ${coronaKit.getPersonName()}!! Thank You for ordering with us. </h4>
<label><strong>Delivery Address:</strong></label>
<label>${coronaKit.getDeliveryAddress()}</label><br>
<label>Email : ${coronaKit.getEmail()}</label><br>
<label>Contact number : ${coronaKit.getContactNumber()}</label><br>

<label>Order Date : ${coronaKit.getOrderDate()}</label>

<h4> Products Ordered:</h4>

<c:choose>
		<c:when test="${kits == null || kits.isEmpty() }">
			<p>No Products Found in Kit</p>
		</c:when>
		<c:otherwise>
			<c:set var="totAmt" value="${0}"/>
			<table border="1" cellspacing="5px" cellpadding="5px" id="iTable">
				<tr >
					<th>Product ID#</th>
					<th>Product Name</th>
					<th>Product Quantity</th>
					<th>Product Cost</th>
				</tr>			
				<c:forEach items="${kits}" var="kit" varStatus="loop">
						<tr>
							<td>${kit.productId} </td>
							<td><c:out value="${productNames[loop.index]}"/></td>
							<td>${kit.quantity}</td>
							<td>${kit.amount}</td>
							<c:set var="totAmt" value="${totAmt + kit.amount}"/>
						</tr>
				</c:forEach>	
			</table>
		</c:otherwise>
	</c:choose>
	<br>
	<label for="totAmt"><strong>Total Bill Amount:</strong></label>
	</strong><input type="text" id="totAmt" name="totAmt" value="${totAmt}" readonly>
					
			
<hr/>	
	<jsp:include page="footer.jsp"/>
</body>
</html>