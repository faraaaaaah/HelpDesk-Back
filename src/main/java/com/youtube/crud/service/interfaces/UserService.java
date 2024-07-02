package com.youtube.crud.service.interfaces;

import com.youtube.crud.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {
    User create(User user);
    List<User> getAllUsers() ;
    User getUserById(Long id);
    User update(User user,Long id);
    void delete(Long id);
}
