package nuudelchin.club.web.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import nuudelchin.club.web.entity.PostEntity;
import nuudelchin.club.web.entity.UserPostEntity;

@Mapper
public interface PostRepository {

	List<UserPostEntity> get();
	
	int save(PostEntity entity);
}
