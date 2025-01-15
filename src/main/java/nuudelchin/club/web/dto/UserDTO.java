package nuudelchin.club.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	
    private String username;    
    private String fullname;
    private String email;
    private String role;
    private String picture;
}
