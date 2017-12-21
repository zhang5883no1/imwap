package im.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/file")
public class FileController {
	
	@Value("${file.upload.path}")
	private String uploadPath;
	
	@RequestMapping(value = "uploadPic", method = RequestMethod.POST)
	@ResponseBody
	public String uploadPic(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String randomName=new StringBuilder().append(Calendar.getInstance().getTime().getTime()).toString();
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
		MultipartFile file = null;
		BufferedOutputStream stream = null;
		for (int i = 0; i < files.size(); ++i) {
			file = files.get(i);
			if (!file.isEmpty()) {
				try {
					String uploadFilePath = file.getOriginalFilename();
					System.out.println("uploadFlePath:" + uploadFilePath);
					// 截取上传文件的文件名
					String uploadFileName = uploadFilePath.substring(uploadFilePath.lastIndexOf('\\') + 1,
							uploadFilePath.indexOf('.'));
					System.out.println("multiReq.getFile()" + uploadFileName);
					// 截取上传文件的后缀
					String uploadFileSuffix = uploadFilePath.substring(uploadFilePath.indexOf('.') + 1,
							uploadFilePath.length());
					System.out.println("uploadFileSuffix:" + uploadFileSuffix);
					
					System.out.println(uploadPath+randomName+".jpg");
					stream = new BufferedOutputStream(new FileOutputStream(
							new File(uploadPath+randomName+".jpg")));
					byte[] bytes = file.getBytes();
					stream.write(bytes, 0, bytes.length);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (stream != null) {
							stream.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				System.out.println("上传文件为空");
			}
		}
		System.out.println("文件接受成功了");
		return randomName+".jpg";
	}
}
