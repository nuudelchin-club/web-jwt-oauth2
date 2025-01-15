package nuudelchin.club.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPostDTO {

	private String username;
	private String fullname;
	private String email;    
    private String content;
    private String updatedAt;
    private String picture;
}
