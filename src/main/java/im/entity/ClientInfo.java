package im.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity  
@Table(name="t_clientinfo") 
public class ClientInfo {  
    @Id  
    @NotNull  
    private String openId;//微信平台openid
    private String clientid;//用户名
    private Short connected;//1已连接  0已断开 
    private Long mostsignbits;  
    private Long leastsignbits;  
    private Date lastconnecteddate;  //最后登录时间
    private String password;//密码
    private Short level;//等级 0游客 1普通用户  9老师 10管理员
    private String headImg;//头像
    private Long createUser;//创建人
    private String mobile;//手机
    private String roomNo;//房间号
    private Short status;//状态 0删除 1正常 2禁言
    private Date createDate;
    
    private int totalHour;//总时间，单位小时
    private long timeLeft;//剩余时间，单位分钟
    
    public String getClientid() {  
        return clientid;  
    }  
    public void setClientid(String clientid) {  
        this.clientid = clientid;  
    }  
    public Short getConnected() {  
        return connected;  
    }  
    public void setConnected(Short connected) {  
        this.connected = connected;  
    }  
    public Long getMostsignbits() {  
        return mostsignbits;  
    }  
    public void setMostsignbits(Long mostsignbits) {  
        this.mostsignbits = mostsignbits;  
    }  
    public Long getLeastsignbits() {  
        return leastsignbits;  
    }  
    public void setLeastsignbits(Long leastsignbits) {  
        this.leastsignbits = leastsignbits;  
    }  
    public Date getLastconnecteddate() {  
        return lastconnecteddate;  
    }  
    public void setLastconnecteddate(Date lastconnecteddate) {  
        this.lastconnecteddate = lastconnecteddate;  
    }
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Short getLevel() {
		return level;
	}
	public void setLevel(Short level) {
		this.level = level;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public Long getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	public int getTotalHour() {
		return totalHour;
	}
	public void setTotalHour(int totalHour) {
		this.totalHour = totalHour;
	}
	public long getTimeLeft() {
		return timeLeft;
	}
	public void setTimeLeft(long timeLeft) {
		this.timeLeft = timeLeft;
	}
	/** 
	*  
	*  
	* @param openId
	* @param clientid
	* @param connected
	* @param mostsignbits
	* @param leastsignbits
	* @param lastconnecteddate
	* @param password
	* @param level
	* @param headImg
	* @param createUser
	* @param mobile
	* @param roomNo
	* @param status
	* @param createDate
	* @param totalHour
	* @param timeLeft 
	*/ 
	
	public ClientInfo(String openId, String clientid, Short connected, Long mostsignbits, Long leastsignbits,
			Date lastconnecteddate, String password, Short level, String headImg, Long createUser, String mobile,
			String roomNo, Short status, Date createDate, int totalHour, long timeLeft) {
		super();
		this.openId = openId;
		this.clientid = clientid;
		this.connected = connected;
		this.mostsignbits = mostsignbits;
		this.leastsignbits = leastsignbits;
		this.lastconnecteddate = lastconnecteddate;
		this.password = password;
		this.level = level;
		this.headImg = headImg;
		this.createUser = createUser;
		this.mobile = mobile;
		this.roomNo = roomNo;
		this.status = status;
		this.createDate = createDate;
		this.totalHour = totalHour;
		this.timeLeft = timeLeft;
	}
	public ClientInfo() {
		super();
	}
	@Override
	public String toString() {
		return "ClientInfo [openId=" + openId + ", clientid=" + clientid + ", connected=" + connected
				+ ", mostsignbits=" + mostsignbits + ", leastsignbits=" + leastsignbits + ", lastconnecteddate="
				+ lastconnecteddate + ", password=" + password + ", level=" + level + ", headImg=" + headImg
				+ ", createUser=" + createUser + ", mobile=" + mobile + ", roomNo=" + roomNo + ", status=" + status
				+ ", createDate=" + createDate + ", totalHour=" + totalHour + ", timeLeft=" + timeLeft + "]";
	}  
    
    
}
