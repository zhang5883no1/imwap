package im.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import im.dao.MessageInfoRepository;
import im.entity.Constant;
import im.entity.MessageInfo;
import im.socket.MessageCachePool;

@RestController
@RequestMapping("/service")
public class AjaxController {

	@Autowired
	MessageInfoRepository infoRep;
	@Autowired
	MessageCachePool msgCachePool;
	
	/**
	 * 获取聊天信息
	* @param type 1管理员 2所有
	* @param page
	* @return
	* @throws IOException
	 */
	@RequestMapping(value = "/getChats", method = RequestMethod.GET)
	public List<MessageInfo> getChats(@RequestParam(value = "type", defaultValue = "0") int type
			,@RequestParam(value = "page", defaultValue = "0") int page) throws IOException{
		if(type==0){
			return null;
		}
		if(page>10){
			return null;
		}
		List<MessageInfo> resultList=new LinkedList<MessageInfo>();
		if(page<=2){
			resultList=getMessageInfoFromCache(type,page);
		}else{
			resultList=getMessageInfoFromDB(type,page);
		}
		return resultList;
	}
	
	/**
	 * 从缓存中获取信息
	* @param type
	* @param page
	* @return
	 */
	private List<MessageInfo> getMessageInfoFromCache(int type,int page){
		LinkedList<MessageInfo> resultList=new LinkedList<MessageInfo>();
		//管理员信息
		if(type==1){
			resultList=setMsgInfo(resultList,msgCachePool.getAdminmsgList(),page);
		//所有信息
		}else if(type==2){
			resultList=setMsgInfo(resultList,msgCachePool.getMsgList(),page);
		}
		return resultList;
	}
	
	/**
	 * 从数据库读取信息
	* @param type
	* @param page
	* @return
	 */
	private List<MessageInfo> getMessageInfoFromDB(int type,int page){
		Page<MessageInfo> list = null;
		Sort sort = new Sort(Sort.Direction.DESC, "times");  
		Pageable pageable = new PageRequest(page, 15, sort); 
		if(type==1){
			list=infoRep.findByLevel(Constant.ADMIN_LEVEL, pageable);
		}else if(type==2){
			list=infoRep.findAll(pageable);
		}else{
			return null;
		}
		return list.getContent();
	}
	
	private LinkedList<MessageInfo> setMsgInfo(LinkedList<MessageInfo> resultList ,LinkedList<MessageInfo> msglist,int page){
		try {
			for(int i=page*15;i<(page+1)*15;i++){
				resultList.addFirst(msglist.get(i));
			}
		} catch (Exception e) {
			// TODO: handle exception
			return resultList;
		}
		return resultList;
	}
}
