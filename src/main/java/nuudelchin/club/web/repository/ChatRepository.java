package nuudelchin.club.web.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import nuudelchin.club.web.entity.ChatEntity;
import nuudelchin.club.web.entity.UserChatEntity;

@Mapper
public interface ChatRepository {

	ChatEntity findBySenderIdAndRecipientId(String senderId, String recipientId);
	
	List<UserChatEntity> selectBySenderId(String senderId);
	
	int save(ChatEntity entity);
}
