package im.socket;

import java.util.Date;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;

import im.dao.ClientInfoRepository;
import im.entity.ClientInfo;
import im.entity.ConsoleInfoDto;
import im.entity.Constant;
import im.entity.LiveUser;
import im.entity.MessageInfo;
import im.util.HtmlFilter;

@Component
public class MessageEventHandler {
	private final SocketIOServer server;

	@Autowired
	private MessageCachePool messageCachePool;
	
	@Autowired
	private LiveUserHandler liveUserHandler;

	@Autowired
	private ClientInfoRepository clientInfoRepository;

	@Autowired
	public MessageEventHandler(SocketIOServer server) {
		this.server = server;
	}

	// 添加connect事件，当客户端发起连接时调用，本文中将clientid与sessionid存入数据库
	// 方便后面发送消息时查找到对应的目标client,
	@OnConnect
	public void onConnect(SocketIOClient client) {
		String openid = client.getHandshakeData().getSingleUrlParam("ssid");
		ClientInfo clientInfo = clientInfoRepository.findClientByOpenId(openid);
		if (clientInfo != null) {
			Date nowTime = new Date(System.currentTimeMillis());
			clientInfo.setConnected((short) 1);
			clientInfo.setMostsignbits(client.getSessionId().getMostSignificantBits());
			clientInfo.setLeastsignbits(client.getSessionId().getLeastSignificantBits());
			clientInfo.setLastconnecteddate(nowTime);
			clientInfoRepository.save(clientInfo);
			client.set("info", clientInfo);
			liveUserHandler.addTotalCount();
		} else {
			client.disconnect();
		}
	}

	// 添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息
	@OnDisconnect
	public void onDisconnect(SocketIOClient client) {
		String clientId = client.getHandshakeData().getSingleUrlParam("clientid");
		ClientInfo clientInfo = clientInfoRepository.findClientByclientid(clientId);
		if (clientInfo != null) {
			clientInfo.setConnected((short) 0);
			clientInfo.setMostsignbits(null);
			clientInfo.setLeastsignbits(null);
			clientInfoRepository.save(clientInfo);
		}
	}

	// 消息接收入口，当接收到消息后，查找发送目标客户端，并且向该客户端发送消息，且给自己发送消息
	@OnEvent(value = "messageevent")
	public void onEvent(SocketIOClient client, AckRequest request, MessageInfo data) {
		System.out.println("-------------------in messageEvent-----------------------");
		ClientInfo currentInfo=(ClientInfo) client.get("info");
		MessageInfo sendData = new MessageInfo();
		sendData.setSourceClientId(currentInfo.getClientid());
		sendData.setTargetClientId(data.getTargetClientId());
		sendData.setMsgContent(HtmlFilter.getNoHTMLString(data.getMsgContent()));
		sendData.setTimes(new Date());
		sendData.setMsgType(data.getMsgType());
		sendData.setHeadUrl(currentInfo.getHeadImg());
		sendData.setLevel(currentInfo.getLevel());
		sendData.setSourceClientOpenId(currentInfo.getOpenId());

		if (Constant.ADMIN_LEVEL.equals(currentInfo.getLevel()) || Constant.TEACHER_LEVEL.equals(currentInfo.getLevel())) {
			// 封装消息模型
			messageCachePool.addMessageInfo(sendData);
		}
		System.out.println("message:"+sendData.getMsgContent());
		putInfo(currentInfo.getLevel(), sendData);
	}
	
	@OnEvent(value = "checkevent")
	public void onCheckEvent(SocketIOClient client, AckRequest request, MessageInfo data) {
		ClientInfo currentInfo=(ClientInfo) client.get("info");
		if (Constant.ADMIN_LEVEL.equals(currentInfo.getLevel()) || Constant.TEACHER_LEVEL.equals(currentInfo.getLevel())) {
			// 封装消息模型
			ConsoleInfoDto cinfo=new ConsoleInfoDto();
			cinfo.setDdInfo(data.getId().toString());
			if(Constant.STATUS_PASS.equals(data.getMsgType())){
				MessageInfo sendData = new MessageInfo();
				sendData.setSourceClientId(data.getSourceClientId());
				sendData.setMsgContent(HtmlFilter.getNoHTMLString(data.getMsgContent()));
				sendData.setTimes(new Date());
				System.out.println(data.getTargetClientId());
				System.out.println(Constant.NONE_NAME.equals(data.getTargetClientId()));
				if(Constant.NONE_NAME.equals(data.getTargetClientId())){
					sendData.setMsgType(Constant.STATUS_NORMAL_INFO);
					sendData.setTargetClientId("");
				}else{
					sendData.setMsgType(Constant.STATUS_PRI_INFO);
					sendData.setTargetClientId(data.getTargetClientId());
				}
				sendData.setHeadUrl(data.getHeadUrl());
				sendData.setLevel(data.getLevel());
				sendData.setSourceClientOpenId(data.getSourceClientOpenId());
				messageCachePool.addMessageInfo(sendData);
				putInfo(currentInfo.getLevel(), sendData);
				cinfo.setType("pass");
			}else if(Constant.STATUS_UNPASS.equals(data.getMsgType())){
				cinfo.setType("unpass");
			}
			putAdminInfo(cinfo);
		}else{
			return;
		}
	}

	// 客户控制 踢出禁言 审核
	@OnEvent(value = "ccevent")
	public void onCcEvent(SocketIOClient client, AckRequest request, ConsoleInfoDto data) {
		System.out.println("-------------------in ccEvent-----------------------");
		Short level = ((ClientInfo) client.get("info")).getLevel();
		//管理员权限
		if (Constant.TEACHER_LEVEL.equals(level) || Constant.ADMIN_LEVEL.equals(level)) {
			//踢出
			if ("kick".equals(data.getType())) {
				//获取用户信息,更新信息
				ClientInfo cinfo = clientInfoRepository.findClientByclientid(data.getDdInfo());
				cinfo.setStatus(Constant.STATUS_TICK);
				clientInfoRepository.save(cinfo);
				//给目标发送踢出信息
				Iterator<SocketIOClient> clientList = server.getAllClients().iterator();
				while (clientList.hasNext()) {
					SocketIOClient targetClient = clientList.next();
					if(cinfo.getClientid().equals(((ClientInfo) targetClient.get("info")).getClientid())){
						targetClient.sendEvent("ccevent", data);
						targetClient.disconnect();
					}
				}
			} else {
				return;
			}
		} else {
			if ("heart".equals(data.getType())) {
				ClientInfo cinfo = clientInfoRepository.findClientByclientid(((ClientInfo) client.get("info")).getClientid());
				long timeleft=cinfo.getTimeLeft();
				if(timeleft<=0){
					client.sendEvent("ccevent", new ConsoleInfoDto("timeout", ""));
					client.disconnect();
				}else{
					cinfo.setTimeLeft(timeleft-1);
					clientInfoRepository.save(cinfo);
				}
			}
		}
	}
	
	/**
	 * 根据用户等级推送信息,普通用户信息全部推送至管理员处
	 * 
	 * @param type
	 * @param sendData
	 * 
	 */
	private void putInfo(Short level, MessageInfo sendData) {
		System.out.println("in put info");
		// 获取所有客户端信息
		Iterator<SocketIOClient> clientList = server.getAllClients().iterator();
		Long msguid = System.currentTimeMillis();
		sendData.setId(msguid);
		while (clientList.hasNext()) {
			SocketIOClient targetClient = clientList.next();
			// 如果是管理员，推送至所有人
			if (Constant.ADMIN_LEVEL.equals(level) || Constant.TEACHER_LEVEL.equals(level)) {
				targetClient.sendEvent("messageevent", sendData);
				// 如果是客户，信息推送至管理员
			} else {
				try {
					if (Constant.ADMIN_LEVEL.equals(((ClientInfo) targetClient.get("info")).getLevel())) {
						targetClient.sendEvent("checkevent", sendData);
					}
				} catch (Exception e) {
					// TODO: handle exception
					continue;
				}
			}
		}
	}
	
	private void putAdminInfo(ConsoleInfoDto cinfo){
		Iterator<SocketIOClient> clientList = server.getAllClients().iterator();
		while (clientList.hasNext()) {
			SocketIOClient targetClient = clientList.next();
			targetClient.sendEvent("ccevent", cinfo);
		}
	}
	
	public  void sendALL(LiveUser count){
		Iterator<SocketIOClient> clientList = server.getAllClients().iterator();
		while (clientList.hasNext()) {
			SocketIOClient targetClient = clientList.next();
			targetClient.sendEvent("serverinfo", count);
		}
	}

}