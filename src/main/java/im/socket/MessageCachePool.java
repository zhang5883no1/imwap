package im.socket;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import im.dao.LiveClientRepository;
import im.dao.MessageInfoRepository;
import im.entity.Constant;
import im.entity.LiveUser;
import im.entity.MessageInfo;

@Component
public class MessageCachePool {
	@Value("${message.cache.pool.size}")
	private int msgSize;
	
	@Autowired MessageInfoRepository messageDao;
	private LinkedList<MessageInfo> msgList=new LinkedList<MessageInfo>();
	
	private LinkedList<MessageInfo> adminmsgList=new LinkedList<MessageInfo>();
	
	private MessageInfo topInfo=new MessageInfo();
	
	private MessageInfo scrolInfo=new MessageInfo();
	
	public LinkedList<MessageInfo> getMsgList() {
		return msgList;
	}
	
	public LinkedList<MessageInfo> getAdminmsgList() {
		return adminmsgList;
	}

	public MessageInfo getTopInfo() {
		return topInfo;
	}

	public void setTopInfo(MessageInfo topInfo) {
		this.topInfo = topInfo;
	}

	public MessageInfo getScrolInfo() {
		return scrolInfo;
	}

	public void setScrolInfo(MessageInfo scrolInfo) {
		this.scrolInfo = scrolInfo;
	}
	
	public void addAdminMessage(MessageInfo msginfo){
		if(adminmsgList.size()>=msgSize){
			adminmsgList.removeLast();
		}
		adminmsgList.addFirst(msginfo);
	}
	
	public void addAllMessageInfo(MessageInfo msginfo){
		if(msgList.size()>=msgSize){
			msgList.removeLast();
		}
		msgList.addFirst(msginfo);
	}

	//新消息最前
	public void addMessageInfo(MessageInfo msginfo){
		if(Constant.MSG_TYPE_SCROL.equals(msginfo.getMsgType())){
			setScrolInfo(msginfo);
		}else if(Constant.MSG_TYPE_TOP.equals(msginfo.getMsgType())){
			setTopInfo(msginfo);
		}else{
			if(msgList.size()>=msgSize){
				msgList.removeLast();
			}
			msgList.addFirst(msginfo);
		}
		if(msginfo.getLevel().equals(Constant.TEACHER_LEVEL)){
			addAdminMessage(msginfo);
		}
		messageDao.save(msginfo);
	}
	
	public void initMsg(){
		Sort sort = new Sort(Sort.Direction.DESC, "times");  
		Pageable pageable = new PageRequest(0, msgSize, sort); 
		Page<MessageInfo> page=messageDao.findAll(pageable);
		List<MessageInfo> infos=page.getContent();
		for(int i=infos.size()-1;i>-1;i--){
			addAllMessageInfo(infos.get(i));
		}
	}
	
	public void initAdminMsg(){
		//新消息第一
		Sort sort = new Sort(Sort.Direction.DESC, "times");  
		Pageable pageable = new PageRequest(0, msgSize, sort); 
		Page<MessageInfo> page=messageDao.findByLevel(Constant.TEACHER_LEVEL, pageable);
		List<MessageInfo> infos=page.getContent();
		for(int i=infos.size()-1;i>-1;i--){
			addAdminMessage(infos.get(i));
		}
	}
	
	public void initTopInfo(){
		Sort sort = new Sort(Sort.Direction.DESC, "times");  
		Pageable pageable = new PageRequest(0, 1, sort); 
		Page<MessageInfo> page=messageDao.findByMsgType(Constant.MSG_TYPE_TOP,pageable);
		List<MessageInfo> infos=page.getContent();
		if(infos!=null&&infos.size()!=0){
			addMessageInfo(infos.get(0));
		}
	}


}
