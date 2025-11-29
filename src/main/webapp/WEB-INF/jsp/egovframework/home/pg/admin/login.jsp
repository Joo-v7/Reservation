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
  <title>OnRoom 관리자 로그인</title>
  <!-- Favicon-->
  <link rel="icon" href="<c:url value='/assets/favicon.ico'/>"/>
  <!-- Core theme CSS (includes Bootstrap)-->
  <link href="<c:url value='/css/styles.css'/>" rel="stylesheet"/>
</head>

<body>

<c:if test="${not empty sessionScope.errorMsg}">
  <script>
    alert('${sessionScope.errorMsg}');
  </script>
  <c:remove var="errorMsg" scope="session"/>
</c:if>

<div class="container-xl">
  <div class="row py-5">

    <!-- Content entries-->
    <div class="container min-vh-100 d-flex flex-column align-items-center justify-content-center">
      <h1 class="fw-bold mb-4">OnRoom</h1>
      <div class="row justify-content-center w-100">
        <div class="col-12 col-sm-10 col-md-8 col-lg-5">
          <div class="card shadow-sm p-4 rounded-4">
            <div class="text-center mb-4">
              <h4 class="fw-bold mb-2">관리자 로그인</h4>
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

              <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold">로그인</button>

            </form>

            <hr class="my-4">
            <div class="text-center">
              <p class="small mb-2">계정이 없으신가요?</p>
              <p class="small text-muted mb=0">문의: <a href="mailto:rlawngur145@naver.com" class="text-decoration-none">rlawngur145@naver.com</a></p>
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
if (window.addEventListener) window.addEventListener('load', login, false);
else if (window.attachEvent) window.attachEvent('onload', login);
else window.onload = login;

function login() {
  // 로그인
  $('#loginForm').on('submit', function (e) {
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

// 아이디 (영어/숫자)
function isValidUsername(val) {
  val = $.trim(val);
  const regex = /^[a-z0-9]{6,20}$/;
  return regex.test(val);
}

</script>
</body>
</html>
