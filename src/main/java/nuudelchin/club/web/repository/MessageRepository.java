package nuudelchin.club.web.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import nuudelchin.club.web.entity.MessageEntity;

@Mapper
public interface MessageRepository {

	List<MessageEntity> findByChatId(String chatId);
	
	int save(MessageEntity entity);
}
