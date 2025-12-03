<%--
  Created by IntelliJ IDEA.
  User: joo
  Date: 2025. 11. 20.
  Time: 15:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/jsp/egovframework/home/pg/common/header.jsp"/>

<style>
  /* 날짜 숫자 기본 색 */
  .fc-daygrid-day-number {
    color: #333 !important;
  }

  /* FullCalendar 요일 헤더의 링크(a)에서 Bootstrap 링크 스타일 제거 */
  .fc .fc-col-header-cell-cushion {
    color: inherit !important;
    text-decoration: none !important;
  }

  /* --- 요일 헤더 색상 (일/토) --- */
  /* 일요일 */
  .fc .fc-col-header-cell.fc-day-sun .fc-col-header-cell-cushion {
    color: red !important;
  }

  /* 토요일 */
  .fc .fc-col-header-cell.fc-day-sat .fc-col-header-cell-cushion {
    color: #0022ff !important; /* 파란색으로 */
  }

  /* --- 날짜 숫자 색상 (토/일) --- */
  .fc-daygrid-day.fc-day-sat .fc-daygrid-day-number {
    color: #0022ff !important;
  }

  .fc-daygrid-day.fc-day-sun .fc-daygrid-day-number {
    color: red !important;
  }

  /* --- 오늘 날짜 강조 --- */
  .fc-day-today {
    background-color: #eaeaea !important;
    /*border: 2px solid rgb(236, 187, 0) !important;*/
  }

  /* --- fullcalendar a태그 색상뺌 --- */
  /*.fc-header-toolbar a,*/
  /*.fc-col-header-cell a,*/
  /*.fc-button-group a,*/
  /*.fc a {*/
  /*  color: unset !important;*/
  /*  text-decoration: none !important;*/
  /*}*/

  /* 이벤트 링크(파란색 제거) */
  .fc .fc-daygrid-event,
  .fc .fc-daygrid-event a,
  .fc .fc-daygrid-dot-event,
  .fc .fc-timegrid-event,
  .fc .fc-timegrid-event a {
    color: inherit !important;        /* 부모 색(대부분 검정) */
    text-decoration: none !important; /* 밑줄 제거 */
  }

  /* FullCalendar의 more 팝오버를 모달보다 뒤로 보내기 */
  .fc-popover {
    z-index: 1040 !important; /* bootstrap modal(1050) 보다 작게 */
  }

  .fc-daygrid-event-harness {
    cursor: pointer !important;
  }

  /* 이미지 크기 고정 */
  .ratio-wrapper {
    aspect-ratio: 16 / 9;     /* 카드의 고정 비율 */
    overflow: hidden;          /* 넘치는 부분 잘라내기 */
  }

  .ratio-wrapper img {
    width: 100%;
    height: 100%;
    object-fit: cover;          /* 비율 유지하면서 꽉 채움 */
    object-position: center;
  }


</style>

<!-- Page content-->
<div class="container-xl">
  <div class="row py-5">

    <!-- sidebar -->
<%--    <div class="col-lg-2">--%>
<%--      <jsp:include page="/WEB-INF/jsp/egovframework/home/pg/common/sidebar.jsp">--%>
<%--        <jsp:param name="sideType" value="reservation"/>--%>
<%--      </jsp:include>--%>
<%--    </div>--%>

<%--    <div class="col-lg-12">--%>
    <div class="col-12 col-lg-8 mb-4">

      <div id="calendar"></div>
      <div class="d-flex justify-content-end" style="margin-bottom:10px; font-size:15px;">
        <span style="color:black; font-size:18px;">●</span> 관리자 승인대기
        &nbsp;&nbsp; <!-- 줄바꿈 없는 공백 -->
        <span style="color:blue; font-size:18px;">●</span> 일자
        &nbsp;&nbsp;
        <span style="color:red; font-size:18px;">●</span> 정기
      </div>

    </div>

    <div class="col-12 col-lg-4 mt-4">

      <div class="mb-3 mt-3">
        <label for="roomSelect" class="text-muted small mb-1"></label>
        <select id="roomSelect" class="form-select">
          <option value="" <c:if test="${param.roomId eq ''}">selected</c:if>>전체</option>
          <c:forEach var="room" items="${roomList}">
            <option value="${room.roomId}" ${param.roomId == room.roomId ? 'selected' : ''}>${room.name}</option>
          </c:forEach>
        </select>
      </div>

      <div class="table-responsive">
        <table id="dataList" class="dataList table table-group-divider mb-3 shadow">
          <tbody>
          <tr>
            <td colspan="2" class="p-0 border-0">
              <img id="roomImage" src='<c:url value="/assets/room/defaultRoomImage.jpg"/>' class="img-fluid w-100" alt="회의실 이미지">
            </td>
          </tr>
          <tr>
            <th scope="row" class="text-dark text-nowrap">회의실명</th>
            <td id="nameRoom"></td>
          </tr>
          <tr>
            <th scope="row" class="text-dark text-nowrap">수용인원</th>
            <td><span id="capacity"></span> 명</td>
          </tr>
          <tr>
            <th scope="row" class="text-dark text-nowrap">설명</th>
            <td id="description"></td>
          </tr>
          <tr>
            <th scope="row" class="text-danger text-nowrap">문의사항</th>
            <td>rlawngur145@naver.com</td>
          </tr>
          </tbody>
        </table>
        <!-- ===================================== -->

        <!-- 버튼 영역 -->
        <div class="d-grid gap-2">
          <button id="reservationBtn" type="button" class="btn btn-primary flex-grow-1">예약하기</button>
<%--          <button type="button" class="btn btn-outline-secondary">목록</button>--%>
        </div>
      </div>


    </div>

      <!-- 예약 상세 모달 -->
      <div class="modal fade" id="reservationDetailModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">

            <div class="modal-header bg-dark text-white">
              <h5 class="modal-title">예약 상세</h5>
              <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>

            <div class="modal-body">

              <!-- 종류 -->
              <dl class="row mb-2">
                <dt class="col-sm-3">종류</dt>
                <dd class="col-sm-6" id="type"></dd>
              </dl>

              <!-- 일자 -->
              <dl class="row mb-2">
                <dt class="col-sm-3">일자</dt>
                <dd class="col-sm-6" id="date"></dd>
              </dl>

              <!-- 시간 -->
              <dl class="row mb-2">
                <dt class="col-sm-3">시간</dt>
                <dd class="col-sm-6" id="time"></dd>
              </dl>

              <!-- 회의실 -->
              <dl class="row mb-2">
                <dt class="col-sm-3">회의실</dt>
                <dd class="col-sm-6" id="roomName"></dd>
              </dl>

              <!-- 회의명 -->
              <dl class="row mb-2">
                <dt class="col-sm-3">회의명</dt>
                <dd class="col-sm-6" id="name"></dd>
              </dl>

              <!-- 안건 -->
              <dl class="row mb-2">
                <dt class="col-sm-3">안건</dt>
                <dd class="col-sm-6" id="agenda"></dd>
              </dl>

              <!-- 참석인원 -->
              <dl class="row mb-2">
                <dt class="col-sm-3">참석인원</dt>
                <dd class="col-sm-6" id="attendeeCount"></dd>
              </dl>

              <!-- 상태 -->
              <dl class="row mb-2">
                <dt class="col-sm-3">상태</dt>
                <dd class="col-sm-6" id="status"></dd>
              </dl>

              <!-- 첨부파일 영역 -->
              <dl class="row mb-2">
                <dt class="col-sm-3">첨부파일</dt>
                <dd class="col-sm-6" id="attachment"></dd>
              </dl>

            </div>

            <div class="modal-footer d-flex justify-content-center">
              <button class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
            </div>

          </div>
        </div>
      </div>


    </div>

  </div>

</div>
<script src="https://cdn.jsdelivr.net/npm/fullcalendar-scheduler@6.1.19/index.global.min.js"></script>

<!-- fullcalender docs = https://fullcalendar.io/docs -->
<script>
// fullcalender Started Bundle
document.addEventListener('DOMContentLoaded', function() {
  toggleReservationBtn();

  let calendarEl = document.getElementById('calendar');

  let calendar = new FullCalendar.Calendar(calendarEl, {
    // 무료 비상업용 key
    schedulerLicenseKey: 'CC-Attribution-NonCommercial-NoDerivatives',

    // 언어/시간
    locale: 'ko',
    // timeZone: 'local', // 사용자의 브라우저 시간대에 맞춤
    timeZone: 'Asia/Seoul',

    // 헤더 툴바
    headerToolbar: {
      left: 'prevYear,prev,next,nextYear today',
      center: 'title',
      right: 'dayGridMonth,timeGridWeek,timeGridDay' // timeGridWeek/timeGridDay는 유료
    },

    // 버튼
    buttonText: {
      today: '오늘',
      month: '월간',
      week: '주간',
      day: '일간',
      register: '예약 등록하기'
    },

    // customButtons: {
    //   reservation: {
    //     text: '회의실 예약하기',
    //     click: function() {
    //       window.location.href = '/reservation.do';
    //     }
    //   }
    // },

    // 선택 관련
    selectable: true,
    selectMirror: true,
    selectConstraint: {
      startTime: '06:00',
      endTime: '22:00'
    },

    // 업무 시간대 설정
    slotMinTime: '06:00:00',  // 8시부터
    slotMaxTime: '22:30:00',  // 19시까지만 보이게
    scrollTime: '06:00:00',   // 처음 열 때도 8시 위치로 스크롤
    allDaySlot: false,

    dayMaxEvents: true, // 달력에 최대 표시 가능한 일정 자동 설정
    nowIndicator: true, // 달력에 현재 시간을 표시

    // eventDisplay: 'block', // 이벤트 스타일
    eventDisplay: 'list-item', // 이벤트 스타일

    // 달력 클릭시 예약 등록 페이지로 이동
    // dateClick: function(info) {
    //   window.location.href = '/reservation.do';
    // },

    events: function(info, successCallback, failureCallback) {

      // 현재 뷰 기준으로 "해당 월"의 1일과 말일 구함
      let base = info.start; // 보이는 범위의 시작일
      let monthStart = new Date(base.getFullYear(), base.getMonth(), 1);
      let monthEnd   = new Date(base.getFullYear(), base.getMonth() + 1, 0); // 다음달 0일 = 이번달 말일

      let startDate = dateFormat(monthStart); // yyyy-MM-dd
      let endDate   = dateFormat(monthEnd);   // yyyy-MM-dd
      let selectedRoomId = $('#roomSelect').val();

      $.ajax({
        url: '/getReservationList.do',
        dataType: 'json',
        data: {
          start: startDate,
          end: endDate,
          roomId: selectedRoomId
        },
        success: function(res) {
          console.log('ajax success:', res);

          if ($.trim(res.error) === 'N') {
            // let list = res.dataMap || [];

            let events = [];

            $.each(res.dataMap, function(index, value) {
              // let eventColor = (value.status === 'PENDING') ? 'red' : 'blue';
              let eventColor = 'black';
              if ($.trim(value.status) !== 'PENDING') {
                eventColor = (value.type === 'R') ? 'red' : 'blue';
              }

              // 일자일때
              if ($.trim(value.type) === 'D') {

                let startDate = dateFormat(new Date(value.startDate));
                let startDateTime = startDate + 'T' + (value.startAt);
                let endDateTime = startDate + 'T' + (value.endAt);

                events.push({
                  id: $.trim(value.reservationId),
                  title: $.trim(value.name),
                  start: startDateTime,
                  end: endDateTime,
                  extendedProps: value, // 해당 데이터 보관
                  color: eventColor
                })
              } else { // 정기일때
                let startDate = dateFormat(new Date(value.startDate));
                let endDate = dateFormat(new Date(value.endDate));
                let daysOfWeekArr = [];
                if (value.daysOfWeek) {
                  daysOfWeekArr = value.daysOfWeek.split(',');
                }

                events.push({
                  id: $.trim(value.reservationId),
                  title: $.trim(value.name),
                  startRecur: startDate,
                  endRecur: endDate,
                  startTime: value.startAt,
                  endTime: value.endAt,
                  daysOfWeek: daysOfWeekArr,
                  extendedProps: value, // 해당 데이터 보관
                  color: eventColor
                })
              }
            });

            console.log('events', events);

            // 회의실 정보 테이블
            dataList(res.room);

            successCallback(events);
          } else {
            alert("예약 목록 조회 중 오류 발생 " + res.errorMsg);
          }
        },
        error: function(xhr, status, error) {
          console.error('예약 조회 오류:', status, error);
          failureCallback(new Error('예약 조회 오류'));
        }
      });
    },


    // 마우스 올렸을때
    eventDidMount: function(info) {
      console.log('extendedProps:', info.event.extendedProps);

      const event = info.event;
      const props = event.extendedProps;

      let startDate = dateFormat(new Date(props.startDate));
      let endDate = dateFormat(new Date(props.endDate));

      let status = ($.trim(props.status) === 'PENDING') ? '승인대기' : '승인완료';

      let dateHtml = '';
      if (props.endDate) {
        dateHtml = '<div><strong>' + '[' + startDate + ' ~ ' + endDate + ']'  + '</strong></div>';
      }

      let startAt = props.startAt ? props.startAt.substring(0, 5) : '';
      let endAt = props.endAt ? props.endAt.substring(0, 5) : '';

      const html =
              dateHtml +
              '<div>' + startAt + ' ~ ' + endAt + '</div>' +
              '<div>' + $.trim(props.roomName) + '</div>' +
              '<div>안건: ' + (props.agenda || '') + '</div>' +
              '<div>참석인원: ' + (props.attendeeCount || '') + '명</div>' +
              '<div>예약상태: ' + (status || '') + '</div>';

      new bootstrap.Popover(info.el, {
        title: event.title,
        content: html,
        html: true,
        trigger: 'hover',
        placement: 'right',
        container: 'body'
      });
    },

    eventClick: function (info) {
      showModalForm(info.event);
    },


  });

  calendar.render();

  // 회의실 셀렉트 변경
  $('#roomSelect').on('change', function() {
    toggleReservationBtn();
    // 캘린더 다시 로드
    calendar.refetchEvents();
  });

  // 예약하기 버튼
  $('#reservationBtn').on('click', function() {
    let selectedRoomId = $('#roomSelect').val();
    window.location.href = '<c:url value="/reservation.do?action=insert"/>' + '&roomId=' + selectedRoomId;
  });
});

// 예약하기 버튼 토글
function toggleReservationBtn() {
  let selectedRoomId = $('#roomSelect').val();
  if (selectedRoomId === "" || selectedRoomId === null) {
    $('#reservationBtn').hide();
  } else {
    $('#reservationBtn').show();
  }
}

// 회의실 정보
function dataList(data) {
  $('.dataList tbody').empty();

  let tableData = '';

  if (!data) {
    tableData += `<tr><td colspan="2" class="text-center text-muted small py-3">선택된 회의실이 없습니다.</td></tr>`;
  } else {
    const imagePath = '<c:url value="/attachment/image/"/>';
    let image = data.imageUrl ? imagePath + data.imageUrl : '<c:url value="/assets/room/defaultRoomImage.jpg"/>';
    // 이미지
    tableData += '<tr>';
    tableData += '<td colspan="2" class="p-0 border-0">';
    tableData += '<div class="ratio-wrapper">'
    tableData += '<img src="' + image + '" class="img-fluid w-100" alt="회의실 이미지">';
    tableData += '</div>'
    tableData += '</td>';
    tableData += '</tr>';
    // 회의실명
    tableData += '<tr>';
    tableData += '<th scope="row" class="text-dark text-nowrap">회의실명</th>';
    tableData += '<td>' + data.name + '</td>';
    tableData += '</tr>';
    // 수용인원
    tableData += '<tr>';
    tableData += '<th scope="row" class="text-dark text-nowrap">수용인원</th>';
    tableData += '<td><span>' + data.capacity + '</span> 명</td>';
    tableData += '</tr>';
    // 설명
    tableData += '<tr>';
    tableData += '<th scope="row" class="text-dark text-nowrap">설명</th>';
    tableData += '<td>' + (data.description || '회의실 설명이 없습니다.') + '</td>';
    tableData += '</tr>';
    // 문의사항 (하드코딩)
    tableData += '<tr>';
    tableData += '<th scope="row" class="text-danger text-nowrap">문의사항</th>';
    tableData += '<td>rlawngur145@naver.com</td>';
    tableData += '</tr>';
  }

  $('.dataList tbody').append(tableData);

}

function dateFormat(date) {
  let formattedDate = date.getFullYear() +
          '-' + ( (date.getMonth()+1) < 9 ? "0" + (date.getMonth()+1) : (date.getMonth()+1) ) +
          '-' + ( (date.getDate()) < 9 ? "0" + (date.getDate()) : (date.getDate()) );
  return formattedDate;
}

// 모달창
function showModalForm(event) {
  const p = event.extendedProps;

  // 종류
  $('#type').text(p.type === 'D' ? '일자' : '정기');

  // 일자
  let date = '';
  if (p.type === 'D') {
    date = dateFormat(new Date(p.startDate));
  } else {
    date = dateFormat(new Date(p.startDate)) + ' ~ ' + dateFormat(new Date(p.endDate));
  }
  $('#date').text(date);

  // 시간
  let startAt = p.startAt ? p.startAt.substring(0, 5) : '';
  let endAt = p.endAt ? p.endAt.substring(0, 5) : '';
  let time = startAt + ' ~ ' + endAt;
  $('#time').text(time);

  // 회의실
  $('#roomName').text(p.roomName);

  // 회의명
  $('#name').text(p.name);

  // 안건
  $('#agenda').text(p.agenda);

  // 참석인원
  $('#attendeeCount').text(p.attendeeCount + ' 명');

  // 상태
  $('#status').text(p.status === 'PENDING' ? '승인대기' : '승인완료');

  // 첨부파일 (없으면 '-')
  if (p.attachment) {
    const displayName = p.attachment.split("_")[1] || p.attachment;
    const url = '${pageContext.request.contextPath}' + '/reservation/attachment.do?file=' + p.attachment;

    $('#attachment').html('<a href="' + url + '">' + displayName + '</a>');
  } else {
    $('#attachment').text('-');
  }

  // 모달 열기
  var modalEl = document.getElementById('reservationDetailModal');
  var modal = new bootstrap.Modal(modalEl);
  modal.show();
}

</script>

<jsp:include page="/WEB-INF/jsp/egovframework/home/pg/common/footer.jsp"/>