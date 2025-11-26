<%--
  Created by IntelliJ IDEA.
  User: joo
  Date: 2025. 11. 8.
  Time: 15:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ include file="/WEB-INF/jsp/egovframework/home/pg/common/header.jsp" %>

<!-- Page content-->
<div class="container">
  <div class="row py-5">

    <!-- sidebar -->
<%--      <div class="col-lg-2">--%>
<%--          <jsp:include page="/WEB-INF/jsp/egovframework/home/pg/common/sidebar.jsp">--%>
<%--              <jsp:param name="sideType" value="board"/>--%>
<%--          </jsp:include>--%>
<%--      </div>--%>

    <!-- content entries-->
    <div class="col-lg-12">

        <!-- 상단 툴바: 좌측 메타, 우측 검색 -->
        <div class="d-flex justify-content-between align-items-center mb-3">

          <%-- 게시물 수 표시--%>
          <span class="count text-muted small"></span>

            <!-- 우측: 검색 폼 -->
          <form id="searchForm" class="d-flex align-items-center gap-2">
            <input type="hidden" id="movePage" name="movePage" value="<c:out value='${param.movePage}' default='1' />">

            <select id="searchBoardType" name="searchBoardType" class="form-select w-auto">
              <option value="" <c:if test="${param.searchBoardType eq ''}">selected</c:if>>게시글 분류 선택</option>
              <c:forEach var="searchBoardType" items="${boardTypeList}">
                <option value="${searchBoardType.boardTypeId}" ${param.searchBoardType == searchBoardType.boardTypeId ? 'selected' : ''}>${searchBoardType.name}</option>
              </c:forEach>
            </select>

            <input id="searchQuery" name="searchQuery" type="text" class="form-control"  placeholder="검색어를 입력하세요" value="<c:out value="${param.searchQuery}" />">

            <button id="searchBtn" type="button" class="btn btn-dark flex-shrink-0">검색</button>

          </form>
        </div>

      <!-- 목록 -->
      <div class="table-responsive">
        <table  id="dataList" class="dataList table table-hover align-middle mb-0">
          <thead class="table-light">
          <tr>
            <th class="text-center">연번</th>
            <th class="text-center">분류</th>
            <th class="text-center">제목</th>
            <th class="text-center">작성자</th>
            <th class="text-center">작성일</th>
            <th class="text-center">조회</th>
          </tr>
          </thead>
          <tbody>
          </tbody>
        </table>
      </div>

          <!-- 페이지네이션 -->
          <div id="boardPage" class="d-flex justify-content-center mt-5">
              <div class="pagination"></div>
          </div>

          <!-- 우측 하단 버튼(선택) -->
          <div class="d-flex justify-content-end">
              <button id="mergeBtn" type="button" class="btn btn-primary mb-5" >글쓰기</button>
          </div>

    </div>
  </div>

</div>

<script>
//페이지 로드가 완료되면
if (window.addEventListener) window.addEventListener('load', boardList, false);
else if (window.attachEvent) window.attachEvent('onload', boardList);
else window.onload = boardList;

// 이벤트 정의
function boardList() {

  // 리스트 데이터
  dataList();

  // 이벤트 등록
  event();

}

function dataList() {
  // 이전 목록 제거
  $('.dataList tbody').children('tr').remove('');
  // 로딩 바
  const constColLen = $('.dataList thead tr').children('th').length;

  $('.dataList tbody').append('<tr class="loading"><td colspan="' + constColLen + '" class="text-center"><i class="fad fa-spinner-third fa-spin fa-5x"></i></td></tr>');
  // form Submit [url, form, swal or toastr, validate, funcData]
  ajaxForm('<c:url value="/getBoardList.do"/>', $('#searchForm').serialize(), function(data) {
    if ($.trim(data.error) == 'N') {
      var tableData = '';
      var trClass = '';
      var page = Number(data.dataMap.page);
      var pageCnt = Number(data.dataMap.pageCnt);
      var recordCnt = Number(data.dataMap.recordCnt ?? data.dataMap.list?.length ?? 0);
      var totalCnt = Number(data.dataMap.totalCnt);

      if (!recordCnt) recordCnt = 0;
      var startNo = totalCnt - ((page - 1) * recordCnt);

      // 게시판 분류 맵
      var boardTypeMap = {};
      (data.dataMap.boardTypeList || []).forEach(function(bt){
        boardTypeMap[bt.boardTypeId] = bt.name;
      });

      $.each(data.dataMap.list, function(key, values) {
        tableData += '<tr' + trClass + '>';
        // 연번
        tableData += '<td class="text-center">' + (startNo - key) + '</td>';
        // 게시판 분류
        tableData += '<td class="text-center">' + $.trim(boardTypeMap[values.boardTypeId] ?? '') + '</td>';
        // 제목
        tableData += '<td class="text-center" style="text-align:left;">'
            + '<a href="#" class="boardView" data-id="' + $.trim(values.boardId) + '">'
            + $.trim(values.title ?? '') + '</a></td>';
        // 작성자
        tableData += '<td class="text-center">' + $.trim(values.name ?? '-') + '</td>';
        // 작성일
        tableData += '<td class="text-center">' + formatDate(values.regDt) + '</td>';
        // 조회수
        tableData += '<td class="text-center">' + $.trim(values.viewCount ?? '0') + '</td>';

        tableData += '</tr>';
      });

      // data 0 이면 "데이터가 없습니다"
      if (totalCnt === 0) {
        tableData += '<tr><td colspan="' + constColLen + '" class="no-data text-center text-secondary">데이터가 없습니다.</td></tr>';
        // 현재 페이지 / 전체 페이지
        $('.count').html('총 <strong>' + numberWithCommas(totalCnt) + '건</strong> | 페이지 <strong>' + '0' + '/' + pageCnt + '</strong>');
      } else {
        $('.count').html('총 <strong>' + numberWithCommas(totalCnt) + '건</strong> | 페이지 <strong>' + page + '/' + pageCnt + '</strong>');
      }
      $('.dataList tbody').append(tableData);
      // 페이징
      customPagination('boardPage', data.dataMap.page, data.dataMap.pageCnt);
    }
    // 로딩 제거
    $('.dataList tbody').children('tr.loading').remove('');
  });
}


// 이벤트
function event() {

  // 검색 버튼 클릭
  $('#searchBtn').on('click', function() {
    $('#searchForm').find('input[name="movePage"]').val('1');
    dataList();
  });

  // 페이징 버튼 클릭
  $('.pagination').on('click', 'li a', function(e) {
    // # 붙는 기본동작 막기
    e.preventDefault();
    // #searchFormd 안의 movePage 값을 클릭된 페이지 번호로 바꿈
    $('#searchForm').find('input[name="movePage"]').val($(this).data('move'));
    dataList();
  });

  // 글쓰기 버튼 클릭
  $('#mergeBtn').on('click', function() {
      $('#searchForm')
          .attr('method', 'post')
          .attr('action', '/board.do?action=insert')
          .submit();
  });

  // 제목 클릭
  $('.dataList').on('click', 'a.boardView', function (e) {
      e.preventDefault();

      let id = $(this).data('id');
      let $form = $('#searchForm');

      $form.attr('method', 'post')
          .attr('action', '/boardView.do')
          .find('input[name="boardId"]').remove();
      $('<input>', { type: 'hidden', name: 'boardId', value: id }).appendTo($form);

      $form.submit();

  });

  // 검색 입력: 한글, 숫자, 영어, 공백만 가능
  $('#searchQuery').on('input', function () {
    const filtered = $(this).val().replace(/[^가-힣ㄱ-ㅎㅏ-ㅣ0-9a-zA-Z\s]/g, '');
    $(this).val(filtered);
  });

}

// 날짜 형식 변환
function formatDate(dataString) {
  const dateObj = new Date(dataString);
  const year = dateObj.getFullYear();
  const month = (dateObj.getMonth() + 1).toString().padStart(2, '0');
  const day = dateObj.getDate().toString().padStart(2, '0');

  return year + '-' + month + '-' + day;
}

function numberWithCommas(a) {
    return a.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")
}


// id, curPage, totalPage, 시작 페이지 번호(안념겨도됨)
function customPagination(a, b, c, d) {
  const group = 10;                   // 한 번에 보여줄 페이지 수
  d = group * (Math.ceil(b / group) - 1) + 1; // 시작 페이지
  let f = d + group - 1;              // 끝 페이지
  if (f >= c) f = c;

  $("#" + a + " .pagination").html(""); // 비우기
  let e = "";

  // << 이전 그룹
  if (b > group) {
    e += '<li class="page-item"><a href="#" class="page-link" aria-label="Previous" data-tp="' + a + '" data-move="' + (d - group) + '" title="first">'
        + '<span aria-hidden="true">&laquo;</span></a></li>';
  }
  // < 이전
  if (b > 1) {
    e += '<li class="page-item"><a href="#" class="page-link" data-tp="' + a + '" data-move="' + (b - 1) + '" title="prev">'
            + '<span aria-hidden="true">&lsaquo;</span></a></li>';
  }

  // 숫자 페이지들
  for (var g = d; g <= f; g++) {
    if (b !== g) {
      e += '<li class="page-item"><a href="#" class="page-link" data-tp="' + a + '" data-move="' + g + '">' + g + '</a></li>';
    } else {
      e += '<li class="page-item"><a href="#" class="page-link active" data-tp="' + a + '" data-move="' + g + '" class="active" title="현재 ' + g + '페이지">'
              + g + '</a></li>';
    }
  }

  // > 다음
  if (b < c) {
    e += '<li class="page-item"><a href="#" class="page-link" data-tp="' + a + '" data-move="' + (b + 1) + '" title="next">'
            + '<span aria-hidden="true">&rsaquo;</span></a></li>';
  }
  // >> 다음 그룹
  if (Math.ceil(b / group) < Math.ceil(c / group)) {
    e += '<li class="page-item"><a href="#" class="page-link" data-tp="' + a + '" data-move="' + (d + group) + '" title="last">'
            + '<span aria-hidden="true">&raquo;</span></a></li>';
  }

  if ($.trim(e) !== "") {
    $("#" + a + " .pagination").html("<ul class='pagination'>" + e + "</ul>");} // bootstrap5: ul-class pagination 적용
}

</script>

<%@ include file="/WEB-INF/jsp/egovframework/home/pg/common/footer.jsp"%>