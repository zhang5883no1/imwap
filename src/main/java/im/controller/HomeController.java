package im.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import im.socket.MessageCachePool;
import im.util.CookieUtil;


@Controller
@RequestMapping("/")
public class HomeController {
	
	@Value("${wss.server.mainurl}")
	private String wssurl;
	
	@Autowired
	private MessageCachePool messageCachePool;
	@Autowired
	private ClientInfoRepository clientInfoRepository;
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(ModelMap map,HttpServletRequest request) throws IOException{
		String clientId=CookieUtil.getCookie(request, Constant.COOKIE_CLIENTID);
		String pwd=CookieUtil.getCookie(request, Constant.COOKIE_PWD);
		ClientInfo clientInfo=clientInfoRepository.findClientByClientidAndPassword(clientId, pwd);
		//判断cookie中是否存在用户信息
		map.addAttribute("serverUrl", wssurl);
		if(clientInfo!=null){
			map.addAttribute("clientId", clientInfo.getClientid());
			map.addAttribute("level",clientInfo.getLevel());
			map.addAttribute("status",clientInfo.getStatus());
		}else{
			map.addAttribute("clientId", "");
			map.addAttribute("level", 0);
			map.addAttribute("status",1);
		}
		map.addAttribute("historyMessage",messageCachePool.getMsgList());
		map.addAttribute("topInfo",messageCachePool.getTopInfo());
		return "index";
	}
	
	/**
	 * 
	* @param request
	* @param name
	* @param pwd
	* @return 1 succes 2 username error 3 pwd error  0 error
	* @throws IOException
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	@ResponseBody
	public String login(HttpServletRequest request,@RequestParam(value = "name", defaultValue = "") String name,@RequestParam(value = "pwd", defaultValue = "") String pwd) throws IOException{
		ClientInfo clientInfo=clientInfoRepository.findClientByclientid(name);
		if(clientInfo==null){
			return "2";
		}
		if(!pwd.equals(clientInfo.getPassword())){
			return "3";
		}
		if(name.equals(clientInfo.getClientid())&&pwd.equals(clientInfo.getPassword())){
			return "1";
		}
		return "0";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(ModelMap map,HttpServletRequest request,HttpServletResponse response) throws IOException{
		String userName=request.getParameter("userName");
		String pwd=request.getParameter("pwd");
		ClientInfo clientInfo=clientInfoRepository.findClientByClientidAndPassword(userName, pwd);
		if(clientInfo!=null){
			response.addCookie(CookieUtil.createCookie(Constant.COOKIE_CLIENTID, clientInfo.getClientid(), Constant.COOKIE_EXPTIME));
			response.addCookie(CookieUtil.createCookie(Constant.COOKIE_PWD, clientInfo.getPassword(), Constant.COOKIE_EXPTIME));
			response.sendRedirect("/");
		}else{
			response.sendRedirect("/?error");
		}
	}
	
}
