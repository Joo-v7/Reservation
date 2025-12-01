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
<div class="container-xl">
  <div class="row py-5">

      <!-- sidebar -->
      <div class="col-lg-2 mt-5">
          <jsp:include page="/WEB-INF/jsp/egovframework/home/pg/common/sidebar.jsp">
              <jsp:param name="sideType" value="myPage"/>
              <jsp:param name="active" value="reservation"/>
          </jsp:include>
      </div>

    <!-- content entries-->
    <div class="col-lg-10">

        <!-- 상단 툴바: 좌측 메타, 우측 검색 -->
        <div class="d-flex justify-content-between align-items-center mb-3">

          <%-- 게시물 수 표시--%>
          <span class="count text-muted small"></span>

          <!-- 우측: 검색 폼 -->
          <form id="searchForm" class="d-flex align-items-center gap-2">
              <input type="hidden" id="movePage" name="movePage" value="<c:out value='${param.movePage}' default='1' />">

              <select id="searchStatus" name="searchStatus" class="form-select w-auto">
                  <option value="" <c:if test="${param.searchStatus eq ''}">selected</c:if>>예약 상태 선택</option>
                  <c:forEach var="status" items="${statusList}">
                      <option value="${status.code}" ${param.searchStatus == status.code ? 'selected' : ''}>${status.name}</option>
                  </c:forEach>
              </select>

              <input id="searchQuery" name="searchQuery" type="text" class="form-control"  placeholder="회의명을 입력하세요" value="<c:out value="${param.searchQuery}" />">

              <button id="searchBtn" type="button" class="btn btn-dark flex-shrink-0">검색</button>

          </form>
        </div>

      <!-- 목록 -->
      <div class="table-responsive">
        <table  id="dataList" class="dataList table table-hover align-middle mb-0">
          <thead class="table-group-divider">
          <tr>
              <th class="text-center">연번</th>
              <th class="text-center">상태</th>
              <th class="text-center">종류</th>
              <th class="text-center">시작일</th>
              <th class="text-center">종료일</th>
              <th class="text-center">시작시간</th>
              <th class="text-center">종료시간</th>
              <th class="text-center">회의실</th>
              <th class="text-center">회의명</th>
<%--              <th class="text-center">안건</th>--%>
              <th class="text-center">참석자 수</th>
<%--              <th class="text-center">첨부파일</th>--%>
<%--              <th class="text-center">작성자</th>--%>
              <th class="text-center">등록일</th>
              <th class="text-center">관리</th>
          </tr>
          </thead>
          <tbody>
          </tbody>
        </table>
      </div>

        <!-- 페이지네이션 -->
        <div id="reservationPage" class="d-flex justify-content-center mt-5">
            <div class="pagination"></div>
        </div>




    </div>
  </div>

</div>

<script>
    //페이지 로드가 완료되면
    if (window.addEventListener) window.addEventListener('load', reservationList, false);
    else if (window.attachEvent) window.attachEvent('onload', reservationList);
    else window.onload = reservationList;

    function reservationList() {
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
        ajaxForm('<c:url value="/myPage/getMyReservationList.do"/>', $('#searchForm').serialize(), function(data) {
            if ($.trim(data.error) === 'N') {
                let tableData = '';
                let trClass = '';
                let page = Number(data.dataMap.page);
                let pageCnt = Number(data.dataMap.pageCnt);
                let recordCnt = Number(data.dataMap.recordCnt ?? data.dataMap.list?.length ?? 0);
                let totalCnt = Number(data.dataMap.totalCnt);

                if (!recordCnt) recordCnt = 0;
                let startNo = totalCnt - ((page - 1) * recordCnt);

                // 예약 상태 맵
                let statusMap = {};
                (data.dataMap.statusList || []).forEach(function(rt){
                    statusMap[rt.code] = rt.name;
                });

                $.each(data.dataMap.list, function(idx, reservation) {
                    tableData += '<tr' + trClass + '>';
                    // 연번
                    tableData += '<td class="text-center">' + (startNo - idx) + '</td>';
                    // 예약상태
                    const statusName = statusMap[reservation.status] || '';
                    tableData += '<td class="text-center">' + statusName + '</td>';
                    // 종류
                    tableData += '<td class="text-center">' + (($.trim(reservation.type) === 'D') ? '일자' : '정기') + '</td>';
                    // 시작일
                    tableData += '<td class="text-center">' + formatDate(reservation.startDate) + '</td>';
                    // 종료일
                    const endBase = reservation.endDate ? reservation.endDate : reservation.startDate;
                    tableData += '<td class="text-center">' + (endBase ? formatDate(endBase) : '-') + '</td>';
                    // 시작시간
                    tableData += '<td class="text-center">' + (reservation.startAt ? reservation.startAt.substring(0, 5) : '') + '</td>';
                    // 종료시간
                    tableData += '<td class="text-center">' + (reservation.endAt ? reservation.endAt.substring(0, 5) : '') + '</td>';
                    // 회의실
                    tableData += '<td class="text-center">' + $.trim(reservation.roomName ?? '-') + '</td>';
                    // 회의명
                    tableData += '<td class="text-center">' + $.trim(reservation.name ?? '-') + '</td>';
                    // 안건
                    // tableData += '<td class="text-center">' + $.trim(reservation.agenda ?? '-') + '</td>';
                    // 참석자 수
                    tableData += '<td class="text-center">' + (reservation.attendeeCount ?? '-') + '</td>';
                    // 첨부파일
                    // tableData += '<td class="text-center">' + (reservation.attachment ?? '-') + '</td>';
                    // 작성자
                    // tableData += '<td class="text-center">' + $.trim(reservation.memberName ?? '-') + '</td>';
                    // 등록일
                    tableData += '<td class="text-center">' + formatDate(reservation.regDt) + '</td>';
                    // 관리
                    if ($.trim(reservation.status) === 'PENDING') {
                        tableData += '<td class="text-center">' +
                            '<button type="button" class="updateBtn btn btn-sm btn-primary" data-reservation-id="' + reservation.reservationId + '">수정</button>' +
                            '<button type="button" class="cancelBtn btn btn-sm btn-danger" data-reservation-id="' + reservation.reservationId + '">취소</button>' +
                            '</td>';
                    } else {
                        tableData += '<td class="text-center text-muted">-</td>';
                    }


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
                customPagination('reservationPage', data.dataMap.page, data.dataMap.pageCnt);
            }
            // 로딩 제거
            $('.dataList tbody').children('tr.loading').remove('');
        });
    }

    function event() {
        // 검색 버튼 클릭
        $('#searchBtn').on('click', function () {
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

        // 테이블 바디 포인터 커서
        $('#dataList tbody').on('mouseover', 'tr', function() {
            $(this).css('cursor', 'pointer');
        });

        // 수정 버튼
        $('#dataList').on('click', '.updateBtn', function() {
            const reservationId = $(this).attr('data-reservation-id');
            window.location.href = '<c:url value="/reservation.do?action=update"/>' + '&reservationId=' + reservationId;
        });

        // 취소 버튼
        $('#dataList').on('click', '.cancelBtn', function() {
            const reservationId = $(this).attr('data-reservation-id');
            const url = '<c:url value="/setReservationCancel.do"/>' + '?reservationId=' + reservationId;

            if (!confirm('취소하시겠습니까?')) return;

            ajaxForm(url, null, function(res) {
               if (res.error === 'N') {
                   dataList();
               }
            });
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

<%@ include file="/WEB-INF/jsp/egovframework/home/pg/common/footer.jsp"%>