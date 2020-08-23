<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Corona Kit-Edit Product(Admin)</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<hr/>

<c:if test="${msg != null }">
	<p><strong>${msg }</strong> </p>
</c:if>
	
<h3>Edit Product</h3>
	
	<form action="admin?action=updateproduct" method="POST">
	
		<div>
			<label>Product Id</label>
			<input type="number" name="pId" value="${product.id }" readonly required/>
		</div>
		<div>
			<label>Product Name</label>
			<input type="text" name="pname" value="${product.productName }" required/>
		</div>
		<div>
			<label>Product Cost</label>
			<input type="text" name="pcost" value="${product.cost }" required/>
		</div>
		<div>
			<label>Product Description</label>
			<input type="text" name="pdesc" value="${product.productDescription}" required/>
		</div>
		<br>
		<button>SAVE</button> </t> 
		
	</form>
	<br>
	<div>
		<a href="admin?action=list"><button>CANCEL</button></a> 
	</div>
<hr/>	
	<jsp:include page="footer.jsp"/>
</body>
</html>