package nuudelchin.club.web.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class PostSelectEntity {
	
    private String content;
    private String updated_at;
    private String author;
//    private String email;
    
    @Lob
    private byte[] picture;
    
    private String pictureSrc;
}
