package im.dao;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import im.entity.MessageInfo;

public interface MessageInfoRepository extends CrudRepository<MessageInfo, String> {

	@Cacheable
	Page<MessageInfo> findAll(Pageable pageable);

	Page<MessageInfo> findByMsgType(String msgType ,Pageable pageable);
}
