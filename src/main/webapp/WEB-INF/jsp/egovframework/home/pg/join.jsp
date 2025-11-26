<%--
  Created by IntelliJ IDEA.
  User: joo
  Date: 2025. 11. 11.
  Time: 13:22
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
<%--      <%@ include file="/WEB-INF/jsp/egovframework/home/pg/common/sidebar.jsp" %>--%>
    </div>

    <!-- Content entries-->
    <div class="col-lg-10">
      <div class="row justify-content-center mb-auto">
        <div class="col-12 col-md-10 col-lg-8 mx-auto">

          <form id="joinForm" method="post">

            <p class="small"><span class="text-danger mb-3">*</span> 는 필수 입력 사항입니다.</p>

            <dl class="row mb-3">
              <dt class="col-sm-2 col-form-label">아이디 <span class="text-danger">*</span></dt>
              <dd class="col-sm-7">
                <div class="input-group">
                  <input id="username" name="username" type="text" class="form-control" placeholder="6~20자의 영문 소문자, 숫자를 입력하세요" maxlength="20" required>
                  <button type="button" id="checkIdBtn" class="btn btn-outline-secondary">중복확인</button>
                  <div class="valid-feedback">사용가능한 아이디입니다.</div>
                  <div class="invalid-feedback">6~20자의 영문 소문자, 숫자만 사용 가능합니다.</div>
                </div>
              </dd>
            </dl>

            <dl class="row mb-3">
              <dt class="col-sm-2 col-form-label">비밀번호 <span class="text-danger">*</span></dt>
              <dd class="col-sm-7">
                <input id="password" name="password" type="password" class="form-control" placeholder="비밀번호를 입력하세요." maxlength="20" required>
                <div class="valid-feedback">사용 가능한 비밀번호입니다.</div>
                <div class="invalid-feedback">비밀번호를 입력해주세요.</div>
              </dd>
            </dl>

            <dl class="row mb-3">
              <dt class="col-sm-2 col-form-label">이름 <span class="text-danger">*</span></dt>
              <dd class="col-sm-7">
                <input id="name" name="name" type="text" class="form-control" placeholder="한글만 입력 가능합니다." maxlength="20" required>
                <div class="valid-feedback"></div>
                <div class="invalid-feedback">한글만 입력가능합니다.</div>
              </dd>
            </dl>

            <dl class="row mb-3">
              <dt class="col-sm-2 col-form-label">전화번호 <span class="text-danger">*</span></dt>
              <dd class="col-sm-7">
                <input id="phone" name="phone" type="text" class="form-control" placeholder="숫자만 입력하세요. ex) 01012345678" maxlength="13" required>
                <div class="valid-feedback"></div>
                <div class="invalid-feedback">형식에 맞게 숫자만 입력하세요.</div>
              </dd>
            </dl>

            <dl class="row mb-3">
              <dt class="col-sm-2 col-form-label">이메일 <span class="text-danger">*</span></dt>
              <dd class="col-sm-7">
                <input id="email" name="email" type="email" class="form-control" placeholder="이메일을 입력하세요." maxlength="100" required>
                <div class="valid-feedback"></div>
                <div class="invalid-feedback">올바른 이메일을 입력해주세요.</div>
              </dd>
            </dl>

            <dl class="row mb-3">
              <dt class="col-sm-2 col-form-label">생년월일 <span class="text-danger">*</span></dt>
              <dd class="col-sm-7">
                <input id="birthdate" name="birthdate" type="date" class="form-control" required>
              </dd>
            </dl>

            <dl class="row mb-5">
              <dt class="col-sm-2 col-form-label">약관동의 <span class="text-danger">*</span></dt>
              <dd class="col-sm-7 pt-2">
                <input id="agreement" class="form-check-input" type="checkbox" required>
                <label class="form-check-label" for="agreement">약관동의</label>
              </dd>
            </dl>

            <div class="d-flex justify-content-start gap-2 mt-5 mb-5">
              <input type="submit" class="btn btn-dark w-75" value="회원가입">
            </div>

          </form>

        </div>
      </div>

    </div>
  </div>
</div>
<script>
//페이지 로드가 완료되면
if (window.addEventListener) window.addEventListener('load', join, false);
else if (window.attachEvent) window.attachEvent('onload', join);
else window.onload = join;

// 이벤트 정의
function join() {

  // 회원가입
  $('#joinForm').on('submit', function (e) {
    console.log('회원가입 버튼 클릭')
    e.preventDefault();
    submitJoinForm();
  });

  // 이벤트 등록
  bindEvents();

}

// 회원가입 폼 제출
function submitJoinForm() {
  let $form = $('#joinForm');

  let formErr = false;
  let moveFocus = '';
  let errMsg = '';

  $form.attr('method', 'post');
  $form.attr('action', '/setMemberMerge.do');

  // ---제출 유효성 체크 시작---
  // 아이디
  if(!formErr && !isValidUsername($('#username').val())) {
    formErr = true;
    moveFocus = 'username';
    errMsg = '아이디는 6~20자의 영문 소문자, 숫자만 사용 가능합니다.';
  }

  // 비밀번호

  // 이름
  if(!formErr && !isValidName($('#name').val())) {
    formErr = true;
    moveFocus = 'name';
    errMsg = '이름은 1~20자의 한글만 입력 가능합니다.'
  }

  // 전화번호
  if(!formErr && !isValidPhone($('#phone').val())) {
    formErr = true;
    moveFocus = 'phone';
    errMsg = '올바른 전화번호를 입력해주세요. ex) 010-1234-5678'
  }

  // 이메일
  if(!formErr && !isValidEmail($('#email').val())) {
    formErr = true;
    moveFocus = 'email';
    errMsg = '올바른 이메일 형식을 입력해주세요. ex) onroom@onroom.com'
  }

  // 생년월일
  if (!formErr && !isValidBirthDate($('#birthdate').val())) {
    formErr = true;
    moveFocus = 'birthdate';
    errMsg = '생년월일은 오늘 이후 날짜를 선택할 수 없습니다.';
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
  ajaxForm('<c:url value="/setMemberMerge.do"/>', $form.serialize(), function (res) {
    // 응답 성공 시
    if (res.error === 'N') {
      alert(res.successMsg);
      window.location.replace("/login.do");
    }
    // alert 2번 호출되는 이유: ajaxForm 에서 error 발생 시 내부에서 alert 띄움.
    // else {
    //     alert(res.errorMsg);
    // }
  });

}

function bindEvents() {

  // 아이디 (영어 소문자, 숫자만 사용 가능)
  $('#username').on('input', function (e) {
    let filtered = $(this).val().toLowerCase().replace(/[^a-z0-9]/g, '').slice(0, 20);
    $(this).val(filtered);

    if (/^[a-z0-9]{6,20}$/.test(filtered)) {
      $(this).removeClass('is-invalid').addClass('is-valid');
    } else {
      $(this).removeClass('is-valid').addClass('is-invalid');
    }

  });

  // 아이디 중복확인 버튼
  $('#checkIdBtn').on('click', function() {
    let username = $.trim($('#username').val());

    if (!isValidUsername(username)) {
      alert('아이디는 6~20자의 영문 소문자, 숫자만 사용 가능합니다.');
      $('#username').focus();
      return;
    }

    ajaxForm('<c:url value="/duplicateId.do"/>', { username:username }, function (res) {
      if (res.error === 'N') {
        alert(res.successMsg);
        $('#username').removeClass('is-invalid').addClass('is-valid');
      } else {
        $('#username').removeClass('is-valid').addClass('is-invalid');
      }
    });
  });

  // 비밀번호

  // 이름 (한글만)
  $('#name').on('input', function () {
    const filtered = $(this).val().replace(/[^가-힣ㄱ-ㅎㅏ-ㅣ]/g, '');
    $(this).val(filtered);

    if (/^[가-힣ㄱ-ㅎㅏ-ㅣ]{1,20}$/.test(filtered)) {
      $(this).removeClass('is-invalid').addClass('is-valid');
    } else {
      $(this).removeClass('is-valid').addClass('is-invalid');
    }

  });

  // 전화번호
  $('#phone').on('input', function () {
    let val = normalizeDigits($(this).val());
    $(this).val(val);
  })
  .on('blur', function () {
    let val = formatKRPhone(normalizeDigits($(this).val()));
    $(this).val(val);

    if (/^010-?\d{4}-?\d{4}$/.test(val)) {
      $(this).removeClass('is-invalid').addClass('is-valid');
    } else {
      $(this).removeClass('is-valid').addClass('is-invalid');
    }
  });

  // 이메일 (영어/숫자/@._%+- 허용)
  $('#email').on('input', function() {
    const val = $(this).val();
    // 이메일에 허용되지 않는 문자 제거
    const filtered = val.replace(/[^a-zA-Z0-9@._%+-]/g, '');
    $(this).val(filtered);

    // 유효성 검사
    if (/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[A-Za-z]{2,}$/.test(filtered)) {
      $(this).removeClass('is-invalid').addClass('is-valid');
    } else {
      $(this).removeClass('is-valid').addClass('is-invalid');
    }
  });

  // 생년월일
  const today = new Date().toISOString().split('T')[0];
  console.log(today);

  $('#birthdate').on('change', function () {
    const val = $(this).val();

    if (val && val > today) {
      alert('생년월일은 오늘 이후 날짜를 선택할 수 없습니다.');
      $(this).val('');
    }

  });

}

// 아이디 (영어/숫자)
function isValidUsername(val) {
  val = $.trim(val);
  const regex = /^[a-z0-9]{6,20}$/;
  return regex.test(val);
}


// 이름 (한글만)
function isValidName(val) {
  val = $.trim(val);
  const regex = /^[가-힣ㄱ-ㅎㅏ-ㅣ]{1,20}$/;
  return regex.test(val);
}

// 전화번호
function isValidPhone(val) {
  val = $.trim(val);

  let digits = val.replace(/\D/g, '');

  if (digits.length !== 11) return false;

  if (!digits.startsWith("010")) return false;

  return true;
}

// 이메일
function isValidEmail(val) {
  val = $.trim(val);
  const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[A-Za-z]{2,}$/;
  return regex.test(val);
}

// 생년월일
function isValidBirthDate(val) {
  const today = new Date().toISOString().split('T')[0];
  val = $.trim(val);

  const regex = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;
  if (!regex.test(val)) return false;

  if (val > today) return false;

  return true;
}

// 전화번호: 숫자만 남기기 & 11자리 제한
function normalizeDigits(raw) {
  let d = String(raw).replace(/\D/g, '');
  return d.slice(0, 11);
}

// 전화번호 - 하이픈 자동 추가
function formatKRPhone(d) {
  if (!d) return '';
  return d.slice(0,3) + '-' + d.slice(3, d.length-4) + '-' + d.slice(-4);
}


</script>

<%@ include file="/WEB-INF/jsp/egovframework/home/pg/common/footer.jsp"%>