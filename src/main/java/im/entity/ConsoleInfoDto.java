package im.entity;

public class ConsoleInfoDto {

	private String type;
	private String ddInfo;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDdInfo() {
		return ddInfo;
	}
	public void setDdInfo(String ddInfo) {
		this.ddInfo = ddInfo;
	}
	/** 
	*  
	*  
	* @param type
	* @param ddInfo 
	*/ 
	
	public ConsoleInfoDto(String type, String ddInfo) {
		super();
		this.type = type;
		this.ddInfo = ddInfo;
	}
	
	public ConsoleInfoDto() {
		super();
	}
	
}
