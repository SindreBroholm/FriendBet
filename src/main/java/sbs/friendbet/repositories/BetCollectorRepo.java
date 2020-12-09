package sbs.friendbet.repositories;

import org.springframework.data.repository.CrudRepository;
import sbs.friendbet.data.BetCollector;

import java.util.List;

public interface BetCollectorRepo extends CrudRepository<BetCollector, Integer> {

    List<BetCollector> findAllByUserId(int userId);
    List<BetCollector> findAllByAgainstUser(int userId);
}
