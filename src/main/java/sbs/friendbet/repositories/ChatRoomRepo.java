package sbs.friendbet.repositories;

import org.springframework.data.repository.CrudRepository;
import sbs.friendbet.chat.ChatRoom;

public interface ChatRoomRepo extends CrudRepository<ChatRoom, Integer> {
    ChatRoom findByChatId(String chatRoomId);
}
