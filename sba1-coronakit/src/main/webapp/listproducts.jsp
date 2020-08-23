<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Corona Kit-All Products(Admin)</title>
</head>
<body>
<jsp:include page="header.jsp"/>

<hr/>

<form action="admin?action=newproduct" method="post">
	<input type="submit" value="Add Product">
</form>

<c:if test="${msg != null }">
	<p><strong>${msg }</strong> </p>
</c:if>
<hr/>
<c:choose>
		<c:when test="${products == null || products.isEmpty() }">
			<p>No Products Found</p>
		</c:when>
		<c:otherwise>
			<table border="1" cellspacing="5px" cellpadding="5px">
				<tr>
					<th>Product ID#</th>
					<th>Product Name</th>
					<th>Product Cost</th>
					<th>Product Description</th>
					<th>Action</th>
				</tr>
				<c:forEach items="${products}" var="product">
					<tr>
						<td>${product.id}</td>
						<td>${product.productName}</td>
						<td>${product.cost}</td>
						<td>${product.productDescription}</td>
						<td>
							<a href="admin?action=editproduct&pId=${product.id}">EDIT</a>
							<span></span>	
							<a href="admin?action=deleteproduct&pId=${product.id}">DELETE</a>					
						</td>
					</tr>
				</c:forEach>
			</table>
		</c:otherwise>
	</c:choose>

<hr/>	
	<jsp:include page="footer.jsp"/>
</body>
</html>