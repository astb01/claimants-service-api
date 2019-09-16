package com.tdd.claimantsservice.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth_users")
public class AppUser {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String password;

    public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
