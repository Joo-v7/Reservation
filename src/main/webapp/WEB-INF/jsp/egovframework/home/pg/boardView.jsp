<%--
  Created by IntelliJ IDEA.
  User: joo
  Date: 2025. 11. 11.
  Time: 00:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="/WEB-INF/jsp/egovframework/home/pg/common/header.jsp" %>


<form id="searchForm" name="searchForm" method="post">
  <input type="hidden" id="movePage" name="movePage" value="<c:out value="${param.movePage}" />">
  <input type="hidden" id="searchBoardType" name="searchBoardType" value="<c:out value="${param.searchBoardType}" />">
  <input type="hidden" id="searchQuery" name="searchQuery" value="<c:out value="${param.searchQuery}" />">
</form>

<!-- Page content-->
<div class="container-xl">
  <div class="row py-5">

    <!-- sidebar -->
<%--    <div class="col-lg-2">--%>
<%--      <%@ include file="/WEB-INF/jsp/egovframework/home/pg/common/sidebar.jsp" %>--%>
<%--    </div>--%>

    <!-- Content entries-->
    <div class="col-lg-12">
      <div class="row justify-content-center mb-auto">
        <div class="col-12 col-md-7">

          <!-- 제목 -->
          <h3 class="fw-semibold mb-3"><c:out value="${board.title}"/></h3>
          <!-- 메타 라인 -->
          <div class="border-top border-bottom pt-3 pb-3 small text-muted d-flex gap-3">
            <div><strong class="text-dark me-3">작성자</strong> <span class="text-secondary"><c:out value="${board.name}"/></span></div>
            <div class="vr"></div>
            <div><strong class="text-dark me-3">작성일</strong> <span><fmt:formatDate value="${board.regDt}" pattern="yyyy-MM-dd HH:mm"/></span></div>
            <div class="vr"></div>
            <div><strong class="text-dark me-3">조회수</strong> <span><c:out value="${board.viewCount}"/></span></div>
          </div>

          <!-- 본문 -->
          <div class="border-bottom pb-4 pt-3" style="min-height: 30vh;">
            <div class="content">
              <c:out value="${board.content}" />
            </div>
          </div>

          <!-- 목록 버튼 -->
          <div class="text-center my-4">
            <button id="boardList" type="button" class="btn btn-secondary px-5">목록</button>
          </div>
        </div>


        </div>
      </div>

    </div>
  </div>
</div>
<script>
//페이지 로드가 완료되면
if (window.addEventListener) window.addEventListener('load', view, false);
else if (window.attachEvent) window.attachEvent('onload', view);
else window.onload = view;

// 이벤트 정의
function view() {

  // 목록
  $('#boardList').on('click', function(){
    console.log('목록 버튼 클릭');
    boardList();

  });

}

// 게시판 목록으로 이동
function boardList() {
  let $form = $('#searchForm');
  $form.attr('action', "/boardList.do");
  $form.submit();
}

</script>

<%@ include file="/WEB-INF/jsp/egovframework/home/pg/common/footer.jsp"%>