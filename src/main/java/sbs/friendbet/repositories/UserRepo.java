package sbs.friendbet.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import sbs.friendbet.data.User;

import java.util.List;


public interface UserRepo extends CrudRepository<User, Integer> {
    User findByUsername(String username);
    User findById(int id);

    @Query("SELECT U FROM User U WHERE U.name LIKE %?1% ")
    List<User> searchForFriendByName(String friendName);

}
