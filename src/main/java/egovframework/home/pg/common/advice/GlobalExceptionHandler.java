package egovframework.home.pg.common.advice;

import egovframework.home.pg.exception.ArgumentNotValidException;
import egovframework.home.pg.exception.ConflictException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


/**
 * 전역 예외를 처리하는 핸들러 클래스
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // 404 - web.xml에 DispatcherServlet이 해당 예외 던지도록 설정함.
    @ExceptionHandler(NoHandlerFoundException.class)
    public void handleNoHandlerFoundException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<script>");
        out.println("alert('요청하신 페이지를 찾을 수 없습니다.');");
        out.println("history.back();");
        out.println("</script>");
        out.flush();
    }

    // DB 에러
    @ExceptionHandler({DataAccessException.class})
    public ResponseEntity<?> handleDataAccessException() {
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put("error", "Y");
        retMap.put("errorTitle", "DB Error");
        retMap.put("errorMsg", "데이터 처리 중 오류가 발생했습니다.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(retMap);
    }

    // Multipart 에러
    @ExceptionHandler({MultipartException.class})
    public ResponseEntity<?> handleMultipartException() {
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put("error", "Y");
        retMap.put("errorTitle", "Multipart 처리 에러");
        retMap.put("errorMsg", "첨부파일 처리 중 오류가 발생했습니다.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(retMap);
    }

    // NPE 에러
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException() {
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put("error", "Y");
        retMap.put("errorTitle", "NPE Error");
        retMap.put("errorMsg", "시스템 오류가 발생했습니다.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(retMap);
    }

    /**
     * 리소스가 중복 예외 처리
     * @param e - ConflictException
     * @return HashMap
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflictException(ConflictException e) {
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put("error", "Y");
        retMap.put("errorTitle", "Conflict Error");
        retMap.put("errorMsg", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(retMap);
    }

    /**
     * 데이터 유효성 검사 예외 처리
     * @param e
     * @return HashMap
     */
    @ExceptionHandler(ArgumentNotValidException.class)
    public ResponseEntity<?> handleArgumentNotValidException(ArgumentNotValidException e) {
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put("error", "Y");
        retMap.put("errorTitle", "Argument Not Valid");
        retMap.put("errorMsg", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(retMap);
    }

    // IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put("error", "Y");
        retMap.put("errorTitle", "Argument Error");
        retMap.put("errorMsg", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(retMap);
    }


}

