package com.devsuperior.dscatalog.DTO;


import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.services.validation.UserUpdateValid;

@UserUpdateValid
public class UserUpdateDTO extends UserDTO  {

    public UserUpdateDTO() {
    }

    public UserUpdateDTO(User obj) {
        super(obj);
    }
}
