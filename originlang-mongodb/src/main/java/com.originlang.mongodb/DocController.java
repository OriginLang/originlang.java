package com.originlang.mongodb;

import com.mongodb.client.gridfs.GridFSBucket;
import com.originlang.id.IdCreator;
import com.originlang.webmvc.annotation.AnonymousAccess;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
public class DocController {

	private final DocRepo docRepo;

	@Data
	public static class DocDTO implements Serializable {

		@Serial
		private static final long serialVersionUID = -273093363202398925L;

		private MultipartFile file;

		private String name;

	}

	@Resource
	private GridFsTemplate gridFsTemplate;

	@Resource
	private GridFSBucket gridFSBucket;

	@PostMapping("/doc")
	@AnonymousAccess
	public Doc doc(DocDTO dto) throws IOException {
		System.out.println("doc");
		Doc doc = new Doc();
		// doc.setId(IdCreator.tsid());
		doc.setAppInfoId(1L);
		Map<String, Object> data = new HashMap<>();
		data.put("name", dto.name);
		if (dto.file != null && dto.file.getSize() > 16000000) {
			System.out.println("文件过大" + dto.file.getSize() + "超过了16M");
			throw new RuntimeException("文件过大");
		}
		// 小文件
		Binary binary = new Binary(dto.getFile().getBytes());
		data.put("file", binary);
		// 大文件
		// ObjectId objectId = gridFsTemplate.store(dto.getFile().getInputStream(),
		// String.valueOf(IdCreator.tsid()));
		// GridFSBucket gridFSBucket = GridFSBuckets.create(mongoDatabase, "2026");
		// ObjectId objectId =
		// gridFSBucket.uploadFromStream(String.valueOf(IdCreator.tsid()),
		// dto.getFile().getInputStream());
		// data.put("file", String.valueOf(IdCreator.tsid()));
		doc.setData(data);
		docRepo.save(doc);
		return doc;
	}

	@GetMapping("/doc/{id}")
	@AnonymousAccess
	public Doc doc(@PathVariable Long id) throws IOException {

		Doc doc = docRepo.findById(id).orElse(null);
		return doc;
		// if (doc == null) {
		// return null;
		// }
		// Map<String, Object> data = doc.getData();
		// Object fileId = data.get("file");
		// if (fileId == null) {
		// return doc;
		// }
		// Query gridQuery = new
		// Query().addCriteria(Criteria.where("filename").is(fileId.toString()));
		//
		// //根据id查询文件
		// GridFSFile fsFile = gridFsTemplate.findOne(gridQuery);
		// //打开流下载对象
		// GridFSDownloadStream in =
		// gridFSBucket.openDownloadStream(fsFile.getObjectId());
		// if (in.getGridFSFile().getLength() > 0) {
		// //获取流对象
		// GridFsResource resource = new GridFsResource(fsFile, in);
		// //获取数据
		// return doc;
		// } else {
		// return null;
		// }

	}

}
