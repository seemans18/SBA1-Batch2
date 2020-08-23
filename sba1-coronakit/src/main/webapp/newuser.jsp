<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Corona Kit-New User(user)</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<hr/>

<h3>Enter your details below:</h3>
	
	<form action="user?action=insertuser" method="POST">
	
		<div>
			<label>Full Name</label>
			<input type="text" name="pname"  required/>
		</div>
		<div>
			<label>Email ID</label>
			<input type="text" name="pemail"  required/>
		</div>
		<div>
			<label>Contact Number</label>
			<input type="text" name="pcontact" required/>
		</div>
		<button>SAVE</button>
	</form>

<hr/>	
	<jsp:include page="footer.jsp"/>
</body>
</html>