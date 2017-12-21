package im.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import im.entity.WXConstant;
import im.util.HttpUtil;

@Controller
@RequestMapping("/oauth")
public class WX_SNS {

	@Value("${wss.server.appid}")
	private String appid;
	@Value("${wss.server.secret}")
	private String secret;

	@RequestMapping(value = "/base", method = RequestMethod.GET)
	public String Base(HttpServletRequest request, Model model,
			@RequestParam(value = "code", defaultValue = "") String code,
			@RequestParam(value = "state", defaultValue = "") String state) throws Exception {

		String url = WXConstant.WX_URL_GET_SNSCODE;
		url = url.replace("APPID",appid);
		url = url.replace("SECRET",	secret);
		url = url.replace("CODE", code);
		String r1 = HttpUtil.doGetJSON(url);
		JSONObject json = new JSONObject(r1);
		try {
			String access_token = json.getString("access_token");
			String openid = json.getString("openid");
			String url2 = WXConstant.WX_URL_GET_SNS_USERINFO;
			url2 = url2.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
			String r2 = HttpUtil.doGetJSON(url2);
			JSONObject user = new JSONObject(r2);

			String uopenid=user.getString("openid");
			String nickname = user.getString("nickname");
			String headimgurl=user.getString("headimgurl");
			
			model.addAttribute("nickname", nickname);
		} catch (Exception e) {
			// TODO: handle exception
		}
		model.addAttribute("state", state);
		return "/weixin/oauth/index";
	}
}
