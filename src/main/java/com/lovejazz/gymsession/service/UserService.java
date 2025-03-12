package com.lovejazz.gymsession.service;

import com.lovejazz.gymsession.repository.RoleRepository;
import com.lovejazz.gymsession.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private  final UserRepository userRepository;
    private  final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    


}
