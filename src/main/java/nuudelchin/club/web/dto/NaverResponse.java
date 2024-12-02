package nuudelchin.club.web.dto;

import java.util.Map;

public class NaverResponse implements OAuth2Response{

    private final Map<String, Object> attribute;

    public NaverResponse(Map<String, Object> attribute) {

        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {

        return "naver";
    }

    @Override
    public String getProviderId() {

        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {

        return attribute.get("email").toString();
    }

    @Override
    public String getName() {

        return attribute.get("name").toString();
    }
    
    @Override
    public String getPictureURL() {

    	if(attribute.containsKey("profile_image")) {
    		return attribute.get("profile_image").toString();	
    	} 
    	return null;
    }
    
    @Override
    public String getUsername() {
    	
    	String provider = this.getProvider();
        String providerId = this.getProviderId();
        String username = provider + " " + providerId;
        return username;
    }
}
