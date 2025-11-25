package egovframework.home.pg.common.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    // Redis 해쉬 이름
    private static final String LOGIN_ATTEMPT_COUNT = "LOGIN_ATTEMPT_COUNT:";
    private static final String BLACKLIST = "BLACKLIST:";
    private static final int MAX_ATTEMPT_COUNT = 5;

    /**
     * 레디스 등록
     */
    public void registerInRedis(String username) {
        String key = LOGIN_ATTEMPT_COUNT + username;
        long ttlSeconds = TimeUnit.MINUTES.toSeconds(15); // 15분 TTL
        redisTemplate.opsForValue().set(key, 0, ttlSeconds, TimeUnit.SECONDS);
    }

    /**
     * Redis 조회
     */
    public boolean existsRedis(String username) {
        String key = LOGIN_ATTEMPT_COUNT + username;
        return redisTemplate.hasKey(key);
    }

    /**
     * 블랙리스트 등록 - 15분
     */
    public void addBlacklist(String username) {
        String key = BLACKLIST + username;
        long ttlSeconds = TimeUnit.MINUTES.toSeconds(15); // 15분 TTL

        // Redis에 등록 (15분 후 자동 삭제)
        redisTemplate.opsForValue().set(key, "LOCKED", ttlSeconds, TimeUnit.SECONDS);
    }

    /**
     * 블랙리스트 여부 조회
     */
    public boolean isBlacklist(String username) {
        String key = BLACKLIST + username;
        return redisTemplate.hasKey(key);
    }

    /**
     * 블랙리스트 잔여 시간 조회 (분 단위)
     */
    public long getBlacklistRemainSeconds(String username) {
        String key = BLACKLIST + username;
        Long ttl = redisTemplate.getExpire(key, TimeUnit.MINUTES);
        return ttl;
    }

    /**
     * 로그인 시도 횟수 조회
     */
    public Integer getLoginAttemptCount(String username) {
        String key = LOGIN_ATTEMPT_COUNT + username;
        Object object = redisTemplate.opsForValue().get(key);
        return object == null ? -1 : Integer.parseInt(object.toString());
    }

    /**
     * 로그인 시도 횟수 + 1
     */
    public void increaseLoginAttemptCount(String username) {
        if (existsRedis(username)) {
            String key = LOGIN_ATTEMPT_COUNT + username;
            redisTemplate.opsForValue().increment(key, 1);
        }
    }

    /**
     * 로그인 시도 횟수 TTL 시간 설정 (15분)
     */
    public void resetLoginAttemptTTL(String username) {
        String key = LOGIN_ATTEMPT_COUNT + username;
        long ttlSeconds = TimeUnit.MINUTES.toSeconds(15);
        redisTemplate.expire(key, ttlSeconds, TimeUnit.SECONDS);
    }

    /**
     * 레디스에서 제거
     */
    public void removeFromRedis(String username) {
        String key = LOGIN_ATTEMPT_COUNT + username;
        redisTemplate.delete(key);
    }

}
