<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*" %>
<h3>Corona Prevention Kit</h3>
<% Date dNow = new Date();
	   SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
%>
<h5>Time now: <%= ft.format(dNow) %></h5>
 <a href="admin?action=logout">Logout</a>
 <!-- 
<div style="float:right">
<form align="right" method="post">
  <a href="admin?action=logout">Logout</a>
</form>
</div>
 -->