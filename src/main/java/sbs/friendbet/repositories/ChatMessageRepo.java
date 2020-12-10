package sbs.friendbet.repositories;

import org.springframework.data.repository.CrudRepository;
import sbs.friendbet.data.ChatMessage;

import java.util.List;

public interface ChatMessageRepo extends CrudRepository<ChatMessage, Integer> {
    List<ChatMessage> findAllByChatId(String chatId);
}
