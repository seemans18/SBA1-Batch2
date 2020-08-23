<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Corona Kit-Add New Product(Admin)</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<hr/>
<c:if test="${msg != null }">
	<p><strong>${msg }</strong> </p>
</c:if>

<h3>Add Product</h3>
	
	<form action="admin?action=insertproduct" method="POST">
	
		<!-- <div>
			<label>Product Id</label>
			<input type="number" name="pId" required/>
		</div>  -->
		<div>
			<label>Product Name</label>
			<input type="text" name="pname"  required/>
		</div>
		<div>
			<label>Product Cost</label>
			<input type="text" name="pcost"  required/>
		</div>
		<div>
			<label>Product Description</label>
			<input type="text" name="pdesc" required/>
		</div>
		<button>ADD</button>
	</form>
<hr/>	
	<jsp:include page="footer.jsp"/>
</body>
</html>