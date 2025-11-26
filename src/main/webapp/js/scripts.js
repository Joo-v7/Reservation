/*!
* Start Bootstrap - Simple Sidebar v6.0.6 (https://startbootstrap.com/template/simple-sidebar)
* Copyright 2013-2023 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-simple-sidebar/blob/master/LICENSE)
*/
// 
// Scripts
// 

window.addEventListener('DOMContentLoaded', event => {

    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        // Uncomment Below to persist sidebar toggle between refreshes
        // if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
        //     document.body.classList.toggle('sb-sidenav-toggled');
        // }
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });
    }

});

// ajaxForm
function ajaxForm(a, b, c) {
    var d = !0
        , f = "application/x-www-form-urlencoded; charset=UTF-8";
    "object" === $.type(b) && b instanceof FormData && (f = d = !1);
    $.ajax({
        type: "post",
        url: a,
        cache: !1,
        data: b,
        processData: d,
        contentType: f,
        dataType: "json",
        success: function(e) {
            if ("N" == e.error)
                "undefined" !== $.type(e.redirect) && "" != $.trim(e.redirect) && ("reload" == $.trim(e.redirect) ? document.location.reload(!0) : document.location.href = $.trim(e.redirect));
            else {
                var g = 0;
                "undefined" !== $.type(e.inputArr) && $.each(e.inputArr, function(h, k) {
                    0 == g && $("#" + h).focus();
                    g++
                });
                "undefined" !== $.type(e.errorTitle) && "" != $.trim(e.errorTitle) && alert(e.errorMsg)
            }
            "function" === $.type(c) && c(e)
        },
        error: function(e, g, h) {

            console.log("status:", e.status);
            console.log("responseText:", e.responseText);
            console.log("responseJSON:", e.responseJSON);

            res = JSON.parse(e.responseText);

            if (res && res.errorMsg) {
                alert(res.errorMsg);   // 서버에서 내려준 메시지 출력
            } else {
                alert("[" + e.status + "] " + h);
            }
        }
    })
}

// id, curPage, totalPage, 시작 페이지 번호(안념겨도됨)
function customPagination(a, b, c, d) {
    const group = 10;                   // 한 번에 보여줄 페이지 수
    d = group * (Math.ceil(b / group) - 1) + 1; // 시작 페이지
    let f = d + group - 1;              // 끝 페이지
    if (f >= c) f = c;

    $("#" + a + " .pagination").html(""); // 비우기
    let e = "";

    // << 이전 그룹
    if (b > group) {
        e += '<li class="page-item"><a href="#" class="page-link" aria-label="Previous" data-tp="' + a + '" data-move="' + (d - group) + '" title="first">'
            + '<span aria-hidden="true">&laquo;</span></a></li>';
    }
    // < 이전
    if (b > 1) {
        e += '<li class="page-item"><a href="#" class="page-link" data-tp="' + a + '" data-move="' + (b - 1) + '" title="prev">'
            + '<span aria-hidden="true">&lsaquo;</span></a></li>';
    }

    // 숫자 페이지들
    for (var g = d; g <= f; g++) {
        if (b !== g) {
            e += '<li class="page-item"><a href="#" class="page-link" data-tp="' + a + '" data-move="' + g + '">' + g + '</a></li>';
        } else {
            e += '<li class="page-item"><a href="#" class="page-link active" data-tp="' + a + '" data-move="' + g + '" class="active" title="현재 ' + g + '페이지">'
                + g + '</a></li>';
        }
    }

    // > 다음
    if (b < c) {
        e += '<li class="page-item"><a href="#" class="page-link" data-tp="' + a + '" data-move="' + (b + 1) + '" title="next">'
            + '<span aria-hidden="true">&rsaquo;</span></a></li>';
    }
    // >> 다음 그룹
    if (Math.ceil(b / group) < Math.ceil(c / group)) {
        e += '<li class="page-item"><a href="#" class="page-link" data-tp="' + a + '" data-move="' + (d + group) + '" title="last">'
            + '<span aria-hidden="true">&raquo;</span></a></li>';
    }

    if ($.trim(e) !== "") {
        $("#" + a + " .pagination").html("<ul class='pagination'>" + e + "</ul>");} // bootstrap5: ul-class pagination 적용
}