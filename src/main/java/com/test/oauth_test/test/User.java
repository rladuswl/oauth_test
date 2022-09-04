package com.test.oauth_test.test;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") //(3)
    private Long id;

    @Column(name = "username")
    private String username;

//    @Column(name = "password")
//    private String password; // 자체 로그인은 사용하지 않고 오로지 oauth로만 할거라 password 필요 x

    @Column(name = "email")
    private String email;

    private String role;

    // private String oauth; // kakao, google, facebook 등 여러 oauth 구현할 때 해당 필드로 구분

    @Column(name = "create_time")
    @CreationTimestamp
    private Timestamp createTime;

    @Builder
    public User(Long id, String username, String email, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
