package im;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import im.dao.ClientInfoRepository;
import im.dao.MessageInfoRepository;
import im.entity.ClientInfo;
import im.entity.Constant;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImwapApplicationTests {

	@Autowired
	private MessageInfoRepository msgdao;
	@Autowired
	private ClientInfoRepository clientdao;
	
	@Test
	public void clientInfoTest() throws InterruptedException {
		String s="http://wap.qizhihuitrade.cn/?openid=ouQYm0_bDahn6UH7iidT_0icsPq0&nickname=%E6%A9%98%E5%AD%90%F0%9F%8D%8A"
				+ "&headimgurl=http%3A%2F%2Fwx.qlogo.cn%2Fmmopen%2Fvi_32%2FQ0j4TwGTfTLQuic0ftibKjn3JcdaWeNib9INSZwng3iaehGckVz1StQUIV6InoTpSQ4yOwrttDa3DdXbohqIBiaS4jw%2F0&stat=zb";
		ClientInfo clientInfo=new ClientInfo();
		clientInfo.setClientid("%E6%A9%98%E5%AD%90%F0%9F%8D%8A");
		clientInfo.setHeadImg("http%3A%2F%2Fwx.qlogo.cn%2Fmmopen%2Fvi_32%2FQ0j4TwGTfTLQuic0ftibKjn3JcdaWeNib9INSZwng3iaehGckVz1StQUIV6InoTpSQ4yOwrttDa3DdXbohqIBiaS4jw%2F0");
		clientInfo.setCreateDate(new Date());
		clientInfo.setLevel(Constant.CUSTOMER_LEVEL);
		clientInfo.setOpenId("ouQYm0_bDahn6UH7iidT_0icsPq0");
		clientInfo.setStatus(Constant.STATUS_NORMAL);
		clientInfo=clientdao.save(clientInfo);
		System.out.println(clientInfo.toString());
	}

}
