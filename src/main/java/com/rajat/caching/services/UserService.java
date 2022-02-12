package com.rajat.caching.services;

import com.rajat.caching.entities.User;
import com.rajat.caching.repository.UserRepository;
import com.rajat.caching.utils.LFUCache;
import com.rajat.caching.utils.LRUCache;
import com.rajat.caching.utils.MRUCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    @Autowired
    UserRepository userRepository;

    LRUCache<User> lruCache = new LRUCache<>(4);
    MRUCache<User> mruCache = new MRUCache<>(2);
    LFUCache<User> lfuCache = new LFUCache<>(2);

    public User saveUser(User user){
        synchronized (lfuCache){
            User savedUser =  userRepository.save(user);
            lfuCache.putValue(savedUser.getId(),savedUser);
            return savedUser;
        }
    }

    public User getUserById(int userID) {
        synchronized (lfuCache){
            // First check the user in the cache
            User cachedUser = lfuCache.getValue(userID);
            // If user is not in cache
            if(cachedUser == null){
                // Get the user from DB
                User dbUser = userRepository.findById(userID);
                // Update the user in cache
                lfuCache.putValue(dbUser.getId(),dbUser);
                return dbUser;
            }else{
                return cachedUser;
            }
        }
    }
}
