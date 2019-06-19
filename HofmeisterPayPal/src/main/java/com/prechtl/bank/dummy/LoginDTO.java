package com.prechtl.bank.dummy;

import de.hofmeister.entity.util.StringIdEntity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public class LoginDTO extends StringIdEntity {
    @Transient
    private String loginId;
    @Transient
    private String password;

    public LoginDTO () {

    }

    public LoginDTO(String loginId, String password){
        this.loginId = loginId;
        this.password = password;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
