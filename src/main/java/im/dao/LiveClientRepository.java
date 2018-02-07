package im.dao;

import org.springframework.data.repository.CrudRepository;

import im.entity.LiveUser;

public interface LiveClientRepository extends CrudRepository<LiveUser, String>{

	LiveUser findById(long id);
}
