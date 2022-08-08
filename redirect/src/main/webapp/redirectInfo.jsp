<%@ page import="eu.apenet.redirects.util.Redirection" %>
<%@ page import="java.util.Locale" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<fmt:setBundle basename="i18n/Application"/>

<%
    Redirection redirection = (Redirection) request.getAttribute("redirection");
    Locale locale = (Locale) request.getAttribute("locale");

    String baseUrl = "archivesportaleurope.net";
    if (redirection.getType().equals(Redirection.REDIRECTION_TYPE_PORTAL)){
        baseUrl = "archivesportaleurope.net";
    }
    else if (redirection.getType().equals(Redirection.REDIRECTION_TYPE_WIKI)){
        baseUrl = "wiki.archivesportaleurope.net";
    }
    else if (redirection.getType().equals(Redirection.REDIRECTION_TYPE_APEF)){
        baseUrl = "archivesportaleuropefoundation.eu";
    }
    else if (redirection.getType().equals(Redirection.REDIRECTION_TYPE_BLOG)){
        baseUrl = "archivesportaleurope.blog";
    }
%>

<%--<c:set var="language" value="${locale.toString()}" scope="session" />--%>
<%--<fmt:setLocale value="${language}" />--%>

<html>
<head>
    <title>APEF Redirection Service</title>

    <link href="<%= request.getContextPath()%>/assets/image/favicon.ico" rel="Shortcut Icon" />

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@300;400;600&display=swap" rel="stylesheet">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa" crossorigin="anonymous"></script>

    <style>
        .my-ta-center {
            text-align: center!important;
        }
        .my-ta-right {
            text-align: center!important;
        }
        @media (min-width: 768px) {
            .h-md-100 {
                height: 100%;
            }
        }
        @media (min-width: 992px) {
            .h-lg-100 {
                height: 100%;
            }
        }
        @media (min-width: 1200px) {
            .h-xl-100 {
                height: 100%;
            }
            .my-ta-center {
                text-align: left!important;
            }
            .my-ta-right {
                text-align: right!important;
            }
        }
    </style>
</head>
<body style="font-family: 'Open Sans', sans-serif; ">

<body style="font-family: 'Open Sans', sans-serif; ">
<div class=".container-fluid">
    <div class="col-md-auto col-12 d-xl-none d-block h-50" style="text-align: center">
        <img style="height: 100%" src="<%= request.getContextPath()%>/assets/image/bg.png"/>
    </div>
    <div class="row align-items-center h-xl-100">
        <div class="col-xl-auto col-12 d-xl-block d-none">
            <img class="h-100" src="<%= request.getContextPath()%>/assets/image/bg.png"/>
        </div>
        <div class="col mt-4 mt-xl-0 p-5">
            <div class="row my-ta-center">
                <% if (redirection.isHandled()) { %>
                <% if (redirection.isIdNotFound()) { %>

                <h1 style="color: #b23063"><p><fmt:message key="label.title.2"/></p></h1>
                <br/><br/>
                <h3 style="font-weight: normal"><p>
                    <fmt:message key="label.message1.2">
                        <fmt:param><%= baseUrl %></fmt:param>
                    </fmt:message>
                </p></h3>
                <h3 style="font-weight: normal"><p><fmt:message key="label.message2.2"/></p></h3>
                <% } else { %>
                <h1 style="color: #b23063"><p><fmt:message key="label.title.1"/></p></h1>
                <br/><br/><br/><br/>
                <h3 style="font-weight: normal"><p>
                    <fmt:message key="label.message1.1">
                        <fmt:param><%= baseUrl %></fmt:param>
                    </fmt:message>
                </p></h3>
                <h3 style="font-weight: normal"><p><fmt:message key="label.message2.1"/></p></h3>
                <% } %>
                <% } else { %>
                <h1 style="color: #b23063"><p><fmt:message key="label.title.3"/></p></h1>
                <br/><br/>
                <h3 style="font-weight: normal"><p>
                    <fmt:message key="label.message1.3">
                        <fmt:param><%= baseUrl %></fmt:param>
                    </fmt:message>
                </p></h3>
                <h3 style="font-weight: normal"><p><fmt:message key="label.message2.3"/></p></h3>
                <% } %>

                <div class="my-ta-right" style="margin-top: 30px">
                    <a style="    background-color: #178aa8;padding: 12px;color: white;font-size: 25px;text-decoration: none;" href="<%= redirection.getNewUrl()%>"><fmt:message key="label.button"/></a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

<%--<div class=".container-fluid">--%>
<%--    <div class="row align-items-center" style="height: 100%">--%>
<%--        <div class="col-auto">--%>
<%--            <img style="height: 100%" src="<%= request.getContextPath()%>/assets/image/bg.png"/>--%>
<%--        </div>--%>
<%--        <div class="col" style="padding: 50px">--%>
<%--            <div class="row">--%>
<%--                <% if (redirection.isHandled()) { %>--%>
<%--                    <% if (redirection.isIdNotFound()) { %>--%>

<%--                    <h1 style="color: #b23063"><p><fmt:message key="label.title.2"/></p></h1>--%>
<%--                <br/><br/>--%>
<%--                    <h3 style="font-weight: normal"><p>--%>
<%--                        <fmt:message key="label.message1.2">--%>
<%--                            <fmt:param><%= baseUrl %></fmt:param>--%>
<%--                        </fmt:message>--%>
<%--                    </p></h3>--%>
<%--                    <h3 style="font-weight: normal"><p><fmt:message key="label.message2.2"/></p></h3>--%>
<%--                    <% } else { %>--%>
<%--                    <h1 style="color: #b23063"><p><fmt:message key="label.title.1"/></p></h1>--%>
<%--                <br/><br/><br/><br/>--%>
<%--                    <h3 style="font-weight: normal"><p>--%>
<%--                        <fmt:message key="label.message1.1">--%>
<%--                            <fmt:param><%= baseUrl %></fmt:param>--%>
<%--                        </fmt:message>--%>
<%--                    </p></h3>--%>
<%--                    <h3 style="font-weight: normal"><p><fmt:message key="label.message2.1"/></p></h3>--%>
<%--                    <% } %>--%>
<%--                <% } else { %>--%>
<%--                    <h1 style="color: #b23063"><p><fmt:message key="label.title.3"/></p></h1>--%>
<%--                <br/><br/>--%>
<%--                    <h3 style="font-weight: normal"><p>--%>
<%--                        <fmt:message key="label.message1.3">--%>
<%--                            <fmt:param><%= baseUrl %></fmt:param>--%>
<%--                        </fmt:message>--%>
<%--                    </p></h3>--%>
<%--                    <h3 style="font-weight: normal"><p><fmt:message key="label.message2.3"/></p></h3>--%>
<%--                <% } %>--%>

<%--                <div style="text-align: right; margin-top: 30px">--%>
<%--                    <a style="    background-color: #178aa8;padding: 12px;color: white;font-size: 25px;text-decoration: none;" href="<%= redirection.getNewUrl()%>"><fmt:message key="label.button"/></a>--%>
<%--                </div>--%>

<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>




</body>
</html>
