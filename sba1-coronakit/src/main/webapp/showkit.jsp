<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Corona Kit-My Kit(user)</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<hr/>

<label>Corona Kit ID: </label>
<input type="text" value="${coronaKitId}" readonly>
<br>
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
	<label for="totAmt">Total Bill Amount:</label>
	<input type="text" id="totAmt" name="totAmt" value="${totAmt}" readonly>

	<a href="user?action=placeorder&totAmt=${totAmt}&coronaKitId=${coronaKitId}"><button>Place Order</button></a>
	<span></span></t>
	<a href="user?action=showproducts"><button>Cancel</button></a>

<hr/>	
	<jsp:include page="footer.jsp"/>
</body>
</html>