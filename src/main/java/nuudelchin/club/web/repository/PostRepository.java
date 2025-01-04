package nuudelchin.club.web.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import nuudelchin.club.web.entity.PostEntity;

@Mapper
public interface PostRepository {

	List<PostEntity> getAllList();
	
	int save(PostEntity PostEntity);
}
