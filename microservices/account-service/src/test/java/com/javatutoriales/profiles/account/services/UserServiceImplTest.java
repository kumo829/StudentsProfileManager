package com.javatutoriales.profiles.account.services;

import com.javatutoriales.profiles.account.persistence.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class UserServiceImplTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;


}