<%--
  Created by IntelliJ IDEA.
  User: joo
  Date: 2025. 11. 8.
  Time: 15:07
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
    <div class="col-lg-2">
      <%@ include file="/WEB-INF/jsp/egovframework/home/pg/common/sidebar.jsp" %>
    </div>

    <!-- Content entries-->
    <div class="col-lg-12">
      <div class="row justify-content-center mb-auto">
        <div class="col-12 col-md-10 col-lg-8">

          <form id="boardForm" method="post">
            <p class="small"><span class="text-danger mb-3">*</span> 는 필수 입력 사항입니다.</p>
            <dl class="row mb-3">
              <dt class="col-sm-2 col-form-label">제목 <span class="text-danger">*</span></dt>
              <dd class="col-sm-5">
                <input type="text" class="form-control" id="title" name="title" maxlength="50" required>
              </dd>
            </dl>

            <dl class="row mb-3">
              <dt class="col-sm-2 col-form-label">분류 <span class="text-danger">*</span></dt>
              <dd class="col-sm-2">
                <select id="boardType" name="boardTypeId" class="form-select" required>
                  <option value="" selected>선택</option>
                  <c:forEach var="boardType" items="${boardTypeList}">
                    <option value="${boardType.boardTypeId}">${boardType.name}</option>
                  </c:forEach>
                </select>
              </dd>
            </dl>

            <dl class="row mb-3">
              <dt class="col-sm-2 col-form-label">내용 <span class="text-danger">*</span>
              </dt>
              <dd class="col-sm-10">
                <textarea class="form-control" id="content" name="content" rows="8" maxlength="255" required></textarea>
              </dd>
            </dl>

            <div class="d-flex justify-content-end gap-2 mt-3 mb-5">
              <input type="submit" class="btn btn-primary" value="저장">
              <button id="boardList" type="button" class="btn btn-dark">취소</button>
            </div>

          </form>

        </div>
      </div>

    </div>
  </div>
</div>
<script>
//페이지 로드가 완료되면
if (window.addEventListener) window.addEventListener('load', board, false);
else if (window.attachEvent) window.attachEvent('onload', board);
else window.onload = board;

// 이벤트 정의
function board() {

  // 목록
  $('#boardList').on('click', function(){
    console.log('목록 버튼 클릭');
    boardList();

  });

  // 저장
  $('#boardForm').on('submit', function (e) {
    console.log('등록 버튼 클릭')
    e.preventDefault();
    submitBoardForm();
  });

  // 이벤트 등록
  bindEvents();

}

// 게시판 목록으로 이동
function boardList() {
  let $form = $('#searchForm');
  $form.attr('action', "/boardList.do");
  $form.submit();
}


// 입력 폼 제출
function submitBoardForm() {
  var $form = $('#boardForm');
  let $searchForm = $('#searchForm');

  var formErr = false;
  var moveFocus = '';
  let errMsg = '';

  $form.attr('method', 'post');
  $form.attr('action', '/setBoardMerge.do');

  // ---제출 유효성 체크 시작---
  // 제목
  if(!formErr && !isValidTitle($('#title').val())) {
    formErr = true;
    moveFocus = 'title';
    errMsg = '제목은 한글, 영어, 숫자만 입력 가능합니다.';
  }

  // 내용
  if(!formErr && !isValidContent($('#content').val())) {
    formErr = true;
    moveFocus = 'content';
    errMsg = '내용은 한글, 영어, 숫자만 입력 가능합니다.';
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
  ajaxForm('<c:url value="/setBoardMerge.do"/>', $form.serialize(), function (res) {
    // 응답 성공 시
    if (res.error === 'N') {
      alert(res.successMsg);
      $searchForm.attr('action', "/boardList.do");
      $searchForm.submit(); // 리스트 페이지로 이동
    }
    // alert 2번 호출되는 이유: ajaxForm 에서 error 발생 시 내부에서 alert 띄움.
    // else {
    //     alert(res.errorMsg);
    // }
  });

}

function bindEvents() {
  // 제목 (한글, 숫자, 영어, 공백 허용)
  $('#title').on('input', function() {
    const filtered = $(this).val().replace(/[^가-힣ㄱ-ㅎㅏ-ㅣ0-9a-zA-Z\s]/g, '');
    $(this).val(filtered);
  });

  // 내용 (한글, 숫자, 영어, 공백 허용)
  $('#content').on('input', function() {
    const filtered = $(this).val().replace(/[^가-힣ㄱ-ㅎㅏ-ㅣ0-9a-zA-Z\s]/g, '');
    $(this).val(filtered);
  });

}

function isValidTitle(val) {
  val = $.trim(val);
  const regex = /^[가-힣ㄱ-ㅎㅏ-ㅣ0-9a-zA-Z\s]+$/;
  return regex.test(val);
}


function isValidContent(val) {
  val = $.trim(val);
  const regex = /^[가-힣ㄱ-ㅎㅏ-ㅣ0-9a-zA-Z\s]+$/;
  return regex.test(val);
}

</script>

<%@ include file="/WEB-INF/jsp/egovframework/home/pg/common/footer.jsp"%>