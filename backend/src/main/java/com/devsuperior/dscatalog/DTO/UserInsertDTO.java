package com.devsuperior.dscatalog.DTO;

import com.devsuperior.dscatalog.entities.User;

public class UserInsertDTO extends UserDTO{

    private String password;

    public UserInsertDTO() {
        super();
    }

    public UserInsertDTO(User obj, String password) {
        super(obj);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
