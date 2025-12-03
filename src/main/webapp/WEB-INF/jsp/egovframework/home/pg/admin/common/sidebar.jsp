<%--
  Created by IntelliJ IDEA.
  User: joo
  Date: 2025. 11. 8.
  Time: 16:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">


<%--<div class="border-end bg-dark" id="sidebar-wrapper">--%>
<%--  <div class="sidebar-heading border-bottom bg-dark text-white text-lg-center"><strong>OnRoom</strong></div>--%>
<%--  <div class="list-group list-group-flush">--%>
<%--    <a class="list-group-item list-group-item-action bg-dark text-white p-3 ${pageContext.request.requestURI.contains('/admin/index') ? 'active bg-primary text-white' : 'text-white bg-dark'}"--%>
<%--       href="<c:out value='/admin/index.do'/>">홈</a>--%>
<%--    <a class="list-group-item list-group-item-action bg-dark text-white p-3 ${pageContext.request.requestURI.contains('/admin/reservation') ? 'active bg-primary text-white' : 'text-white bg-dark'}"--%>
<%--       href="<c:out value='/admin/reservationList.do'/>">예약 관리</a>--%>
<%--    <a class="list-group-item list-group-item-action bg-dark text-white p-3 ${pageContext.request.requestURI.contains('/admin/member') ? 'active bg-primary text-white' : 'text-white bg-dark'}"--%>
<%--       href="<c:out value='/admin/memberList.do'/>">회원 관리</a>--%>
<%--    <a class="list-group-item list-group-item-action bg-dark text-white p-3 ${pageContext.request.requestURI.contains('/admin/room') ? 'active bg-primary text-white' : 'text-white bg-dark'}"--%>
<%--       href="<c:out value='/admin/roomList.do'/>">회의실 관리</a>--%>
<%--  </div>--%>
<%--</div>--%>

<div class="border-end bg-dark" id="sidebar-wrapper">
  <div class="sidebar-heading border-bottom bg-dark text-white text-lg-center">
    <strong>OnRoom</strong>
  </div>

  <div class="list-group list-group-flush">
    <!-- 홈 -->
    <a class="list-group-item list-group-item-action bg-dark text-white p-3
       ${pageContext.request.requestURI.contains('/admin/index') ? 'active bg-primary text-white' : 'text-white bg-dark'}"
       href="<c:out value='/admin/index.do'/>">홈</a>

    <!-- ================= 회원 관리 (접히는 헤더) ================= -->
    <a class="list-group-item list-group-item-action p-3 d-flex justify-content-between align-items-center
       bg-dark text-white
       ${pageContext.request.requestURI.contains('/admin/member') ? 'active bg-primary text-white' : ''}"
       data-bs-toggle="collapse"
       href="#memberSubMenu"
       role="button"
       aria-expanded="${pageContext.request.requestURI.contains('/admin/member') ? 'true' : 'false'}"
       aria-controls="memberSubMenu">
      <span>회원 관리</span>
      <!-- 화살표 아이콘 (Bootstrap Icons 쓰면 됩니다. 없으면 그냥 ▾/▴ 로) -->
      <span class="small">
  <i class="bi bi-chevron-down"></i>
</span>
    </a>

    <!-- 회원 관리 서브메뉴들 -->
    <div class="collapse ${pageContext.request.requestURI.contains('/admin/member') ? 'show' : ''}"
         id="memberSubMenu">
      <a class="list-group-item list-group-item-action bg-dark text-white ps-5"
         href="<c:out value='/admin/memberList.do'/>">
        회원 관리
      </a>

      <a class="list-group-item list-group-item-action bg-dark text-white ps-5"
         href="<c:out value='/admin/memberLoginBlockList.do'/>">
        로그인 제한 관리
      </a>
    </div>

    <!-- 예약 관리 -->
    <a class="list-group-item list-group-item-action bg-dark text-white p-3
       ${pageContext.request.requestURI.contains('/admin/reservation') ? 'active bg-primary text-white' : 'text-white bg-dark'}"
       href="<c:out value='/admin/reservationList.do'/>">예약 관리</a>

    <!-- 회의실 관리 -->
    <a class="list-group-item list-group-item-action bg-dark text-white p-3
       ${pageContext.request.requestURI.contains('/admin/room') ? 'active bg-primary text-white' : 'text-white bg-dark'}"
       href="<c:out value='/admin/roomList.do'/>">회의실 관리</a>
  </div>
</div>




