package im.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class HttpUtil {
	private final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
    static RestTemplate restTemplate;
	
	public static String doGetJSON(String url){
		ResponseEntity<String> entity=restTemplate.getForEntity(url, String.class);
		if(entity.getStatusCodeValue()==200){
			return restTemplate.getForEntity(url, String.class).getBody();
		}else{
			return "";
		}
	}
	
}
