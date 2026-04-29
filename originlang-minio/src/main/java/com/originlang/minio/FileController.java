package com.originlang.minio;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件
 */
@Controller
public class FileController {

	@Resource
	MinioTemplate minioTemplate;

	/**
	 * 上传文件
	 * @param file 文件
	 * @return 文件信息
	 */
	// @RequestMapping(value = "/upload", method = {
	// org.springframework.web.bind.annotation.RequestMethod.POST })
	// @ResponseBody
	public FileOss upload(@NotNull(message = "必须选中一个文件上传") MultipartFile file) {

		var originalFilename = file.getOriginalFilename();
		// 判断桶是否存在
		if (!minioTemplate.bucketExists(Bucket.image.getValue())) {
			minioTemplate.bucketCreate(Bucket.image.getValue());
		}
		// 传null则不指定对象名
		FileOss fileOss = minioTemplate.fileUpload(file, Bucket.image, null);
		String url = minioTemplate.urlGet(Bucket.valueOf(fileOss.getBucket()), fileOss.getObject(), 7);
		fileOss.setUrl(url);
		fileOss.setFilename(originalFilename);
		return fileOss;
	}

}
