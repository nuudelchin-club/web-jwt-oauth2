package nuudelchin.club.web.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import nuudelchin.club.web.entity.ChatEntity;

@Mapper
public interface ChatRepository {

	ChatEntity findBySenderIdAndRecipientId(String senderId, String recipientId);
	
	List<ChatEntity> select();
	
	int save(ChatEntity entity);
}
