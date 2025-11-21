<%--
  Created by IntelliJ IDEA.
  User: joo
  Date: 2025. 11. 11.
  Time: 10:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="/WEB-INF/jsp/egovframework/home/pg/common/header.jsp" %>

<!-- Page content-->
<div class="container-xl">
  <div class="row py-5">

    <!-- Content entries-->
    <div class="container d-flex align-items-center" style="min-height: 60vh;">
      <div class="row justify-content-center w-100">
        <div class="col-12 col-sm-10 col-md-8 col-lg-5">

            <div class="card shadow-sm p-4 rounded-4">
            <div class="text-center mb-4">
              <h4 class="fw-bold mb-2">로그인</h4>
              <p class="text-muted small">계정 정보를 입력하세요</p>
            </div>

            <form id="loginForm" method="post" action="<c:url value='/loginProcess.do'/>">
              <div class="mb-3">
                <label for="username" class="form-label">아이디</label>
                <input type="text" class="form-control form-control-lg" id="username" name="username"
                       placeholder="아이디를 입력하세요" maxlength="20" required>
              </div>

              <div class="mb-3">
                <label for="password" class="form-label">비밀번호</label>
                <input type="password" class="form-control form-control-lg" id="password" name="password"
                       placeholder="비밀번호를 입력하세요" maxlength="20" required>
              </div>

              <div class="d-flex justify-content-between align-items-center mb-4">
<%--                <div class="form-check">--%>
<%--                  <input class="form-check-input" type="checkbox" id="rememberMe">--%>
<%--                  <label class="form-check-label" for="rememberMe">자동 로그인</label>--%>
<%--                </div>--%>
<%--                <a href="#" class="small text-decoration-none">비밀번호 찾기</a>--%>
              </div>

              <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold">로그인</button>

                <!-- 네이버 OAuth2 로그인 -->
                <div class="mt-2">
                    <a href="<c:url value='/oauth2/authorization/naver'/>"
                       class="btn w-100 d-flex align-items-center justify-content-center"
                       style="background-color:#03C75A; border:none; color:#ffffff;
            font-weight:600; height:48px;">

                        <img src="/images/naverLoginIcon.png"
                             alt="naver"
                             style="width:24px; height:24px; margin-right:8px;">

                        <span>네이버 로그인</span>
                    </a>
                </div>

            </form>

            <hr class="my-4">
            <div class="text-center">
              <p class="small mb-2">계정이 없으신가요?</p>
              <a href="<c:url value='join.do'/>" class="btn btn-outline-secondary w-100 py-2">회원가입</a>
            </div>
          </div>
        </div>

      </div>

    </div>
  </div>
</div>
<script>
//페이지 로드가 완료되면
if (window.addEventListener) window.addEventListener('load', login, false);
else if (window.attachEvent) window.attachEvent('onload', login);
else window.onload = login;

function login() {
  // 로그인
  $('#loginForm').on('submit', function(e) {
      // 실패면 막고 alert
      if (!isValidUsername($('#username').val())) {
          e.preventDefault();
          alert('아이디는 6~20자의 영문 소문자, 숫자만 사용 가능합니다.');
          $('#username').focus();
          return false;
      }
  });

$('#username').on('input', function () {
    let filtered = $(this).val().toLowerCase().replace(/[^a-z0-9]/g, '').slice(0, 20);
    $(this).val(filtered);
});

}

function submitLoginForm() {
  let $form = $('#loginForm');

  let formErr = false;
  let moveFocus = '';
  let errMsg = '';

  $form.attr('method', 'post');
  $form.attr('action', '/loginProcess.do');

  // ---제출 유효성 체크 시작---
  // 아이디
  if(!formErr && !isValidUsername($('#username').val())) {
    formErr = true;
    moveFocus = 'username';
    errMsg = '아이디는 6~20자의 영문 소문자, 숫자만 사용 가능합니다.';
  }

  if (formErr) {
    alert(errMsg);
    if (moveFocus) {
      $('#' + moveFocus).focus();
    }

  }

  // $.ajax({
  //   url: '/loginProcess.do',
  //   type: 'POST',
  //   data: $form.serialize(),
  //   dataType: 'json',
  //   success: function(e) {
  //     if (e.error === 'Y') {
  //       alert(e.errorMsg);
  //     }
  //   }
  //
  // });

}

// 아이디 (영어/숫자)
function isValidUsername(val) {
  val = $.trim(val);
  const regex = /^[a-z0-9]{6,20}$/;
  return regex.test(val);
}

</script>

<%@ include file="/WEB-INF/jsp/egovframework/home/pg/common/footer.jsp"%>