package sbs.friendbet.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import sbs.friendbet.data.FriendTracker;

import java.util.List;

public interface FriendTrackerRepo extends CrudRepository<FriendTracker, Integer> {

    @Query(value = "SELECT * from friendbet.friend_tracker as ft where friend_id = ?1 having pending = true", nativeQuery = true)
    List<FriendTracker> findPendingFriendRequest(int userId);

    @Query(value = "SELECT * from friendbet.friend_tracker as ft where user_id = ?1 or friend_id = ?1 having pending = false ", nativeQuery = true)
    List<FriendTracker> findAllFriends(int userId);


    FriendTracker findByFriendshipId(String friendshipId);
}
