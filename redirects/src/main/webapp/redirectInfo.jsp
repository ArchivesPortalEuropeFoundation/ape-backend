<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<fmt:setBundle basename="i18n/Application"/>

<%
    String newUrl = (String) request.getAttribute("newUrl");
%>

<html>
<body>
<h2><fmt:message key="label.welcome"/> --> Hello World! : <%= newUrl%></h2>
</body>
</html>
