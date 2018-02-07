package im.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import im.dao.LiveClientRepository;
import im.entity.LiveUser;
import im.socket.LiveUserHandler;
import im.socket.MessageCachePool;
import im.socket.MessageEventHandler;


/**
 * @Scheduled(fixedRate = 5000) ：上一次开始执行时间点之后5秒再执行
 * @Scheduled(fixedDelay = 5000) ：上一次执行完毕时间点之后5秒再执行
 * @Scheduled(initialDelay=1000, fixedRate=5000)
 *                               ：第一次延迟1秒后执行，之后按fixedRate的规则每5秒执行一次
 * @Scheduled(cron="1/5 * * * * *") ：通过cron表达式定义规则
 */
@Component
public class ScheduledTasks {
	
	@Autowired
	MessageCachePool msgpool;
	@Autowired
	MessageEventHandler messageEventHandler;
	@Autowired
	LiveUserHandler liveUserHandler;
	
	boolean initf=true;
	
	/**
	 * 每分钟更新在线客户信息
	 */
	//@Scheduled(fixedRate = 1 * 60 * 1000)
	@Scheduled(fixedDelay = 10 * 60 * 60 * 1000)
	public void updateLiveUser() {
		if(initf){
			initf=false;
			msgpool.initMsg();
			msgpool.initAdminMsg();
			liveUserHandler.initLiver();
		}
	}
	
	@Scheduled(cron="1/10 * * * * *")
	public void sendTotalCount() {
		LiveUser totalCount=liveUserHandler.getLiveUser();
		messageEventHandler.sendALL(totalCount);
	}
	
	
	@Scheduled(fixedDelay = 5*60*1000)
	public void saveTotalCount() {
		liveUserHandler.saveLiveUser();
	}
}
