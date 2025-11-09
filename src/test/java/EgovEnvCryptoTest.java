import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.cryptography.EgovEnvCryptoService;
import org.egovframe.rte.fdl.cryptography.EgovPasswordEncoder;
import org.egovframe.rte.fdl.cryptography.impl.EgovEnvCryptoServiceImpl;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Slf4j
public class EgovEnvCryptoTest {

    //계정암호화키 키
    public String algorithmKey = "joo";

    //계정암호화 알고리즘(MD5, SHA-1, SHA-256)
    public String algorithm = "SHA-256";

    //계정암호화키 블럭사이즈
    public int algorithmBlockSize = 1024;

    @Test
    public void AlgorithmCreateTest() {

        EgovPasswordEncoder egovPasswordEncoder = new EgovPasswordEncoder();
        egovPasswordEncoder.setAlgorithm(algorithm);

        log.info("------------------------------------------------------");
        log.info("알고리즘(algorithm) : "+algorithm);
        log.info("알고리즘 키(algorithmKey) : "+algorithmKey);
        log.info("알고리즘 키 Hash(algorithmKeyHash) : "+egovPasswordEncoder.encryptPassword(algorithmKey));
        log.info("알고리즘 블럭사이즈(algorithmBlockSize)  :"+algorithmBlockSize);

    }

    @Test
    public void UserTest() {
        String[] arrCryptoString = {
                "root", //데이터베이스 접속 계정 설정
                "root", //데이터베이스 접속 패드워드 설정
                "jdbc:mariadb://127.0.0.1:3308/onroom", //데이터베이스 접속 주소 설정
                "org.mariadb.jdbc.Driver" //데이터베이스 드라이버
        };

        log.info("------------------------------------------------------");
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath:/egovframework/spring/context-crypto-test.xml"});
        EgovEnvCryptoService cryptoService = context.getBean(EgovEnvCryptoServiceImpl.class);
        log.info("------------------------------------------------------");

        String label = "";
        try {
            for(int i=0; i < arrCryptoString.length; i++) {
                if(i==0)label = "사용자 아이디";
                if(i==1)label = "사용자 비밀번호";
                if(i==2)label = "접속 주소";
                if(i==3)label = "데이터 베이스 드라이버";
                log.info(label+" 원본(orignal):" + arrCryptoString[i]);
                log.info(label+" 인코딩(encrypted):" + cryptoService.encrypt(arrCryptoString[i]));
                log.info("------------------------------------------------------");
            }
        } catch (IllegalArgumentException e) {
            log.error("["+e.getClass()+"] IllegalArgumentException : " + e.getMessage());
        } catch (Exception e) {
            log.error("["+e.getClass()+"] Exception : " + e.getMessage());
        }


    }
}