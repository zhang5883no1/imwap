package im.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_messageinfo")
public class MessageInfo {
	@Id
	@GeneratedValue
	private Long id;
	//原客户端openid
	private String sourceClientOpenId;
	//源客户端id  
    private String sourceClientId;  
    //目标客户端id  
    private String targetClientId;  
    //消息类型 
    private String msgType;  
    //消息内容  
    private String msgContent;  
    //发送时间
    private Date times;
    private long timestamp;
    //房间号
    private String roomNo;
    //消息等级
    private Short level;
    //头像
    private String headUrl;
    
    public String getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	public Date getTimes() {
		return times;
	}
	public void setTimes(Date times) {
		this.times = times;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSourceClientId() {  
        return sourceClientId;  
    }  
    public void setSourceClientId(String sourceClientId) {  
        this.sourceClientId = sourceClientId;  
    }  
    public String getTargetClientId() {  
        return targetClientId;  
    }  
    public void setTargetClientId(String targetClientId) {  
        this.targetClientId = targetClientId;  
    }  
    public String getMsgType() {  
        return msgType;  
    }  
    public void setMsgType(String msgType) {  
        this.msgType = msgType;  
    }  
    public String getMsgContent() {  
        return msgContent;  
    }  
    public void setMsgContent(String msgContent) {  
        this.msgContent = msgContent;  
    }
    
	public Short getLevel() {
		return level;
	}
	public void setLevel(Short level) {
		this.level = level;
	}
	
	public String getHeadUrl() {
		return headUrl;
	}
	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}
	
	public String getSourceClientOpenId() {
		return sourceClientOpenId;
	}
	public void setSourceClientOpenId(String sourceClientOpenId) {
		this.sourceClientOpenId = sourceClientOpenId;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public MessageInfo() {
		super();
	}
	/** 
	*  
	*  
	* @param id
	* @param sourceClientOpenId
	* @param sourceClientId
	* @param targetClientId
	* @param msgType
	* @param msgContent
	* @param times
	* @param timestamp
	* @param roomNo
	* @param level
	* @param headUrl 
	*/ 
	
	public MessageInfo(Long id, String sourceClientOpenId, String sourceClientId, String targetClientId, String msgType,
			String msgContent, Date times, long timestamp, String roomNo, Short level, String headUrl) {
		super();
		this.id = id;
		this.sourceClientOpenId = sourceClientOpenId;
		this.sourceClientId = sourceClientId;
		this.targetClientId = targetClientId;
		this.msgType = msgType;
		this.msgContent = msgContent;
		this.times = times;
		this.timestamp = timestamp;
		this.roomNo = roomNo;
		this.level = level;
		this.headUrl = headUrl;
	}
    
	
}
