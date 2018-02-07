package im.entity;

import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity  
@Table(name="t_liveUser") 
public class LiveUser {
    @Id  
    @NotNull  
	private long id;
	
	private long totalvisiter;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTotalvisiter() {
		return totalvisiter;
	}

	public void setTotalvisiter(long totalvisiter) {
		this.totalvisiter = totalvisiter;
	}
	
	public void autoadd(){
		this.totalvisiter++;
	}

	/** 
	*  
	*  
	* @param id
	* @param totalvisiter 
	*/ 
	
	public LiveUser(long id, long totalvisiter) {
		super();
		this.id = id;
		this.totalvisiter = totalvisiter;
	}

	/** 
	*  
	*   
	*/ 
	
	public LiveUser() {
		super();
	}
	
}
