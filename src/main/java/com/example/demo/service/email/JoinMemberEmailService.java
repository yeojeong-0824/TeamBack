package com.example.demo.service.email;

import com.example.demo.config.exception.RequestDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 회원가입에 대한 Email 전송
 */
@Service
@RequiredArgsConstructor
public class JoinMemberEmailService {

    private final EmailService emailService;

    // ToDo: Map이라는 자료구조가 해당 로직에 적당한지 여부를 고민해 봐야 함
    // ToDo: 이메일 전송 시 유효시간을 만들어야 함, 어떤 식으로 로직을 작성 할 지 고민을 해봐야 할 것 같음

    private Map<String, String> emailAuthedKeyMap = new HashMap<>();
    public void sendAuthedEmail(String email, String key) {
        String title = "회원가입 이메일 인증 코드";
        String text = "이메일 인증 코드: " + key;

        emailAuthedKeyMap.put(email, key);
        emailService.sendEmail(email, title, text);
    }

    // 회원가입 인증 이메일의 값이 Authed라면 인증된 사용자라는 것으로 간주함
    public boolean checkAuthedEmail(String email) {

        String savedKey = emailAuthedKeyMap.get(email);
        if(savedKey == null) return false;
        if(!savedKey.equals("Authed")) return false;

        emailAuthedKeyMap.remove(email);
        return true;
    }

    // 회원가입에 인증 이메일의 Key 값과 해당 이메일에 발송 된 Key 값이 같다면 해당 이메일의 값을 Authed로 변경
    public boolean checkAuthedKey(String email, String key) {

        String savedKey = emailAuthedKeyMap.get(email);
        if(savedKey == null) throw new RequestDataException("잘못된 이메일에 대한 요청입니다.");
        if(!savedKey.equals(key)) return false;

        emailAuthedKeyMap.put(email, "Authed");
        return true;
    }

    // 회원가입 인증 이메일을 전송 할 때 임의의 4자리 수를 만들고 이 값을 Map 자료구조에 저장
    public String createAuthedKey() {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis()); // 시스템 시간을 기준으로 난수를 설정하는 부분의 Seed가 적당한지 모르겠음

        int rInt = random.nextInt(10000);
        return String.format("%04d", rInt);
    }
}
