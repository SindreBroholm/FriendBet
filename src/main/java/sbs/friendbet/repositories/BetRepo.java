package sbs.friendbet.repositories;

import org.springframework.data.repository.CrudRepository;
import sbs.friendbet.data.Bet;

public interface BetRepo extends CrudRepository<Bet, Integer> {

}
