package im.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import im.dao.ClientInfoRepository;
import im.entity.ClientInfo;
import im.socket.MessageCachePool;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private ClientInfoRepository clientInfoRepository;
	@Value("${file.upload.banner.path}")
	private String uploadPath;
	@Autowired
	MessageCachePool msgpool;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() throws IOException{
		return "admins/login";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String login(HttpServletRequest request) throws IOException{
		String name=request.getParameter("userCode");
		String passwd=request.getParameter("passwd");
		if("woshiadmin".equals(name)&&"adminshiwo".equals(passwd)){
			request.getSession().setAttribute("islogin", true);
			return "admins/index";
		}
		return "admins/login?error";
	}
	
	@RequestMapping(value = "/client/list", method = RequestMethod.GET)
	public String index(ModelMap map,HttpServletRequest request,@RequestParam(value = "name", defaultValue = "") String name,@RequestParam(value = "page", defaultValue = "1") int page) throws IOException{
		if(request.getSession().getAttribute("islogin")==null){
			return "admins/login";
		}
		Sort sort = new Sort(Sort.Direction.DESC, "createDate");  
		Pageable pageable = new PageRequest(page-1, 20, sort); 
		if("".equals(name)){
			Page<ClientInfo> list=clientInfoRepository.findAll(pageable);
			map.addAttribute("list",list);
		}else{
			Page<ClientInfo> list=clientInfoRepository.findClientByclientid(name,pageable);
			map.addAttribute("list",list);
		}
		map.addAttribute("name",name);
		return "admins/clientList";
	}
	
	@RequestMapping(value = "/client/{clientId}", method = RequestMethod.GET)
	public String getinfo(ModelMap map,@PathVariable String clientId,HttpServletRequest request) throws IOException{
		if(request.getSession().getAttribute("islogin")==null){
			return "admins/login";
		}
		ClientInfo info;
		if("add".equals(clientId)){
			info=new ClientInfo();
		}else{
			info=clientInfoRepository.findClientByclientid(clientId);
		}
		map.addAttribute("info",info);
		return "admins/client";
	}
	
	@RequestMapping(value = "/client/update", method = RequestMethod.POST)
	public void update(ModelMap map,HttpServletRequest request,HttpServletResponse reponse) throws IOException{
		if(request.getSession().getAttribute("islogin")==null){
			reponse.sendRedirect("/admin/");
		}
		String clientid=request.getParameter("clientId");
		String password=request.getParameter("password");
		String level =request.getParameter("level");
		String status =request.getParameter("status");
		String mobile =request.getParameter("mobile");
		ClientInfo info=new ClientInfo();
		info.setClientid(clientid);
		info.setLevel(Short.valueOf(level));
		info.setPassword(password);
		info.setStatus(Short.valueOf(status));
		info.setMobile(mobile);
		clientInfoRepository.save(info);
		reponse.sendRedirect("/admin/client/list");
	}
	
	
	@RequestMapping(value = "/banner", method = RequestMethod.GET)
	public String getBanner(HttpServletRequest request) throws IOException{
		if(request.getSession().getAttribute("islogin")==null){
			return "admins/login";
		}
		return "admins/banner";
	}
	
	@RequestMapping(value = "/banner/update", method = RequestMethod.POST)
	@ResponseBody
	public String uploadPic(HttpServletRequest request) throws IOException {
		if(request.getSession().getAttribute("islogin")==null){
			return "admins/login";
		}
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
		String name=request.getParameter("filename");
		MultipartFile file = null;
		BufferedOutputStream stream = null;
		for (int i = 0; i < files.size(); ++i) {
			file = files.get(i);
			if (!file.isEmpty()) {
				try {
					String uploadFilePath = file.getOriginalFilename();
					System.out.println("uploadFlePath:" + uploadFilePath);
					// 截取上传文件的文件名
					String uploadFileName = uploadFilePath.substring(uploadFilePath.lastIndexOf('\\') + 1,
							uploadFilePath.indexOf('.'));
					System.out.println("multiReq.getFile()" + uploadFileName);
					// 截取上传文件的后缀
					String uploadFileSuffix = uploadFilePath.substring(uploadFilePath.indexOf('.') + 1,
							uploadFilePath.length());
					System.out.println("uploadFileSuffix:" + uploadFileSuffix);
					
					System.out.println(uploadPath+name+".jpg");
					stream = new BufferedOutputStream(new FileOutputStream(
							new File(uploadPath+name+".jpg")));
					byte[] bytes = file.getBytes();
					stream.write(bytes, 0, bytes.length);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (stream != null) {
							stream.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				System.out.println("上传文件为空");
			}
		}
		System.out.println("文件接受成功了");
		return "success";
	}
	
}
