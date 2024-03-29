package com.devsuperior.dscatalog.DTO;
import com.devsuperior.dscatalog.entities.User;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class UserDTO implements Serializable {

    private Long id;
    @Size(min = 5, max = 20, message = "Campo deve ter entre 5 e 20 caracteres")
    @NotBlank(message ="Campo obrigatório")
    private String firstName;
    private String lastName;
    @Email(message = "Favor entrar com um email válido")
    private String email;

    private Set<RoleDTO> roles = new HashSet<>();

    public UserDTO() {
    }

    public UserDTO(User obj) {
        id = obj.getId();
        firstName = obj.getFirstName();
        lastName = obj.getLastName();
        email = obj.getEmail();
        obj.getRoles().forEach(x-> roles.add(new RoleDTO(x)));
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
