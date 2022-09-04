package com.test.oauth_test.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // 프론트 -> 서버 로 인가코드 전송하면 받아오는 URI
    // 임시 URI 경로
    @GetMapping("/api/authorization_code")
    public AccessTokenRes getLogin(@RequestParam("code") String code) {

        // 인가코드 받았으니 이를 가지고 카카오서버에게 액세스 토큰 발급 요청
        AccessTokenRes accessTokenRes = userService.getAccessToken(code);

        return accessTokenRes;
    }
}
