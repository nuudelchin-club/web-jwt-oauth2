package nuudelchin.club.web.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import nuudelchin.club.web.entity.PostEntity;
import nuudelchin.club.web.entity.PostSelectEntity;

@Mapper
public interface PostRepository {

	List<PostSelectEntity> selectPostList();
	
	int save(PostEntity PostEntity);
}
