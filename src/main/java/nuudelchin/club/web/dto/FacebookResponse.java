package nuudelchin.club.web.dto;

import java.util.Map;

public class FacebookResponse implements OAuth2Response{

    private final Map<String, Object> attribute;

    public FacebookResponse(Map<String, Object> attribute) {

        this.attribute = attribute;
    }

    @Override
    public String getProvider() {

        return "facebook";
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

    	if(attribute.containsKey("picture")) {
    		return attribute.get("picture").toString();	
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
