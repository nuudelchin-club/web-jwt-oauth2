package nuudelchin.club.web.dto;

public interface OAuth2Response {

	
    String getProvider();
    
    String getProviderId();
    
    String getEmail();
    
    String getName();
    
    String getPictureURL();
    
    String getUsername();
}
