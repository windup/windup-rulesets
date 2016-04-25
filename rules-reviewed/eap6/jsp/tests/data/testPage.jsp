<%@ page import="java.io.*,java.util.*, javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="org.apache.commons.io.output.*" %>
<%@ page import="com.ibm.weblogic.*" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<%@ taglib prefix="some" uri="some.tld" %>
<%@taglib prefix="testSubstr" uri="/WEB-INF/Substr.taglib.tld"%>

<%@ attribute name="greeting" required="true" %>
<%@ attribute name="name" required="true" %>

<%!
    public boolean errorHandler( String field, String value, Exception ex ) {
        return true;
    }
%>

<jsp:useBean id="user" class="user.UserData" scope="session"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Simple JSP</title>
</head>
<%@ page import="java.util.NonExistent" %>
<%@ page import="java.util.Date" %>
<%@ page import="" %>
<body>
    <h3>Hello world</h3><br>
    <strong>Current Time is</strong>: <%=new Date() %>

    <c:out value="${'<tag> , &'}"/>
    <testSubstr:substring input="Windup greets you" start="0" end="6"/>
    <some:tag> body </some:tag>

    <%@ include file="included.jsp" %>
</body>
</html>