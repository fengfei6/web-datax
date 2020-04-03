package com.feng.project.service;

import com.feng.project.domain.User;
import com.feng.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //@Cacheable(value = "user",unless = "#result==null")
    public User findUserByNameAndPass(String name,String pass){
       Optional<User> optionalUser = userRepository.findUserByNameAndPassword(name,pass);
       if(optionalUser.isPresent()){
           return optionalUser.get();
       }
       return null;
    }

    public void save(User user){
        userRepository.save(user);
    }

    public void updateUserPassByEmail(String email,String pass){
        userRepository.updateUserPassByEmail(email,pass);
    }
    
    public void update(User user) {
    	userRepository.save(user);
    }
    
    public void delete(Integer id) {
    	userRepository.deleteById(id);
    }
    
    public User getOne(Integer id) {
    	return userRepository.getOne(id);
    }
    
    public List<User> findAll(){
    	return userRepository.findAll();
    }
    
    public Integer modifyRole(Integer id,String role) {
    	return userRepository.modifyRole(id, role);
    }
    
    public Integer modifyPass(Integer id,String password) {
    	return userRepository.modifyPass(id, password);
    }
    
    public List<User> searchUsersByName(String name){
    	return userRepository.searchUsersByName(name);
    }

    public User findUserByName(String name){ return userRepository.findUserByName(name);}
}
