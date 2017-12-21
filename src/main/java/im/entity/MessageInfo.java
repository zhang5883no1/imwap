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
    //房间号
    private String roomNo;
    //消息等级
    private Short level;
    
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
	public MessageInfo() {
		super();
	}
	public MessageInfo(Long id, String sourceClientId, String targetClientId, String msgType, String msgContent,
			Date times, String roomNo,Short level) {
		super();
		this.id = id;
		this.sourceClientId = sourceClientId;
		this.targetClientId = targetClientId;
		this.msgType = msgType;
		this.msgContent = msgContent;
		this.times = times;
		this.roomNo = roomNo;
		this.level=level;
	}  
    
}
