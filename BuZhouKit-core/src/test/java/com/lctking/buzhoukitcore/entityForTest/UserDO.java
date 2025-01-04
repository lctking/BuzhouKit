package com.lctking.buzhoukitcore.entityForTest;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;

@Getter
@Setter
@Builder
@Accessors(chain = true)
public class UserDO {
    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;


    private String phone;

    @Override
    public String toString() {
        return "UserDO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDO userDO = (UserDO) o;
        return Objects.equals(id, userDO.id) && Objects.equals(username, userDO.username) && Objects.equals(password, userDO.password) && Objects.equals(phone, userDO.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, phone);
    }
}
