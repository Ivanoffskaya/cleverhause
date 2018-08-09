<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="userName" value="${principal}"/>
<c:set var="hasUserName" value="${userName != null}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cleverhause.ru, welcome page!</title>
    <!-- Bootstrap -->
    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/styles/jumbotron-narrow.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <div class="header">
        <ul class="nav nav-pills pull-right">
            <li><a href="${contextPath}/home">Home</a></li>
            <li class="active"><a href="${contextPath}/myboard/myboard">Boards</a></li>
            <li><a href="#">About</a></li>
            <li><a href="${contextPath}/contacts">Contact</a></li>
        </ul>
        <h3 class="text-muted">Hello, ${userName}!</h3>
    </div>

    <div class="jumbotron">
        <h2>New Board Registration</h2>
        <div>
            <form:select path="country">
                <form:option value="0" label="Select"/>
                <form:options items="${countryList}" itemValue="countryId" itemLabel="countryName"/>
            </form:select>
            <form class="form-signin" method="post" role="form">
                <div class="form-group">
                    <input type="text" class="form-ctrlVal" name="boardId" required autofocus>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button class="btn btn-lg btn-primary btn-block" type="submit">Get Id</button>
                </div>
            </form>
        </div>
    </div>

    <div class="row marketing">
        <div class="col-lg-6">
        </div>
        <div class="col-lg-6">
        </div>
    </div>

    <div class="footer">
        <p>&copy; Ivanov's 2017</p>
    </div>

</div> <!-- /container -->

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<!--<script src="/resources/jQuery/jquery-3.2.1.min.js"></script>-->
<script src="${contextPath}/resources/jQuery/jquery-3.2.1.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>