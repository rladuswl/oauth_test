package com.test.oauth_test.test;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "kakao_username")
    private String kakao_username; // kakao Nickname

    @Column(name = "username")
    @Nullable
    private String username; // 서비스 내 username

//    @Column(name = "password")
//    private String password; // 자체 로그인은 사용하지 않고 오로지 oauth로만 할거라 password 필요 x

    @Column(name = "email")
    private String email; // kakao Email

    @Column(name = "profile_img")
    @Nullable
    private String profileImg; // profileImg

    private String role;

    // private String oauth; // kakao, google, facebook 등 여러 oauth 구현할 때 해당 필드로 구분

    @Column(name = "create_time")
    @CreationTimestamp
    private Timestamp createTime;

    @Builder
    public User(String kakao_username, String username, String email, String profileImg, String role) {
        this.kakao_username = kakao_username;
        this.username = username;
        this.email = email;
        this.profileImg = profileImg;
        this.role = role;
    }
}
