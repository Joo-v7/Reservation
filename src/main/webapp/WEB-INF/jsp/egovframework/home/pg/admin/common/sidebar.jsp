<%--
  Created by IntelliJ IDEA.
  User: joo
  Date: 2025. 11. 8.
  Time: 16:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="border-end bg-dark" id="sidebar-wrapper">
  <div class="sidebar-heading border-bottom bg-dark text-white text-lg-center"><strong>OnRoom</strong></div>
  <div class="list-group list-group-flush">
    <a class="list-group-item list-group-item-action bg-dark text-white p-3 ${pageContext.request.requestURI.contains('/admin/reservation') ? 'active bg-primary text-white' : 'text-white bg-dark'}"
       href="<c:out value='/admin/reservationList.do'/>">예약 관리</a>
    <a class="list-group-item list-group-item-action bg-dark text-white p-3 ${pageContext.request.requestURI.contains('/admin/member') ? 'active bg-primary text-white' : 'text-white bg-dark'}"
       href="<c:out value='/admin/memberList.do'/>">회원 관리</a>
    <a class="list-group-item list-group-item-action bg-dark text-white p-3 ${pageContext.request.requestURI.contains('/admin/room') ? 'active bg-primary text-white' : 'text-white bg-dark'}"
       href="<c:out value='/admin/roomList.do'/>">회의실 관리</a>
  </div>
</div>



