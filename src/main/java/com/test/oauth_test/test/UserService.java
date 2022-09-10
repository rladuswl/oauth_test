package com.test.oauth_test.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 환경 변수
    @Value("${kakao.client-id}")
    String client_id;

    @Value("${kakao.secret}")
    String client_secret;

    @Value("${kakao.redirect-uri}")
    String redirect_uri;

    public AccessTokenRes getAccessToken(String code) {

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client_id);
        params.add("redirect_uri", redirect_uri);
        params.add("code", code);
        // params.add("client_secret", "{시크릿 키}"); // 우선 생략

        HttpEntity<MultiValueMap<String, String>> accessTokenRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                accessTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        AccessTokenRes accessTokenRes = null;
        try {
            accessTokenRes = objectMapper.readValue(accessTokenResponse.getBody(), AccessTokenRes.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return accessTokenRes;
    }

    // accessToken 으로 회원정보 요청 후 DB에 저장
    public void saveUser(String token) {

        // 카카오 서버로부터 회원정보 받아오기
        KakaoProfile profile = findProfile(token);

        User user = userRepository.findByEmail(profile.getKakao_account().getEmail());

        if(user == null) {
            user = User.builder()
                    .kakao_username(profile.getKakao_account().getProfile().getNickname())
                    .username(null)
                    .email(profile.getKakao_account().getEmail())
                    .profileImg(null)
                    .role("ROLE_USER").build();

            userRepository.save(user);
        }
    }

    public KakaoProfile findProfile(String token) {

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers);

        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoProfile;
    }
}
