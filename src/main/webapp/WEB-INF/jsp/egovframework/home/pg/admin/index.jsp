<%--
  Created by IntelliJ IDEA.
  User: joo
  Date: 2025. 11. 25.
  Time: 17:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
  <meta name="description" content="" />
  <meta name="author" content="" />
  <title>OnRoom 관리자 홈 페이지</title>
  <!-- Favicon-->
  <link rel="icon" href="<c:url value='/assets/favicon.ico'/>"/>
  <!-- Core theme CSS (includes Bootstrap)-->
  <link href="<c:url value='/css/styles.css'/>" rel="stylesheet"/>

  <style>
    /* 기본 hover: primary 색상 */
    #sidebar-wrapper .list-group-item:hover {
      background-color: var(--bs-primary) !important;
      color: #fff !important;
    }

    /* active는 항상 primary 유지 */
    #sidebar-wrapper .list-group-item.active {
      background-color: var(--bs-primary) !important;
      color: #fff !important;
      border: none !important;
    }
  </style>

</head>
<body>
<div class="d-flex" id="wrapper">
  <!-- Sidebar-->
  <jsp:include page="/WEB-INF/jsp/egovframework/home/pg/admin/common/sidebar.jsp"></jsp:include>

  <!-- Page content wrapper-->
  <div id="page-content-wrapper">
    <!-- Top navigation-->
    <nav class="navbar navbar-expand-lg bg-light border-bottom">

    <div class="container-fluid">
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav ms-auto mt-2 mt-lg-0">
            <li class="nav-item mx-4"><a class="nav-link active" href="<c:url value='/admin/index.do'/>">홈</a></li>
            <li class="nav-item"><a class="nav-link active" href="<c:url value='/logout.do'/>">로그아웃</a></li>
          </ul>
        </div>
      </div>
    </nav>


    <!-- Page content-->
    <div class="container-fluid">
      <h1 class="mt-4">Simple Sidebar</h1>
      <p>The starting state of the menu will appear collapsed on smaller screens, and will appear non-collapsed on larger screens. When toggled using the button below, the menu will change.</p>
      <p>
        Make sure to keep all page content within the
        <code>#page-content-wrapper</code>
        . The top navbar is optional, and just for demonstration. Just create an element with the
        <code>#sidebarToggle</code>
        ID which will toggle the menu when clicked.
      </p>
    </div>
  </div>

</div>
<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<!-- Bootstrap core JS-->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- Core theme JS-->
<script src="<c:url value='/js/scripts.js'/>"></script>
</body>
</html>
