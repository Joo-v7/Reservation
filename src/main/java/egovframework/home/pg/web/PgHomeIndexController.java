package egovframework.home.pg.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PgHomeIndexController {

    @GetMapping("/index.do")
    public String index() {
        return "home/pg/home";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/admin/index.do")
    public String adminHome() {
        return "home/pg/admin/index";
    }

}
