package sbs.friendbet.repositories;

import org.springframework.data.repository.CrudRepository;
import sbs.friendbet.data.User;


public interface UserRepo extends CrudRepository<User, Integer> {
    User findByUsername(String username);
    User findById(int id);
}
