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
  <title>OnRoom 관리자 로그인 제한 관리 페이지</title>
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
      <h1 class="mt-4">로그인 제한 관리</h1>

      <!-- 상단 툴바: 좌측 메타, 우측 검색 -->
      <div class="d-flex justify-content-between align-items-center mb-3">

        <%-- 리스트 수 표시--%>
        <span class="count text-muted small"></span>

        <!-- 우측: 검색 폼 -->
        <form id="searchForm" class="d-flex align-items-center gap-2">
          <input id="searchQuery" name="searchQuery" type="text" class="form-control"  placeholder="로그인 아이디를 입력하세요">
          <button id="searchBtn" type="button" class="btn btn-dark flex-shrink-0">검색</button>
        </form>
      </div>

      <!-- 목록 -->
      <!-- style: 테이블 내에서 스크롤 처리 -->
      <div class="table-responsive" style="max-height: 500px; overflow-y: auto;">
        <table  id="dataList" class="dataList table table-hover align-middle mb-0">
          <thead class="table-active">
          <tr>
            <th class="text-center">연번</th>
            <th class="text-center">아이디</th>
            <th class="text-center">로그인 제한 시각</th>
            <th class="text-center">남은 제한 시간</th>
            <th class="text-center">관리</th>
          </tr>
          </thead>
          <tbody>
          </tbody>
        </table>
      </div>

    </div>
  </div>

</div>
<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<!-- Bootstrap core JS-->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- Core theme JS-->
<script src="<c:url value='/js/scripts.js'/>"></script>

<script>
//페이지 로드가 완료되면
if (window.addEventListener) window.addEventListener('load', memberList, false);
else if (window.attachEvent) window.attachEvent('onload', memberList);
else window.onload = memberList;

function memberList() {
  dataList();

  event();
}

function dataList() {
  // 이전 목록 제거
  // $('.dataList tbody').children('tr').remove('');
  $('.dataList tbody').empty();
  // 로딩 바
  const constColLen = $('.dataList thead tr').children('th').length;

  $('.dataList tbody').append('<tr class="loading"><td colspan="' + constColLen + '" class="text-center"><i class="fad fa-spinner-third fa-spin fa-5x"></i></td></tr>');

  // form Submit [url, form, swal or toastr, validate, funcData]
  ajaxForm('<c:url value="/admin/getMemberLoginBlockList.do"/>', null, function(data) {
    if ($.trim(data.error) == 'N') {
      let totalCnt = data.dataList.length;

      let tableData = '';

      $.each(data.dataList, function(idx, item) {
        tableData += '<tr>';
        // 연번
        tableData += '<td class="text-center">' + (totalCnt - idx) + '</td>';
        // 아이디
        tableData += '<td class="text-center">' + item.username + '</td>';
        // 로그인 제한 시각
        tableData += '<td class="text-center">' + formDateTime(item.blockedTime) + '</td>';
        // 남은 제한 시간
        tableData += '<td class="text-center">' + item.expiredTime + '</td>';
        // 관리
        tableData += '<td class="text-center">' +
                '<button type="button" class="releaseBtn btn btn-danger" ' +
                'data-username="' + item.username + '">제한해제</button>' +
                '</td>';
        tableData += '</tr>';
      });

      // data 0 이면 "데이터가 없습니다"
      if (totalCnt === 0) {
        tableData += '<tr><td colspan="' + constColLen + '" class="no-data text-center text-secondary">데이터가 없습니다.</td></tr>';
        // 현재 데이터 개수
        $('.count').html('총 <strong>' + numberWithCommas(totalCnt) + '건</strong>');
      } else {
        $('.count').html('총 <strong>' + numberWithCommas(totalCnt) + '건</strong>');
      }
      $('.dataList tbody').append(tableData);

    }
    // 로딩 제거
    $('.dataList tbody').children('tr.loading').remove('');
  });
}

function event() {
  // 검색 버튼 클릭
  $('#searchBtn').on('click', function () {
    doSearch();
  });

  // 엔터 눌러도 검색되게
  $('#searchForm').on('submit', function (e) {
    e.preventDefault();
    doSearch();
  });

  // 검색 입력: 영어 소문자/숫자만 가능
  $('#searchQuery').on('input', function () {
    const filtered = $(this).val().replace(/[^0-9a-z\s]/g, '');
    $(this).val(filtered);
  });

  // 제한 해제 버튼 클릭
  $('.dataList').on('click', '.releaseBtn', function () {
    const username = $(this).data('username'); // .attr은 문자열로 읽는데, .data는 데이터 타입이 js내에서 유지됨
    ajaxForm('<c:url value="/admin/setMemberLoginUnblock.do"/>', {username: username}, function (res) {
      if ($.trim(res.error) === 'N') {
        alert(res.successMsg);
        dataList();
      }
    });
  });

}

// 검색: 레디스에서 전체 데이터 가져와서 뿌려줘서 검색 결과만 보이고 나머지는 숨기게함
function doSearch() {
  const query = $('#searchQuery').val().toLowerCase().trim();

  const $tbody = $('#dataList tbody');

  // 로딩행/노데이터행 제외한 실제 데이터 행만 대상으로
  const $dataRows = $tbody.find('tr').not('.loading').not('.no-data');

  // 검색어 없으면 전부 보여주고 no-data 지우기
  if (query === '') {
    $dataRows.show();
    $tbody.find('tr.no-data').remove();

    const totalCnt = $dataRows.length;
    $('.count').html('총 <strong>' + numberWithCommas(totalCnt) + '건</strong>');
    return;
  }

  // 검색어 있으면 필터링
  $dataRows.each(function () {
    const username = $(this).find('td:nth-child(2)').text().toLowerCase(); // 2번째 열이 아이디임
    const isMatch = username.includes(query);
    $(this).toggle(isMatch);
  });

  // 필터링 후 보이는 행 수
  const visibleCount = $dataRows.filter(':visible').length;

  // count는 보이는 데이터 행 기준
  $('.count').html('총 <strong>' + numberWithCommas(visibleCount) + '건</strong>');

  const constColLen = $('.dataList thead tr').children('th').length;

  // 결과 없으면 no-data 행 보여주기
  $tbody.find('tr.no-data').remove(); // 기존 no-data 제거
  if (visibleCount === 0) {
    $tbody.append(
            '<tr class="no-data">' +
            '<td colspan="' + constColLen + '" class="text-center text-secondary">' +
            '데이터가 없습니다.' +
            '</td>' +
            '</tr>'
    );
  }
}

// 날짜 + 시간(분:초)
function formDateTime(dataString) {
  const dateObj = new Date(dataString);

  const year = dateObj.getFullYear();
  const month = (dateObj.getMonth() + 1).toString().padStart(2, '0');
  const day = dateObj.getDate().toString().padStart(2, '0');
  const hours = String(dateObj.getHours()).padStart(2, "0");
  const minutes = String(dateObj.getMinutes()).padStart(2, "0");

  return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes;
}

function numberWithCommas(a) {
  return a.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")
}

</script>

</body>
</html>
