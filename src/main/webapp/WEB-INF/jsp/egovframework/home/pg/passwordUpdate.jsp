<%--
  Created by IntelliJ IDEA.
  User: joo
  Date: 2025. 11. 11.
  Time: 13:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="/WEB-INF/jsp/egovframework/home/pg/common/header.jsp" %>

<!-- Page content-->
<div class="container-xl">
  <div class="row py-5">

    <!-- sidebar -->
    <div class="col-lg-2 mt-5">
      <jsp:include page="/WEB-INF/jsp/egovframework/home/pg/common/sidebar.jsp">
        <jsp:param name="sideType" value="myPage"/>
        <jsp:param name="active" value="account"/>
      </jsp:include>
    </div>

    <!-- Content entries-->
    <div class="col-lg-8">
      <div class="row justify-content-center mb-auto">
        <div class="col-12 col-md-10 col-lg-10 mx-auto">
          <h2 class="display-6 fw-bold mb-3">비밀번호 변경</h2>

          <!-- 제목 아래에 선 -->
          <hr class="border-5 opacity-100 mb-4 mt-0"/>

            <form id="changeForm" class="g-3">

              <div class="col-md-6 mb-3">
                <label for="oldPassword" class="form-label fw-bold">기존 비밀번호</label>
                <input id="oldPassword" name="oldPassword" class="password form-control" type="password">
                <div class="valid-feedback">형식에 맞는 비밀번호입니다.</div>
                <div class="invalid-feedback">소문자, 숫자, 특수문자를 각각 1개 이상 사용하여, 6~20자 이내로 입력해주세요.</div>
              </div>

              <div class="col-md-6 mb-3">
                <label for="newPassword" class="form-label fw-bold">신규 비밀번호</label>
                <input id="newPassword" name="newPassword" class="password form-control" type="password">
                <div class="valid-feedback">형식에 맞는 비밀번호입니다.</div>
                <div class="invalid-feedback">소문자, 숫자, 특수문자를 각각 1개 이상 사용하여, 6~20자 이내로 입력해주세요.</div>
              </div>

              <div class="col-md-6 mb-3">
                <label for="newPasswordCheck" class="form-label fw-bold">신규 비밀번호 확인</label>
                <input id="newPasswordCheck" class="form-control" type="password">
                <div class="valid-feedback">위의 비밀번호와 일치합니다.</div>
                <div class="invalid-feedback">위의 비밀번호와 일치하지 않습니다.</div>
              </div>

              <button type="submit" class="changeBtn btn btn-outline-dark fw-bold mt-2 w-50">변경하기</button>

            </form>


        </div>
      </div>
    </div>

  </div>
</div>
<script>
//페이지 로드가 완료되면
if (window.addEventListener) window.addEventListener('load', event, false);
else if (window.attachEvent) window.attachEvent('onload', event);
else window.onload = event;

// 이벤트
function event() {

  // 비밀번호 유효성 (소문자, 숫자, 특수문자 허용 + 각 1개 이상)
  $('.password').on('input', function() {
    let val = $(this).val();

    val = val.replace(/[^a-z0-9!@#$%^&*()\-=+{}\[\]:;'",.<>?\\/|~`]/gi, '').slice(0, 20);
    $(this).val(val);

    if (isValidPassword(val)) {
      $(this).removeClass('is-invalid').addClass('is-valid');
    } else {
      $(this).removeClass('is-valid').addClass('is-invalid');
    }
  });

  // 비밀번호 확인
  $('#newPasswordCheck').on('input', function () {
    let val = $(this).val();
    let newPassword = $('#newPassword').val();

    if (isMatchedPassword(val, newPassword)) {
      $(this).removeClass('is-invalid').addClass('is-valid');
    } else {
      $(this).removeClass('is-valid').addClass('is-invalid');
    }

  });

  // 변경 버튼 클릭
  $('.changeBtn').on('click', function (e) {
    e.preventDefault();

    let $form = $('#changeForm');

    let formErr = false;
    let moveFocus = '';
    let errMsg = '';

    $form.attr('method', 'post');
    $form.attr('action', '/myPage/setPasswordUpdate.do');

    // === 제출 유효성 체크 시작 ===

    // 기존 비밀번호
    if (!formErr && !isValidPassword($('#oldPassword').val())) {
      formErr = true;
      moveFocus = 'oldPassword';
      errMsg = '소문자, 숫자, 특수문자를 각각 1개 이상 사용하여, 6~20자 이내로 입력해주세요.';
    }

    // 새로운 비밀번호
    if (!formErr && !isValidPassword($('#newPassword').val())) {
      formErr = true;
      moveFocus = 'newPassword';
      errMsg = '소문자, 숫자, 특수문자를 각각 1개 이상 사용하여, 6~20자 이내로 입력해주세요.';
    }

    // 확인 비밀번호 유효성 확인
    if (!formErr && !isValidPassword($('#newPasswordCheck').val())) {
      formErr = true;
      moveFocus = 'newPasswordCheck';
      errMsg = '소문자, 숫자, 특수문자를 각각 1개 이상 사용하여, 6~20자 이내로 입력해주세요.';
    }

    // 비밀번호 일치 확인
    if (!formErr && !isMatchedPassword($('#newPassword').val(), $('#newPasswordCheck').val())) {
      formErr = true;
      moveFocus = 'newPasswordCheck';
      errMsg = '신규 비밀번호와 확인 비밀번호가 일치하지 않습니다.';
    }

    // 기존 비밀번호와 일치하면 수정 불가능
    if (!formErr && isMatchedPassword($('#oldPassword').val(), $('#newPassword').val())) {
      formErr = true;
      moveFocus = 'newPassword';
      errMsg = '기존 비밀번호와 동일한 비밀번호로 바꿀 수 없습니다.';
    }

    if (formErr) {
      alert(errMsg);
      if (moveFocus) {
        $('#' + moveFocus).focus();
      }
      return;
    }

    ajaxForm('<c:url value="/myPage/setPasswordUpdate.do"/>', $form.serialize(), function (res) {
      if ($.trim(res.error === 'N')) {
        alert(res.successMsg);
        window.location.replace("/logout.do");
      }
    });


  });


}

// 비밀번호 (소문자/숫자/특수문자)
function isValidPassword(val) {
  val = $.trim(val);
  const regex = /^(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()\-=+{}\[\]:;'",.<>?\\/|~`])[a-z0-9!@#$%^&*()\-=+{}\[\]:;'",.<>?\\/|~`]{6,20}$/;
  return regex.test(val);
}

// 비밀번호 일치여부 확인
function isMatchedPassword(oldPassword, newPassword) {
  oldPassword = $.trim(oldPassword);
  newPassword = $.trim(newPassword);
  return oldPassword === newPassword;
}




</script>

<%@ include file="/WEB-INF/jsp/egovframework/home/pg/common/footer.jsp"%>