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
  <title>OnRoom 관리자 회의실 관리 페이지</title>
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

<c:if test="${not empty sessionScope.errorMsg}">
  <script>
    alert('${sessionScope.errorMsg}');
  </script>
  <c:remove var="errorMsg" scope="session"/>
</c:if>

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
      <h1 class="mt-4">회의실 관리</h1>

      <!-- 상단 툴바: 좌측 메타, 우측 검색 -->
      <div class="d-flex justify-content-between align-items-center mb-3">

        <%-- 리스트 수 표시--%>
        <span class="count text-muted small"></span>

        <!-- 우측: 검색 폼 -->
        <form id="searchForm" class="d-flex align-items-center gap-2">
          <input type="hidden" id="movePage" name="movePage" value="<c:out value='${param.movePage}' default='1' />">

          <select id="searchType" name="searchType" class="form-select w-auto">
            <option value="" <c:if test="${param.searchType eq ''}">selected</c:if>>회의실 사용여부 선택</option>
            <option value="Y" ${param.searchType == 'Y' ? 'selected' : ''}>사용</option>
            <option value="N" ${param.searchType == 'N' ? 'selected' : ''}>미사용</option>
          </select>

          <input id="searchQuery" name="searchQuery" type="text" class="form-control"  placeholder="회의실 이름을 입력하세요" value="<c:out value="${param.searchQuery}" />">

          <button id="searchBtn" type="button" class="btn btn-dark flex-shrink-0">검색</button>

        </form>
      </div>

      <!-- 목록 -->
      <div class="table-responsive">
        <table  id="dataList" class="dataList table table-hover align-middle mb-0">
          <thead class="table-active">
          <tr>
            <th class="text-center">연번</th>
            <th class="text-center">회의실명</th>
            <th class="text-center">정원</th>
            <th class="text-center">이미지</th>
            <th class="text-center">사용 여부</th>
            <th class="text-center">등록일시</th>
            <th class="text-center">수정일시</th>
            <th class="text-center">관리</th>
          </tr>
          </thead>
          <tbody>
          </tbody>
        </table>
      </div>

      <!-- 페이지네이션 -->
      <div id="customPage" class="d-flex justify-content-center mt-5">
        <div class="pagination"></div>
      </div>

      <!-- 회의실 등록 버튼 -->
      <div class="d-flex justify-content-end mb-3">
        <button id="roomInsertBtn" type="button" class="btn btn-primary" >회의실 등록</button>
      </div>


      <!-- 회의실 등록/수정 모달 -->
      <div id="roomModal" class="modal fade" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">

            <div class="modal-header bg-dark text-white">
              <h5 id="modalTitle" class="modal-title">회의실 관리</h5>
              <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>

            <div class="modal-body">
              <form id="modalForm" enctype="multipart/form-data">
                <p class="small"><span class="text-danger mb-3">*</span> 는 필수 입력 사항입니다.</p>

                <!-- roomId -->
                <input id="roomId" name="roomId" type="hidden" value="">
<%--                <!-- 현재 이미지명 -->--%>
<%--                <input id="currentImageName" name="currentImageName" type="hidden" value="">--%>
<%--                <!-- 현재 이미지 데이터 -->--%>
<%--                <input id="currentImageUrl" name="currentImageUrl" type="hidden" value="">--%>

                <div class="mb-3">
                  <label for="roomName" class="form-label fw-bold">회의실명 <span class="text-danger mb-3">*</span></label>
                  <input id="roomName" name="name" type="text" class="form-control" placeholder="회의실명을 입력하세요" required maxlength="100">
                </div>

                <div class="mb-3">
                  <label for="capacity" class="form-label fw-bold">수용인원 <span class="text-danger mb-3">*</span></label>
                  <input id="capacity" name="capacity" type="number" class="form-control" min="1" placeholder="수용 인원을 입력하세요" required>
                </div>

                <div class="mb-3">
                  <label for="description" class="form-label fw-bold">설명 <span class="text-danger mb-3">*</span></label>
                  <textarea id="description" name="description" class="form-control" rows="3" placeholder="회의실 설명을 입력하세요" required maxlength="255"></textarea>
                </div>

                <div class="mb-3">
                  <label for="imageUrl" class="form-label fw-bold">이미지</label>
                  <input id="imageUrl" name="imageUrl" type="file" class="form-control" placeholder="이미지를 넣어주세요">
                  <!-- 현재 이미지 미리보기 용 -->
                  <div id="currentImage" class="mt-2"></div>
                </div>

                <div class="mb-3">
                  <label for="useYn" class="form-label fw-bold">사용여부 <span class="text-danger mb-3">*</span></label>
                  <select id="useYn" name="useYn" class="form-select">
                    <option value="Y">사용</option>
                    <option value="N">미사용</option>
                  </select>
                </div>


              </form>
            </div>

            <div class="modal-footer d-flex justify-content-center">
              <button id="roomSaveBtn" type="button" class="btn btn-primary">저장</button>
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
            </div>

          </div>
        </div>
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
if (window.addEventListener) window.addEventListener('load', roomList, false);
else if (window.attachEvent) window.attachEvent('onload', roomList);
else window.onload = roomList;

function roomList() {
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
  ajaxForm('<c:url value="/admin/getRoomList.do"/>', $('#searchForm').serialize(), function(data) {
    if ($.trim(data.error) === 'N') {
      let tableData = '';
      let trClass = '';
      let page = Number(data.dataMap.page);
      let pageCnt = Number(data.dataMap.pageCnt);
      let recordCnt = Number(data.dataMap.recordCnt ?? data.dataMap.list?.length ?? 0);
      let totalCnt = Number(data.dataMap.totalCnt);

      if (!recordCnt) recordCnt = 0;
      let startNo = totalCnt - ((page - 1) * recordCnt);

      $.each(data.dataMap.list, function(index, item) {

        tableData += '<tr' + trClass + '>';
        // 연번
        tableData += '<td class="text-center">' + (startNo - index) + '</td>';
        // 회의실명
        tableData += '<td class="text-center">' + $.trim(item.name) + '</td>';
        // 정원
        tableData += '<td class="text-center">' + item.capacity + '</td>';
        // 이미지
        const imagePath = '<c:url value="/attachment/image/" />';

        tableData += '<td class="text-center">';
        tableData += item.imageUrl ?
                '<img src="' + imagePath + item.imageUrl + '" style="width:150px; height:100px; object-fit:cover; border-radius:8px;" alt="회의실 이미지"/>'
                : '-';
        tableData += '</td>';
        // 사용여부
        tableData += '<td class="text-center">' + item.useYn + '</td>';
        // 등록일시
        tableData += '<td class="text-center">' + formDateTime(item.regDt) + '</td>';
        // 수정일시
        tableData += '<td class="text-center">' + (item.updtDt ? formDateTime(item.updtDt) : '-') + '</td>';
        // 관리
        // 사용여부 버튼용 텍스트 / 다음 상태
        let useBtnText = item.useYn === 'Y' ? '미사용' : '사용';
        let nextUseYn  = item.useYn === 'Y' ? 'N' : 'Y';

        tableData += '<td class="text-center">' +
                '<button type="button" class="updateBtn btn btn-success" ' +
                'data-room-id="' + item.roomId + '">수정</button>' +
                '<button type="button" class="useYnBtn btn ' + (item.useYn === 'Y' ? 'btn-danger' : 'btn-primary') + '" ' +
                'data-room-id="' + item.roomId + '" ' +
                'data-use-yn="' + nextUseYn + '">' + useBtnText + '</button>' +
                '</td>';

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
      customPagination('customPage', data.dataMap.page, data.dataMap.pageCnt);
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
  });

  // 회의실명 입력: 한글/영어/숫자만 가능
  $('#roomName').on('input', function () {
    const filtered = $(this).val().replace(/[^가-힣ㄱ-ㅎㅏ-ㅣ0-9a-zA-Z\s]/g, '');
    $(this).val(filtered);
  });

  // 회의실명 입력: 한글/영어/숫자만 가능
  $('#description').on('input', function () {
    const filtered = $(this).val().replace(/[^가-힣ㄱ-ㅎㅏ-ㅣ0-9a-zA-Z\s]/g, '');
    $(this).val(filtered);
  });

  // 사용/미사용 버튼 클릭
  $('.dataList').on('click', '.useYnBtn', function () {
    const roomId = $(this).attr('data-room-id');
    const useYn = $(this).attr('data-use-yn');

    let $form = $('#searchForm');

    // 같은 Hidden 값이 계속 쌓이지 않게 기존 값 제거
    $form.find('input[name="useYn"]').remove();
    $form.find('input[name="roomId"]').remove();
    $form.find('input[name="action"]').remove();

    // hidden input 추가
    $form.append('<input type="hidden" name="useYn" value="' + useYn + '">');
    $form.append('<input type="hidden" name="roomId" value="' + roomId + '">');

    ajaxForm('<c:out value="/admin/setRoomUseYnUpdate.do" />', $form.serialize(), function(res) {
      if (res.error === 'N') {
        alert(res.successMsg);
        dataList();
      }
    });

  });

  // 회의실 등록 버튼 클릭
  $('#roomInsertBtn').on('click', function () {
    roomModal('insert');
  });

  // 수정 버튼 클릭
  $('.dataList').on('click', '.updateBtn', function () {
    const roomId = $(this).attr('data-room-id');
    roomModal('update', roomId);
  });

  // 회의실 저장
  // 모달 내 등록/수정 폼에서 저장 버튼 클릭
  $('#roomSaveBtn').on('click', function () {
    const modalForm = $('#modalForm')[0];
    const formData = new FormData(modalForm);

    // === 제출 유효성 체크 ===
    let formErr = false;
    let moveFocus = '';
    let errMsg = '';

    // 회의실명
    if (!formErr && !isValidRoomName($('#roomName').val())) {
      formErr = true;
      moveFocus = 'roomName';
      errMsg = '회의실명은 100자 이하의 영문자, 한글, 숫자만 입력 가능합니다.';
    }

    // 수용인원
    if (!formErr && ($('#capacity').val() === '')) {
      formErr = true;
      moveFocus = 'capacity';
      errMsg = '수용인원은 숫자만 입력 가능합니다.';
    }

    // 설명
    if (!formErr && !isValidDescription($('#description').val())) {
      formErr = true;
      moveFocus = 'description';
      errMsg = '설명은 255자 이하의 영문자, 한글, 숫자만 입력 가능합니다.';
    }

    if (formErr) {
      alert(errMsg);
      if (moveFocus) {
        $('#' + moveFocus).focus();
      }
      return;
    }


    // merge
    ajaxForm('<c:url value="/admin/setRoomMerge.do"/>', formData, function(res) {
      if ($.trim(res.error) == 'N') {
        alert(res.successMsg);

        $('#roomModal').modal('hide'); // 모달 닫기
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

// 회의실명 (한글/영어/숫자/공백)
function isValidRoomName(val) {
  val = $.trim(val);
  const regex = /^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9\s]{1,100}$/;
  return regex.test(val);
}

// 회의실 설명 입력 (한글/영어/숫자/공백)
function isValidDescription(val) {
  val = $.trim(val);
  const regex = /^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9\s]{1,255}$/;
  return regex.test(val);
}


// 회의실 등록/수정 모달창
// (action, roomId)
function roomModal(action, roomId) {
  // 폼/히든값 초기화
  $('#modalForm')[0].reset();
  $('#roomId').val('');
  // $('#currentImageUrl').val('');
  // $('#currentImageName').val('');
  $('#currentImage').empty();

  $('#roomName').prop('readonly', action === 'update'); // 회의실명은 UNIQUE라서 수정일때 readonly

  // 회의실 등록일떄
  if (action === 'insert') {
    $('#modalTitle').text('회의실 등록');
    $('#userYn').val('Y');

    // 모달 열기
    let modalEl = document.getElementById('roomModal');
    let modal = new bootstrap.Modal(modalEl);
    modal.show();
  } else { // 회의실 수정일때
    $('#modalTitle').text('회의실 수정');
    ajaxForm('<c:url value="/admin/getRoom.do"/>', {roomId: roomId}, function (res) {
      if (res.error === 'N') {
        const data = res.dataMap;

        $('#roomId').val(data.roomId);
        $('#roomName').val(data.name);
        $('#capacity').val(data.capacity);
        $('#description').val(data.description);
        $('#useYn').val(data.useYn);

        // 기존 이미지 파일/파일명 히든에 저장
        // if (data.imageName && data.imageUrl) {
        //   $('#currentImageName').val(data.imageName);
        //   $('#currentImageUrl').val(data.imageUrl);
        // }

        // 기존 이미지 모달에 표시
        if (data.imageUrl) {
          const base = '<c:url value="/admin/room/image.do"/>'
          const url = base + '?imageUrl=' + data.imageUrl + '&imageName=' + data.imageName;
          // append() X -> html()로 전체 덮어쓰기
          $('#currentImage').html(
                  '<span class="text-muted small me-1">* 기존 이미지:</span>' +
                  '<a href="' + url + '">' + data.imageName + '<a></td>'
          )
        }

        // 모달 열기
        let modalEl = document.getElementById('roomModal');
        let modal = new bootstrap.Modal(modalEl);
        modal.show();

      }
    });
  }
}




</script>

</body>
</html>
