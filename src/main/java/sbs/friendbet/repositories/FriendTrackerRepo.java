package sbs.friendbet.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import sbs.friendbet.data.FriendTracker;

import java.util.List;

public interface FriendTrackerRepo extends CrudRepository<FriendTracker, Integer> {

    List<FriendTracker> findAllByUserId(int userId);

    @Query("SELECT ft from FriendTracker ft where ft.user.id = ?1 and ft.pending = true ")
    List<FriendTracker> findPendingFriendRequest(int userId);

    @Query("SELECT ft from FriendTracker ft where ft.user.id = ?1 and ft.pending = false ")
    List<FriendTracker> findAllFriends(int userId);

}
