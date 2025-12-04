<%--
  Created by IntelliJ IDEA.
  User: joo
  Date: 2025. 11. 8.
  Time: 16:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- sidebar 종류 -->
<c:set var="sideType" value="${param.sideType}"/>

<!-- 마이페이지 사이드바 현재 메뉴 -->
<c:set var="active" value="${param.active}"/>

<!-- 메뉴 : 게시판 -->
<c:if test="${sideType eq 'board'}">
  <div class="card mb-3">
    <div class="card-header fw-bold text-center bg-dark text-white">
      게시판
    </div>
    <div class="list-group list-group-flush">
      <a href="/boardList.do" class="list-group-item list-group-item-action">
        게시글 목록
      </a>
      <a href="/board.do" class="list-group-item list-group-item-action">
        게시글 작성
      </a>
    </div>
  </div>
</c:if>


<!-- 메뉴 : 예약 -->
<c:if test="${sideType eq 'reservation'}">
  <div class="card mb-3">
    <div class="card-header fw-bold text-center bg-dark text-white">
      예약
    </div>
    <div class="list-group list-group-flush">
      <a href="/reservationList.do" class="list-group-item list-group-item-action">
        예약 조회
      </a>
      <a href="/reservation.do" class="list-group-item list-group-item-action">
        예약하기
      </a>
    </div>
  </div>
</c:if>

<!-- 메뉴 : 마이페이지 -->
<c:if test="${sideType eq 'myPage'}">
  <div class="card mb-3">
    <div class="card-header fw-bold text-center bg-dark text-white">
      마이페이지
    </div>

    <div id="mypageAccordion" class="accordion accordion-flush">

      <!-- 계정 -->
      <div class="accordion-item">
        <h2 class="accordion-header" id="headingAccount">
          <button class="accordion-button fw-bold ${active eq 'account' ? '' : 'collapsed'}"
                  type="button"
                  data-bs-toggle="collapse"
                  data-bs-target="#collapseAccount">
            계정
          </button>
        </h2>

        <div id="collapseAccount"
             class="accordion-collapse collapse ${active eq 'account' ? 'show' : ''}"
             aria-labelledby="headingAccount"
             data-bs-parent="#mypageAccordion">

          <div class="accordion-body p-0">
            <div class="list-group list-group-flush">

              <a href="<c:url value='/myPage/myInfoList.do'/>"
                 class="list-group-item list-group-item-action">
                내 정보
              </a>

              <a href="<c:url value='/myPage/passwordUpdate.do'/>"
                 class="list-group-item list-group-item-action">
                비밀번호 변경
              </a>

              <a href="#"
                 class="list-group-item list-group-item-action">
                회원탈퇴
              </a>

            </div>
          </div>
        </div>
      </div>


      <!-- 예약 -->
      <div class="accordion-item">
        <h2 class="accordion-header" id="headingReservation">
          <button class="accordion-button fw-bold ${active eq 'reservation' ? '' : 'collapsed'}"
                  type="button"
                  data-bs-toggle="collapse"
                  data-bs-target="#collapseReservation">
            예약
          </button>
        </h2>

        <div id="collapseReservation"
             class="accordion-collapse collapse ${active eq 'reservation' ? 'show' : ''}"
             aria-labelledby="headingReservation"
             data-bs-parent="#mypageAccordion">

          <div class="accordion-body p-0">
            <div class="list-group list-group-flush">

              <a href="<c:url value="/myPage/myReservationList.do"/>"
                 class="list-group-item list-group-item-action">
                내 예약
              </a>

            </div>
          </div>
        </div>
      </div>


      <!-- 게시판 -->
      <div class="accordion-item">
        <h2 class="accordion-header" id="headingBoard">
          <button class="accordion-button fw-bold ${active eq 'board' ? '' : 'collapsed'}"
                  type="button"
                  data-bs-toggle="collapse"
                  data-bs-target="#collapseBoard">
            게시글
          </button>
        </h2>

        <div id="collapseBoard"
             class="accordion-collapse collapse ${active eq 'board' ? 'show' : ''}"
             aria-labelledby="headingBoard"
             data-bs-parent="#mypageAccordion">

          <div class="accordion-body p-0">
            <div class="list-group list-group-flush">

              <a href="<c:url value="/myPage/myBoardList.do"/> "
                 class="list-group-item list-group-item-action">
                내 게시글
              </a>

            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</c:if>



