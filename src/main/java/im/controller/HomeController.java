package im.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import im.dao.ClientInfoRepository;
import im.entity.ClientInfo;
import im.entity.Constant;
import im.entity.WXConstant;
import im.socket.LiveUserHandler;
import im.socket.MessageCachePool;
import im.util.EmojiFilter;
import im.util.HttpUtil;


@Controller
@RequestMapping("/")
public class HomeController {
	
	@Value("${wss.server.mainurl}")
	private String wssurl;
	@Value("${wss.server.appid}")
	private String appid;
	@Value("${wss.server.secret}")
	private String secret;
	
	@Autowired
	private MessageCachePool messageCachePool;
	@Autowired
	private ClientInfoRepository clientInfoRepository;
	@Autowired
	private LiveUserHandler liveUserHandler;
	
	@RequestMapping(value = "/zhangchengceshi", method = RequestMethod.GET)
	public String test(ModelMap map,HttpServletRequest request,
			@RequestParam(value = "code", defaultValue = "") String code) throws IOException{
		//判断cookie中是否存在用户信息
		ClientInfo clientInfo=clientInfoRepository.findClientByclientid(code);
		map.addAttribute("clientinfo",clientInfo);
		map.addAttribute("serverUrl", wssurl);
		map.addAttribute("historyMessage",messageCachePool.getMsgList());
		map.addAttribute("topInfo",messageCachePool.getTopInfo());
		map.addAttribute("totalclints",liveUserHandler.getLiveUser().getTotalvisiter());
		return "index";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(ModelMap map,HttpServletRequest request,
			@RequestParam(value = "openid", defaultValue = "") String uopenid,
			@RequestParam(value = "nickname", defaultValue = "") String nickname,
			@RequestParam(value = "headimgurl", defaultValue = "") String headimgurl) throws IOException{
		//判断cookie中是否存在用户信息
		System.out.println("----------------------------------");
		System.out.println(nickname);
		ClientInfo clientInfo=checkClientInfo(uopenid,nickname,headimgurl);
		map.addAttribute("serverUrl", wssurl);
		map.addAttribute("clientinfo",clientInfo);
		map.addAttribute("historyMessage",messageCachePool.getMsgList());
		map.addAttribute("topInfo",messageCachePool.getTopInfo());
		map.addAttribute("totalclints",liveUserHandler.getLiveUser().getTotalvisiter());
		return "index";
	}
	
	
	@RequestMapping(value = "/oauth", method = RequestMethod.GET)
	public String Base(HttpServletRequest request, ModelMap map,
			@RequestParam(value = "code", defaultValue = "") String code,
			@RequestParam(value = "state", defaultValue = "") String state) throws Exception {
		ClientInfo clientInfo=null;
		//微信授权开始
		//获取授权码accesstoken
		String url = WXConstant.WX_URL_GET_SNSCODE;
		url = url.replace("APPID",appid);
		url = url.replace("SECRET",	secret);
		url = url.replace("CODE", code);
		String r1 = HttpUtil.doGetJSON(url);
		JSONObject json = new JSONObject(r1);
		try {
			String access_token = json.getString("access_token");
			String openid = json.getString("openid");
			//通过accesstoken获取用户信息
			String url2 = WXConstant.WX_URL_GET_SNS_USERINFO;
			url2 = url2.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
			String r2 = HttpUtil.doGetJSON(url2);
			JSONObject user = new JSONObject(r2);

			//根据所返回的微信用户信息登录
			String uopenid=user.getString("openid");
			String nickname = user.getString("nickname");
			String headimgurl=user.getString("headimgurl");
			clientInfo=checkClientInfo(uopenid,nickname,headimgurl);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		//返回页面信息
		//ws链接
		map.addAttribute("serverUrl", wssurl);
		if(clientInfo!=null){
//			map.addAttribute("clientId", clientInfo.getClientid());
//			map.addAttribute("level",clientInfo.getLevel());
//			map.addAttribute("status",clientInfo.getStatus());
//			map.addAttribute("ssid",clientInfo.getOpenId());
//			map.addAttribute("headImgUrl",clientInfo.getHeadImg());
		}else{
			clientInfo=new ClientInfo();
		}
		map.addAttribute("clientinfo",clientInfo);
		map.addAttribute("historyMessage",messageCachePool.getMsgList());
		map.addAttribute("topInfo",messageCachePool.getTopInfo());
		return "index";
	}
	
	private ClientInfo checkClientInfo(String openid,String nickname,String headimgurl){
		ClientInfo clientInfo=clientInfoRepository.findClientByOpenId(openid);
		//用户首次登录
		if(clientInfo==null){
			clientInfo=new ClientInfo();
			clientInfo.setClientid(EmojiFilter.filterEmoji(nickname));
			clientInfo.setHeadImg(headimgurl);
			clientInfo.setCreateDate(new Date());
			clientInfo.setLevel(Constant.CUSTOMER_LEVEL);
			clientInfo.setOpenId(openid);
			clientInfo.setStatus(Constant.STATUS_NORMAL);
		}else{
			//不是首次登录更新头像
			if(headimgurl.equals(clientInfo.getHeadImg())||clientInfo.getLevel()==Constant.TEACHER_LEVEL||clientInfo.getLevel()==Constant.ADMIN_LEVEL){
				
			}else{
				clientInfo.setHeadImg(headimgurl);
			}
		}
		clientInfo=clientInfoRepository.save(clientInfo);
		if(clientInfo==null){
			clientInfo=new ClientInfo();
		}
		return clientInfo;
	}
}
