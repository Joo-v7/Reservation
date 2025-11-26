package egovframework;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Controller
@RequestMapping("/bootstrap")
public class BootStrapController {

    @RequestMapping("/index.do")
    public String index() {
        return "example/bootstrap/index";
    }

    @RequestMapping("/sidebar.do")
    public String sidebar() {
        return "example/bootstrap/sidebar";
    }

    @GetMapping("/hello.do")
    public String hello() {
        return "example/bootstrap/helloworld";
    }

    @GetMapping("/admin.do")
    public String adminHome() {
        return "home/pg/admin/index";
    }
}
