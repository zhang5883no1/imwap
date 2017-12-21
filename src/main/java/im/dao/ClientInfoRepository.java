package im.dao;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import im.entity.ClientInfo;

public interface ClientInfoRepository extends CrudRepository<ClientInfo, String>{  
    ClientInfo findClientByclientid(String clientId);
    
    Page<ClientInfo> findClientByclientid(String clientId,Pageable pageable);
    
    List<ClientInfo> findClientByConnectedAndRoomNo(Short connected ,String roomNo);
    
    ClientInfo findClientByClientidAndPassword(String clientId,String password);
    
    @Cacheable
    Page<ClientInfo> findAll(Pageable pageable);
    
}
