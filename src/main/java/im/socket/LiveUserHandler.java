package im.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import im.dao.LiveClientRepository;
import im.entity.LiveUser;

@Component
public class LiveUserHandler {

	@Autowired
	LiveClientRepository liveClientDao;
	
	private LiveUser liveUser;

	
	public LiveUser getLiveUser() {
		return liveUser;
	}


	public void setLiveUser(LiveUser liveUser) {
		this.liveUser = liveUser;
	}

	public void addTotalCount(){
		this.liveUser.autoadd();
	}

	public void saveLiveUser() {
		// TODO Auto-generated method stub
		liveClientDao.save(this.liveUser);
	}


	public void initLiver() {
		// TODO Auto-generated method stub
		this.liveUser=liveClientDao.findById(1);
	}

}
