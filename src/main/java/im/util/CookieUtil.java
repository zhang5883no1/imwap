package im.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {

	public static Cookie createCookie(String name,String value,int exptime){
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(exptime);
		cookie.setPath("/");
		return cookie;
	}
	
	public static String getCookie(HttpServletRequest request,String name){
		Cookie[] cookies = request.getCookies();//这样便可以获取一个cookie数组
		if(cookies!=null){
			for(Cookie cookie : cookies){
				if(name.equals(cookie.getName())){
					return cookie.getValue(); // get the cookie value
				}
			}
		}
		return "";
	}
}
