package com.rajat.caching.services;

import com.rajat.caching.entities.User;
import com.rajat.caching.interfaces.InbuiltCacheInterface;
import com.rajat.caching.repository.UserRepository;
import com.rajat.caching.utils.LFUCache;
import com.rajat.caching.utils.LRUCache;
import com.rajat.caching.utils.MFUCache;
import com.rajat.caching.utils.MRUCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Value("${eviction.method}")
    private String ev_method;

    @Value("${cache.capacity}")
    private int capacity;

    private InbuiltCacheInterface<User> cacheSelected;

    public void initializeCache(){
        log.info("The selected Caching technique is  : " + ev_method);
        switch (ev_method){
            case("LFU"):
                this.cacheSelected = new LFUCache<>(capacity);
                break;
            case("MFU"):
                this.cacheSelected = new MFUCache<>(capacity);
            case("LRU"):
                this.cacheSelected = new LRUCache<>(capacity);
            case("MRU"):
                this.cacheSelected = new MRUCache<>(capacity);
        }
    }

    public User saveUser(User user){
        synchronized (cacheSelected){
            User savedUser =  userRepository.save(user);
            cacheSelected.putValue(savedUser.getId(),savedUser);
            return savedUser;
        }
    }

    public User getUserById(int userID) {
        synchronized (cacheSelected){
            // First check the user in the cache
            User cachedUser = cacheSelected.getValue(userID);
            // If user is not in cache
            if(cachedUser == null){
                // Get the user from DB
                User dbUser = userRepository.findById(userID);
                // Update the user in cache
                cacheSelected.putValue(dbUser.getId(),dbUser);
                return dbUser;
            }else{
                return cachedUser;
            }
        }
    }
}
