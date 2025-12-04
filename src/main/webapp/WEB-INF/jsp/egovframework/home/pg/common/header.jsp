<%--
  Created by IntelliJ IDEA.
  User: joo
  Date: 2025. 11. 8.
  Time: 15:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <meta name="description" content="" />
    <meta name="author" content="" />
    <title>OnRoom</title>

    <!-- Favicon -->
    <link rel="icon" href="<c:url value='/assets/favicon.ico'/>"/>

    <!-- Core theme CSS (includes Bootstrap)-->
    <link href="<c:url value='/css/styles.css'/>" rel="stylesheet"/>
</head>
<body class="d-flex flex-column min-vh-100">
<!-- Responsive navbar-->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark py-4">
    <div class="container">
        <a class="navbar-brand" href="<c:url value='/'/>">OnRoom</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav ms-5 mb-2 mb-lg-0">
                <li class="nav-item mx-4"><a class="nav-link active" href="<c:url value='/'/>">홈</a></li>
                <li class="nav-item mx-4"><a class="nav-link active" href="<c:url value='/reservationList.do'/>">예약</a></li>
                <li class="nav-item mx-4"><a class="nav-link active" href="<c:url value='/boardList.do'/>">게시판</a></li>
            </ul>
            <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                <c:choose>
                    <c:when test="${isLogin}">
                        <li class="nav-item"><a class="nav-link active" href="<c:url value='/myPage/myInfoList.do'/>">
                            <strong style="text-decoration: underline; text-decoration-color: white; text-underline-offset: 3px;">${name}</strong> 님</a></li>
                        <li class="nav-item"><a class="nav-link active" href="<c:url value='/logout.do'/>">로그아웃</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item"><a class="nav-link active" href="<c:url value='/join.do'/>">회원가입</a></li>
                        <li class="nav-item"><a class="nav-link active" href="<c:url value='/login.do'/>">로그인</a></li></c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
<!-- Page header with logo and tagline-->
<header class="py-1 bg-light border-bottom mb-4">
    <div class="container">
        <div class="text-center my-4">
            <h1 class="fw-bolder">OnRoom</h1>
            <p class="lead mb-0">회의실 예약 웹 사이트</p>
        </div>
    </div>
</header>

<!-- 에러 처리 -->
<c:if test="${not empty sessionScope.errorMsg}">
<script>
    alert('${sessionScope.errorMsg}');
</script>
    <c:remove var="errorMsg" scope="session"/>
</c:if>



