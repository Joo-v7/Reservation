<%--
  Created by IntelliJ IDEA.
  User: joo
  Date: 2025. 11. 8.
  Time: 15:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="/WEB-INF/jsp/egovframework/home/pg/common/header.jsp" %>

<%--<style>--%>
<%--  /* 이 페이지에서만 dd 하단 마진 제거 */--%>
<%--  dd {--%>
<%--    margin-bottom: 0 !important;--%>
<%--  }--%>
<%--</style>--%>

<!-- Page content-->
<div class="container-xl">
  <div class="row py-5">

    <!-- sidebar -->
    <div class="col-lg-2">
<%--      <jsp:include page="/WEB-INF/jsp/egovframework/home/pg/common/sidebar.jsp">--%>
<%--        <jsp:param name="sideType" value="reservation"/>--%>
<%--      </jsp:include>--%>
    </div>

    <!-- Content entries-->
    <div class="col-lg-10">
      <div class="row justify-content-center mb-auto">
        <div class="col-12 col-md-10 col-lg-10">

          <form id="reservationForm" method="post">
            <p class="small"><span class="text-danger mb-3">*</span> 는 필수 입력 사항입니다.</p>

            <dl class="row mb-3">
              <dt class="col-sm-2 col-form-label">회의실 <span class="text-danger">*</span></dt>
              <dd class="col-sm-3">
                  <select id="room" name="roomId" class="form-select" required>
                    <option value="" selected>선택</option>
                    <c:forEach var="room" items="${roomList}">
                      <option value="${room.roomId}" data-capacity="${room.capacity}">${room.name}</option>
                    </c:forEach>
                  </select>
              </dd>

              <dd class="col-sm-5 d-flex align-items-center gap-3">

                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="radio" name="type" id="dateTypeOnce" value="D" checked>
                  <label class="form-check-label" for="dateTypeOnce">일자</label>
                </div>

                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="radio" name="type" id="dateTypeRegular" value="R">
                  <label class="form-check-label" for="dateTypeRegular">정기</label>
                </div>

              </dd>

            </dl>

            <dl class="row mb-3">
              <dt class="col-sm-2 col-form-label">회의명 <span class="text-danger">*</span></dt>
              <dd class="col-sm-6">
                <input type="text" class="form-control" id="name" name="name" maxlength="20" placeholder="회의명을 입력하세요." required >
              </dd>
            </dl>

            <dl class="row mb-3">
              <dt class="col-sm-2 col-form-label">안건 <span class="text-danger">*</span></dt>
              <dd class="col-sm-6">
                <input type="text" class="form-control" id="agenda" name="agenda" maxlength="50" placeholder="안건을 입력하세요." required>
              </dd>
            </dl>

            <dl class="row mb-3">
              <dt class="col-sm-2 col-form-label">참석인원 <span class="text-danger">*</span></dt>
              <dd class="col-sm-2">
                <select id="attendeeCount" name="attendeeCount" class="form-select" required>
                  <option value="" selected>선택</option>
<%--                  <c:forEach var="i" begin="1" end="100">--%>
<%--                    <option value="${i}">${i}</option>--%>
<%--                  </c:forEach>--%>
                </select>
              </dd>
            </dl>

            <dl class="row mb-3">
              <dt class="col-sm-2 col-form-label">회의일자 <span class="text-danger">*</span></dt>
              <dd class="col-sm-3">
                <input type="date" class="date form-control" id="startDate" name="startDate" required>
              </dd>
              ~
              <dd class="col-sm-3">
                <input type="date" class="date form-control" id="endDate" name="endDate">
              </dd>
            </dl>

            <dl id="weekdaySection" class="row mb-3">
              <dt class="col-sm-2 col-form-label">요일 선택 <span class="text-danger">*</span></dt>
              <dd class="col-sm-10 d-flex gap-3 pt-2">

                <div class="form-check form-check-inline">
                  <input id="mon" class="form-check-input" type="radio" name="daysOfWeek" value="1" >
                  <label class="form-check-label" for="mon">월</label>
                </div>

                <div class="form-check form-check-inline">
                  <input id="tue" class="form-check-input" type="radio" name="daysOfWeek" value="2" >
                  <label class="form-check-label" for="tue">화</label>
                </div>

                <div class="form-check form-check-inline">
                  <input id="wed" class="form-check-input" type="radio" name="daysOfWeek" value="3" >
                  <label class="form-check-label" for="wed">수</label>
                </div>

                <div class="form-check form-check-inline">
                  <input id="thu" class="form-check-input" type="radio" name="daysOfWeek" value="4" >
                  <label class="form-check-label" for="thu">목</label>
                </div>

                <div class="form-check form-check-inline">
                  <input id="fri" class="form-check-input" type="radio" name="daysOfWeek" value="5" >
                  <label class="form-check-label" for="fri">금</label>
                </div>

                <div class="form-check form-check-inline">
                  <input id="sat" class="form-check-input" type="radio" name="daysOfWeek" value="6" >
                  <label class="form-check-label" for="sat">토</label>
                </div>

              </dd>
            </dl>

            <dl class="row mb-3">
              <dt class="col-sm-2 col-form-label">회의시간 <span class="text-danger">*</span></dt>
              <dd class="col-sm-3">
                <!-- min:06 / max:22 input type이 time이면 선택은 가능하지만 제출시 유효성 검사에서 걸러줌 -->
                <input type="time" class="form-control" id="startAt" name="startAt" min="06:00" max="22:00" required>
              </dd>
              ~
              <dd class="col-sm-3">
                <!-- min:06 / max:22 input type이 time이면 선택은 가능하지만 제출시 유효성 검사에서 걸러줌 -->
                <input type="time" class="form-control" id="endAt" name="endAt" min="06:00" max="22:00" required>
              </dd>
            </dl>

            <dl class="row mb-3">
              <dt class="col-sm-2 col-form-label">첨부파일</dt>
              <dd class="col-sm-6">
                <input class="form-control" type="file" id="attachment" name="attachment">
              </dd>
            </dl>

            <div class="d-flex justify-content-end gap-2 mt-3 mb-5">
              <input type="submit" class="btn btn-primary" value="저장">
              <button id="reservationList" type="button" class="btn btn-dark">취소</button>
            </div>

          </form>

        </div>
      </div>

    </div>
  </div>
</div>
<script>
//페이지 로드가 완료되면
if (window.addEventListener) window.addEventListener('load', reservation, false);
else if (window.attachEvent) window.attachEvent('onload', reservation);
else window.onload = reservation;

// 이벤트 정의
function reservation() {

  // 취소
  $('#reservationList').on('click', function () {
    console.log('취소 버튼 클릭');
    reservationList();

  });

  // 저장
  $('#reservationForm').on('submit', function (e) {
    console.log('저장 버튼 클릭')
    e.preventDefault();
    submitReservationForm();
  });

  // 이벤트 등록
  bindEvents();

}

// 예약 목록으로 이동
function reservationList() {
  if (window.history.length > 1) {
    history.back(); // 바로 이전 페이지로 (캘린더 상태 그대로)
  } else {
    window.location.href = "/reservationList.do";
  }
}


// 입력 폼 제출
function submitReservationForm() {
  var $form = $('#reservationForm');

  var formErr = false;
  var moveFocus = '';
  let errMsg = '';

  $form.attr('method', 'post');
  $form.attr('action', '/setReservationMerge.do');

  // ---제출 유효성 체크 시작---
  // 회의명
  if(!formErr && !isValidName($('#name').val())) {
    formErr = true;
    moveFocus = 'name';
    errMsg = '회의명은 한글, 영어, 숫자만 입력 가능합니다.';
  }

  // 안건
  if(!formErr && !isValidAgenda($('#agenda').val())) {
    formErr = true;
    moveFocus = 'agenda';
    errMsg = '안건은 한글, 영어, 숫자만 입력 가능합니다.';
  }

  // 회의시간
  if(!formErr) {
    const startAt = $('#startAt').val();
    const endAt = $('#endAt').val();

    if (!startAt || !endAt) {
      formErr = true;
      moveFocus = !startAt ? 'startAt' : 'endAt';
      errMsg = '회의 시작 시간과 종료 시간을 모두 입력해주세요.';
    } else if (startAt < '06:00' || startAt > '22:00' || endAt < '06:00' || endAt > '22:00') {
      formErr = true;
      moveFocus = 'startAt';
      errMsg = '회의 시간은 06:00부터 22:00 사이로만 설정 가능합니다.';
    } else if (endAt <= startAt) {
      formErr = true;
      moveFocus = 'endAt';
      errMsg = '회의 종료 시간은 회의 시작 시간보다 늦어야 합니다.';
    }
  }

  // 참석인원
  if (!formErr && $('#attendeeCount').val() === "") {
    formErr = true;
    moveFocus = 'attendeeCount';
    errMsg = '참석인원을 선택해주세요.';
  }

  let today = new Date().toISOString().split("T")[0];
  let start = $('#startDate').val();
  let end = $('#endDate').val();
  let type = $('input[name="type"]:checked').val();

  // 회의일자 체크
  if (!formErr && start < today) {
    formErr = true;
    moveFocus = 'startDate';
    errMsg = '오늘 이전 날짜는 선택할 수 없습니다.';
  }

  // 정기 예약일 때만 종료일 체크
  if (!formErr && type === 'R') {
    if (!end) {
      formErr = true;
      moveFocus = 'endDate';
      errMsg = '정기 예약의 종료일을 입력해주세요.';
    } else if (end < start) {
      formErr = true;
      moveFocus = 'endDate';
      errMsg = '종료일은 시작일보다 이전일 수 없습니다.';
    }
  }

  // 정기 예약일 때 요일 선택 체크
  if (!formErr && type === 'R') {
    if (!$('input[name="type"]:checked').val()) {
      formErr = true;
      moveFocus = 'mon';
      errMsg = '정기 예약은 요일을 하나 선택해야 합니다.';
    }
  }


  // 에러가 하나라도 잡혔으면 여기서 한 번만 alert 후 종료 (각각 alert 띄우니 사용자 편의성 매우 떨어짐)
  if (formErr) {
    alert(errMsg);
    if (moveFocus) {
      $('#' + moveFocus).focus();
    }
    return;
  }

  // action 세팅
  var act = ('${param.action}' === 'update') ? 'update' : 'insert';
  if ($form.find('input[name="action"]').length === 0) {
    $('<input>', { type: 'hidden', name: 'action', value: act }).appendTo($form);
  } else {
    $form.find('input[name="action"]').val(act);
  }

  // 수정/등록 결과
  // ajaxForm(submit url, data form, result)
  ajaxForm('<c:url value="/setReservationMerge.do"/>', $form.serialize(), function (res) {
    // 응답 성공 시
    if (res.error === 'N') {
      alert(res.successMsg);

      if (window.history.length > 1) {
        history.back(); // 바로 이전 페이지로 (캘린더 상태 그대로)
      } else {
        window.location.href = "/reservationList.do";
      }
    }

  });

}

function bindEvents() {
  // 회의명 (한글, 숫자, 영어, 공백 허용)
  $('#name').on('input', function() {
    const filtered = $(this).val().replace(/[^가-힣ㄱ-ㅎㅏ-ㅣ0-9a-zA-Z\s]/g, '');
    $(this).val(filtered);
  });

  // 안건 (한글, 숫자, 영어, 공백 허용)
  $('#agenda').on('input', function() {
    const filtered = $(this).val().replace(/[^가-힣ㄱ-ㅎㅏ-ㅣ0-9a-zA-Z\s]/g, '');
    $(this).val(filtered);
  });

  // 일자/정기
  $('#weekdaySection').hide();
  $('#endDate').prop('disabled', true).val('');
  // $('input[name="type"]').prop('checked', false);

  $('input[name="type"]').on('change', function () {
    // 일자
    if (this.value === 'D') {
      $('#endDate').prop('disabled', true).val(''); // 종료일 비활성화
      $('#weekdaySection').hide(); // 요일 숨김
      $('input[name="daysOfWeek"]').prop('checked', false); // 요일 선택 해제
    } else { // 정기
      $('#endDate').prop('disabled', false); // 종료일 활성화
      $('#weekdaySection').show(); // 요일 영역 보이게 변경
    }
  });

  // 회의실 선택하면 참석자 최대 인원을 회의실 용량으로 변경
  $('#room').on('change', function() {
    const capacity = $(this).find(":selected").data("capacity");
    const $attendeeCount = $('#attendeeCount');

    $attendeeCount.empty();
    $attendeeCount.append('<option value="">선택</option>');

    if (!capacity) return;

    for (let i = 1; i <= capacity; i++) {
      $attendeeCount.append('<option value="' + i + '">' + i + '</option>');
    }
  });

  // 오늘 날짜 세팅
  let today = new Date().toISOString().split("T")[0];
  console.log(today);
  $('#startDate, #endDate').attr('min', today);

}

function isValidName(val) {
  val = $.trim(val);
  const regex = /^[가-힣ㄱ-ㅎㅏ-ㅣ0-9a-zA-Z\s]+$/;
  return regex.test(val);
}


function isValidAgenda(val) {
  val = $.trim(val);
  const regex = /^[가-힣ㄱ-ㅎㅏ-ㅣ0-9a-zA-Z\s]+$/;
  return regex.test(val);
}


</script>

<%@ include file="/WEB-INF/jsp/egovframework/home/pg/common/footer.jsp"%>