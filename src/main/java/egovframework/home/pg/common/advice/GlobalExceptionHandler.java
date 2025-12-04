package egovframework.home.pg.common.advice;

import egovframework.home.pg.exception.AccessDeniedException;
import egovframework.home.pg.exception.ArgumentNotValidException;
import egovframework.home.pg.exception.ConflictException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;


/**
 * 전역 예외를 처리하는 핸들러 클래스
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * NoHandlerFoundException
     * DispatcherService가 처리할 수 없는 요청 (404) 예외 처리
     * @param request
     * @param response
     * @throws IOException
     */
    // web.xml에 DispatcherServlet이 해당 예외 던지도록 설정함, 공통 헤더 (header.jsp) 에서 처리
    @ExceptionHandler(NoHandlerFoundException.class)
    public void handleNoHandlerFoundException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String errorMsg = "요청하신 페이지를 찾을 수 없습니다.";

        request.getSession().setAttribute("errorMsg", errorMsg);
        response.sendRedirect(request.getContextPath() + "/");
    }

    /**
     * DataAccessException
     * DB 데이터 처리 중 발생한 예외 처리
     * @return 에러 응답 map
     */
    @ExceptionHandler({DataAccessException.class})
    public ResponseEntity<?> handleDataAccessException() {
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put("error", "Y");
        retMap.put("errorTitle", "DB Error");
        retMap.put("errorMsg", "데이터 처리 중 오류가 발생했습니다.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(retMap);
    }

    /**
     * MultipartException
     * @return 에러 응답 map
     */
    @ExceptionHandler({MultipartException.class})
    public ResponseEntity<?> handleMultipartException() {
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put("error", "Y");
        retMap.put("errorTitle", "Multipart 처리 에러");
        retMap.put("errorMsg", "첨부파일 처리 중 오류가 발생했습니다.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(retMap);
    }

    /**
     * NullPointerException
     * @return 에러 응답 map
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException() {
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put("error", "Y");
        retMap.put("errorTitle", "NPE Error");
        retMap.put("errorMsg", "시스템 오류가 발생했습니다.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(retMap);
    }

    /**
     * IllegalArgumentException
     * @param e
     * @return 에러 응답 map
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put("error", "Y");
        retMap.put("errorTitle", "Argument Error");
        retMap.put("errorMsg", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(retMap);
    }

    /**
     * ConflictException
     * 리소스 중복 커스텀 예외 처리
     * @param e - ConflictException
     * @return 에러 응답 map
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
     * ArgumentNotValidException
     * 데이터 유효성 검사 커스텀 예외 처리
     * @param e
     * @return 에러 응답 map
     */
    @ExceptionHandler(ArgumentNotValidException.class)
    public ResponseEntity<?> handleArgumentNotValidException(ArgumentNotValidException e) {
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put("error", "Y");
        retMap.put("errorTitle", "Argument Not Valid");
        retMap.put("errorMsg", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(retMap);
    }

    /**
     * AccessDeniedException
     * OAuth 이용자 접근 예외 처리
     * @param e
     * @return 에러 응답 map
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put("error", "Y");
        retMap.put("errorTitle", "Access Denied");
        retMap.put("errorMsg", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(retMap);
    }


}

