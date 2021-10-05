package com.devsuperior.dscatalog.services;
import com.devsuperior.dscatalog.DTO.*;
import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable){
        Page<User> page = repository.findAll(pageable);
        return page.map(x -> new UserDTO(x));
    }

    @Transactional(readOnly = true)
    public UserDTO findByID(Long id){
        Optional<User> user = repository.findById(id);
        User obj =  user.orElseThrow(() -> new ResourceNotFoundException("Id não existe"));
        return new UserDTO(obj);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO obj){
        User user = new User();
        fromDTO(user, obj);
        user.setPassword(bCryptPasswordEncoder.encode(obj.getPassword()));
        user = repository.save(user);
        return new UserDTO(user);
    }

    @Transactional
    public UserUpdateDTO update(Long id, UserUpdateDTO obj) {
        try {
            User user = repository.getOne(id);
            fromDTO(user,obj);
            user = repository.save(user);
            return new UserUpdateDTO(user);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
    }

    public void delete(Long id){
        try{
            repository.deleteById(id);
        } catch(DataIntegrityViolationException e){
            throw new DatabaseException("Não é possível deletar esse usuário");
        }
        catch(EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
    }

    private void fromDTO(User user, UserDTO obj) {
        user.setFirstName(obj.getFirstName());
        user.setLastName(obj.getLastName());
        user.setEmail(obj.getEmail());
        user.getRoles().clear();
        for(RoleDTO roleDTO : obj.getRoles()){
            user.getRoles().add(roleRepository.getOne(roleDTO.getId()));
        }
    }
}
