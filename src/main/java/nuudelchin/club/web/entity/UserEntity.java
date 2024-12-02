package nuudelchin.club.web.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserEntity {

    @Id
    private String username;
    
    private String fullname;

    private String email;

    private String role;
    
    @Lob
    private byte[] picture;
}
