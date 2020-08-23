<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Corona Kit-All Products(user)</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<hr/>
<c:if test="${msg != null }">
	<p><strong>${msg }</strong> </p>
</c:if>
<hr/>		
<h5>Select the products to add to the Kit</h5>
<% String kitID = request.getParameter("kitID");%>
	<c:choose>
		<c:when test="${products == null || products.isEmpty() }">
			<p>No Products Found</p>
		</c:when>
		<c:otherwise>
			<table border="1" cellspacing="5px" cellpadding="5px">
				<tr>
					<th>Product ID#</th>
					<th>Product Name</th>
					<th>Product Description</th>
					<th>Product Cost</th>
					<th style="width:300px">Action</th>
				</tr>
				<c:forEach items="${products}" var="product">
					<tr>
						<td>${product.id}</td>
						<td>${product.productName}</td>
						<td>${product.productDescription}</td>
						<td>${product.cost}</td>
						<td>
							<form action="user?action=addnewitem&pId=${product.id}" method="POST">
								<label>Qty: <input type="number" id="qty" name="qty" /> </label>
								<button> Add to Kit</button>
							</form>
							<a href="user?action=deleteitem&pId=${product.id}&kId=${kitId}"><button>Delete from kit</button></a>	
						</td>
					</tr>
				</c:forEach>
				<br>
				<!--  confirm & Delete -->
				<a href="user?action=showkit&ckit=${coronakitID}"><button>SHOW KIT</button></a>
				<span></span></t></t>
				<a href="user?action=showproducts"><button>CANCEL</button></a>
			</table>
		</c:otherwise>
	</c:choose>

<hr/>	
	<jsp:include page="footer.jsp"/>
</body>
</html>