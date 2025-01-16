package nuudelchin.club.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserChatDTO {

	private String fullname;
    private String picture;
    private String chatId;
    private String senderId;
    private String recipientId;
}
