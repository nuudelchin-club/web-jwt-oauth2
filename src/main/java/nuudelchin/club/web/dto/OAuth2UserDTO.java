package nuudelchin.club.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuth2UserDTO {
    
    private String username;
    private String fullname;
    private String email;
    private String pictureUrl;
    private String role;
    
}
