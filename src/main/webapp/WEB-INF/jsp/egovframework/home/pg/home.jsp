<%--
  Created by IntelliJ IDEA.
  User: joo
  Date: 2025. 11. 8.
  Time: 15:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/jsp/egovframework/home/pg/common/header.jsp"/>

<!-- Page content-->
<div class="container-xl">
  <div class="row py-5">

    <!-- sidebar -->
<%--    <div class="col-lg-2">--%>
<%--      <jsp:include page="/WEB-INF/jsp/egovframework/home/pg/common/sidebar.jsp"/>--%>
<%--    </div>--%>

    <!-- Content entries-->
    <div class="col-lg-12 ">

      <!-- 검색 -->
      <div class="mb-4 d-flex justify-content-center">

        <form id="searchForm" class="d-flex align-items-center gap-3 w-50">
          <input type="hidden" id="movePage" name="movePage" value="<c:out value='${param.movePage}' default='1' />">
          <input type="hidden" name="orderType" value="<c:out value='${param.orderType}'/>">

            <select id="searchStatus" name="searchStatus" class="form-select w-auto">
              <option value="" <c:if test="${param.searchStatus eq ''}">selected</c:if>>전체</option>
            </select>

            <input id="searchQuery" name="searchQuery" type="text" class="form-control flex-grow-1"  placeholder="검색어를 입력하세요" value="<c:out value='${param.searchQuery}' />">

            <button id="searchBtn" type="button" class="btn btn-dark flex-shrink-0 px-4">검색</button>
        </form>

      </div>


      <!-- 상단 툴바: 좌측 메타, 우측 검색 -->
      <div class="d-flex justify-content-between align-items-center mb-3">
        <%-- 게시물 수 표시--%>
        <span class="count text-muted small">1건</span>
        <select id="orderType" name="orderType" class="form-select w-auto">
          <option value="" <c:if test="${param.orderType eq ''}">selected</c:if>>정렬기준</option>
          <option value="name" ${param.orderType == 'name' ? 'selected' : ''}>이름순</option>
          <option value="regDt" ${param.orderType == 'regDt' ? 'selected' : ''}>등록일순</option>
        </select>
      </div>


      <!-- room list -->
      <div id="dataList" class="dataList row text-center">

      </div>

      <!-- 페이지네이션 -->
      <div id="homePage" class="d-flex justify-content-center mt-5">
        <div class="pagination"></div>
      </div>

    </div>
  </div>


</div>

<script>
//페이지 로드가 완료되면
if (window.addEventListener) window.addEventListener('load', homeList, false);
else if (window.attachEvent) window.attachEvent('onload', homeList);
else window.onload = homeList;

function homeList() {
  dataList();

  event();
}

function dataList() {
  // 이전 데이터 제거
  $('.dataList').empty();

  ajaxForm('<c:url value="/getIndex.do"/>', $('#searchForm').serialize(), function(data) {
    if ($.trim(data.error) === 'N') {
      let dataHtml = '';
      let page = Number(data.dataMap.page);
      let pageCnt = Number(data.dataMap.pageCnt);
      let recordCnt = Number(data.dataMap.recordCnt ?? data.dataMap.list?.length ?? 0);
      let totalCnt = Number(data.dataMap.totalCnt);

      $.each(data.dataMap.list, function(idx, room) {
        let imgUrl = room.imageUrl ? room.imageUrl : '<c:url value="/assets/room/defaultRoomImage.jpg"/>';

        dataHtml += '<div class="col-12 col-md-6 col-lg-3 mb-4">';
        dataHtml += '<div class="card mb-4">';
        dataHtml += '<img class="card-img-top" src="' + imgUrl + '" alt="회의실 이미지"/>'
        dataHtml += '<div class="card-body">';
        dataHtml += '<div class="small text-muted">' + '가민 | 회의실' + '</div>';
        dataHtml += '<h2 class="card-title h4">' + room.name + '</h2>';
        dataHtml += '<p class="card-text">' + '수용인원: ' + room.capacity + '</p>';
        dataHtml += '<p class="card-text">' + room.description + '</p>';
        dataHtml += '<a class="reservationBtn btn btn-primary" href="/reservationList.do">예약하기 →</a>';
        dataHtml += '</div>'
        dataHtml += '</div>'
        dataHtml += '</div>'

      });

      // data 0 이면 "데이터가 없습니다"
      if (totalCnt === 0) {
        dataHtml += '<div class="col-12 text-center text-secondary py-5">' + '데이터가 없습니다.' + '</div>';
        // 현재 페이지 / 전체 페이지
        $('.count').html('총 <strong>' + numberWithCommas(totalCnt) + '건</strong> | 페이지 <strong>' + '0' + '/' + pageCnt + '</strong>');
      } else {
        $('.count').html('총 <strong>' + numberWithCommas(totalCnt) + '건</strong> | 페이지 <strong>' + page + '/' + pageCnt + '</strong>');
      }
      $('.dataList').append(dataHtml);
      // 페이징
      customPagination('homePage', data.dataMap.page, data.dataMap.pageCnt);

    }
  });

}

function event() {
  // 검색 버튼 클릭
  $('#searchBtn').on('click', function () {
    const selectedOrder = $('#orderType').val();
    $('#searchForm input[name="orderType"]').val(selectedOrder);
    $('#searchForm').find('input[name="movePage"]').val('1');
    dataList();
  });

  // 페이징 버튼 클릭
  $('.pagination').on('click', 'li a', function (e) {
    // # 붙는 기본동작 막기
    e.preventDefault();
    // #searchFormd 안의 movePage 값을 클릭된 페이지 번호로 바꿈
    $('#searchForm').find('input[name="movePage"]').val($(this).data('move'));
    dataList();
  });

  // 검색 입력: 한글/영어/숫자만 가능
  $('#searchQuery').on('input', function () {
    const filtered = $(this).val().replace(/[^가-힣ㄱ-ㅎㅏ-ㅣ0-9a-zA-Z\s]/g, '');
    $(this).val(filtered);
  })

  // 정렬 선택
  $('#orderType').on('change', function() {
    let selectedOrder = $(this).val();
    $('#searchForm input[name="orderType"]').val(selectedOrder);
    $('#searchForm').find('input[name="movePage"]').val('1');
    dataList();
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

</script>

<jsp:include page="/WEB-INF/jsp/egovframework/home/pg/common/footer.jsp"/>