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
          <h2 class="display-6 fw-bold mb-3">내 정보</h2>

          <table class="dataList table table-group-divider">
            <tbody>
            </tbody>
          </table>

          <div class="d-flex justify-content-end gap-3 mt-5 mb-3">
<%--            <button id="editBtn" class="view btn btn-primary" type="button">수정</button>--%>
<%--            <button id="saveBtn" class="edit hide btn btn-dark" type="button">저장</button>--%>
<%--            <button id="cancelBtn" class="edit hide btn btn-secondary" type="button">취소</button>--%>
          </div>



        </div>
      </div>
    </div>

  </div>
</div>
<script>
//페이지 로드가 완료되면
if (window.addEventListener) window.addEventListener('load', myInfo, false);
else if (window.attachEvent) window.attachEvent('onload', myInfo);
else window.onload = myInfo;

// 이벤트 정의
function myInfo() {

  dataList();

  event();

}

function dataList() {
  $('.dataList tbody').empty();

  ajaxForm('<c:url value="/myPage/getMyInfoList.do"/>', null, function(data) {
    if ($.trim(data.error) === 'N') {
      let tableData = '';
      const dataMap = data.dataMap;

      let username = (dataMap.role === 'ROLE_OAUTH') ? '소셜 로그인 이용자' : dataMap.username;

      // 아이디
      tableData += '<tr>';
      tableData += '<th class="bg-light text-center">' + '아이디' + '</th>';
      tableData += '<td>' + $.trim(username) + '</td>';
      tableData += '</tr>';

      // 이름
      tableData += '<tr>';
      tableData += '<th class="bg-light text-center">' + '이름' + '</th>';
      tableData += '<td>' + $.trim(dataMap.name) + '</td>';
      tableData += '</tr>';

      // 전화번호
      tableData += '<tr>';
      tableData += '<th class="bg-light text-center">' + '전화번호' + '</th>';
      tableData += '<td>' + dataMap.phone + '</td>';
      tableData += '</tr>';

      // 이메일
      tableData += '<tr>';
      tableData += '<th class="bg-light text-center">' + '이메일' + '</th>';
      tableData += '<td>' + dataMap.email + '</td>';
      tableData += '</tr>';

      // 생년월일
      tableData += '<tr>';
      tableData += '<th class="bg-light text-center">' + '생년월일' + '</th>';
      tableData += '<td>' + formatDate(dataMap.birthdate) + '</td>';
      tableData += '</tr>';

      // 최근 로그인 일시
      tableData += '<tr>';
      tableData += '<th class="bg-light text-center">' + '최근 로그인' + '</th>';
      tableData += '<td>' + formDateTime(dataMap.lastLogined) + '</td>';
      tableData += '</tr>';

      // 가입일시
      tableData += '<tr>';
      tableData += '<th class="bg-light text-center">' + '가입일' + '</th>';
      tableData += '<td>' + formDateTime(dataMap.regDt) + '</td>';
      tableData += '</tr>';

      $('.dataList tbody').append(tableData);

      // 데이터 불러올 시 사용할 view 화면
      // $('.edit').hide();
      // $('.view').show();

    }

  });
}

// 이벤트 정의
function event() {
  // // 수정 버튼
  // $('#editBtn').on('click', function() {
  //   $('.edit').show();
  //   $('.view').hide();
  // });
  //
  // // 취소 버튼
  // $('#cancelBtn').on('click', function() {
  //   dataList();
  //   $('.edit').hide();
  //   $('.view').show();
  // });


}

// 날짜 형식 변환
function formatDate(dataString) {
  const dateObj = new Date(dataString);
  const year = dateObj.getFullYear();
  const month = (dateObj.getMonth() + 1).toString().padStart(2, '0');
  const day = dateObj.getDate().toString().padStart(2, '0');

  return year + '-' + month + '-' + day;
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



</script>

<%@ include file="/WEB-INF/jsp/egovframework/home/pg/common/footer.jsp"%>