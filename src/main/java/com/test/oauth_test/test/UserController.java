package com.test.oauth_test.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // 프론트 -> 서버 로 인가코드 전송하면 받아오는 URI
    // 임시 URI 경로
    // https://kauth.kakao.com/oauth/authorize?client_id=b9f6eaeb47ed2f08476461345671880c&redirect_uri=http://52.78.4.217:8080/api/authorization_code&response_type=code
    // https://kauth.kakao.com/oauth/authorize?client_id=b9f6eaeb47ed2f08476461345671880c&redirect_uri=http://localhost:8080/api/authorization_code&response_type=code
    @GetMapping("/api/authorization_code")
    public ResponseEntity getLogin(@RequestParam("code") String code) {

        // 인가코드 받았으니 이를 가지고 카카오서버에게 액세스 토큰 발급 요청
        AccessTokenRes accessTokenRes = userService.getAccessToken(code);

        // 액세스 토큰 발급 완료

        // 발급 받은 accessToken 으로 카카오 서버에 회원정보 요청 후 DB에 저장
        String jwtToken = userService.saveUser(accessTokenRes.getAccess_token());

        System.out.println("access_token : " + accessTokenRes.getAccess_token());
        System.out.println("jwtToken : Bearer " + jwtToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        return ResponseEntity.ok().headers(headers).body("success");
    }

    @GetMapping("/api/code")
    public ResponseEntity code(@RequestParam("code") String code) {
        return ResponseEntity.ok().body("success");
    }

    // 인가코드 과정 없이 바로 액세스코드 받아오기
    @GetMapping("/api/access_token")
    public ResponseEntity getToken(@RequestParam("token") String token) {

        String jwtToken = userService.saveUser(token);

        System.out.println("jwtToken : Bearer " + jwtToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        return ResponseEntity.ok().headers(headers).body("success");
    }

    @GetMapping("/test")
    public ResponseEntity test(@RequestParam("name") String name) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, "토큰토큰토큰");
        return ResponseEntity.ok().headers(headers).body(name + " hello!!!!!!!!!!!!");
    }

    @GetMapping("/auth/test")
    public String authTest(Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return principalDetails.getUser().getUsername() + principalDetails.getUser().getEmail();
    }
}
