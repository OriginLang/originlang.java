package com.originlang.minio;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Tags;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
// @ConditionalOnBean(value = {MinioAutoConfiguration.class})
// @AutoConfiguration(after =
// {MinioAutoConfiguration.class,MinioConfigurationProperty.class})
@Component
@Configuration(proxyBeanMethods = false)
public class MinioTemplate {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(MinioTemplate.class);

	public MinioTemplate() {
	}

	MinioClient minioClient;

	/**
	 * minio 客户端
	 * @param minioClient minio 客户端
	 */
	@Autowired
	public void setMinioClient(MinioClient minioClient) {
		this.minioClient = minioClient;
	}

	/**
	 * 创建bucket
	 * @param bucket bucket名称
	 * @return 是否创建成功
	 */
	public synchronized boolean bucketCreate(String bucket) {
		try {
			boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
			if (!found) {
				minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean bucketExists(String bucket) {
		try {
			return this.minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
		}
		catch (Exception e) {
			log.error("判断minio bucket 是否存在异常", e);
			return false;
		}

	}

	/**
	 * 查询所有存储桶
	 */
	public List<io.minio.messages.Bucket> bucketList() {
		try {
			return minioClient.listBuckets();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 删除一个空桶 如果存储桶存在对象不为空时，删除会报错。
	 */
	public void bucketDelete(String bucketName) throws ServerException, InsufficientDataException,
			ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
			InvalidResponseException, XmlParserException, InternalException {
		minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
	}

	public synchronized String fileNameGenerate(String originalFilename) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return System.currentTimeMillis() + "_" + format.format(new Date()) + "_" + new Random().nextInt(1000)
				+ originalFilename.substring(originalFilename.lastIndexOf("."));
	}

	private synchronized String generatePathAndFileNameWithoutSuffix() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String now = format.format(new Date());
		String[] dates = now.split("-");
		String path = dates[0] + "/" + dates[1] + "/" + dates[2] + "/";
		return path + System.currentTimeMillis() + "_" + now + "_" + new Random().nextInt(1000);
	}

	/**
	 * 上传 MultipartFile objectName存在时为更新 objectName前添加了路径 objectName去除了后缀
	 */
	public FileOss fileUpload(MultipartFile multipartFile, Bucket bucketEnum, String objectName) {
		try {
			// 原始名称没有时给个随机的
			String originalFilename = multipartFile.getOriginalFilename();
			// todo
			// originalFilename = originalFilename == null ||
			// originalFilename.trim().length() == 0 ?
			// Math.random(24).toString() :
			// originalFilename;

			// objectName不存在时生成新的
			String fileName = objectName == null || objectName.trim().length() == 0
					? this.generatePathAndFileNameWithoutSuffix() : objectName;

			ObjectWriteResponse response = this.minioClient.putObject(PutObjectArgs.builder()
				.bucket(bucketEnum.getValue())
				.object(fileName)
				.stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
				.contentType(multipartFile.getContentType())
				.build());

			FileOss fileInfo = new FileOss();
			fileInfo.setBucket(response.bucket());
			fileInfo.setObject(response.object());
			fileInfo.setFilename(originalFilename);
			return fileInfo;

		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 上传 File objectName存在时为更新 objectName前添加了路径 objectName去除了后缀
	 */
	public FileOss fileUpload(File file, Bucket bucketEnum, String objectName, String contentType) {
		try {
			// 原始名称没有时给个随机的
			String originName = file.getName();
			// todo
			// originName = originName.trim().length() == 0 ? RandomUtil.randomString(24)
			// : originName;

			// objectName不存在时生成新的
			String fileName = objectName == null || objectName.trim().isEmpty()
					? this.generatePathAndFileNameWithoutSuffix() : objectName;

			// 构造参数
			UploadObjectArgs args = UploadObjectArgs.builder()
				.bucket(bucketEnum.getValue())
				.object(fileName)
				.filename(file.getAbsolutePath())
				.contentType(contentType)
				.build();
			// 使用文件上传
			ObjectWriteResponse response = this.minioClient.uploadObject(args);
			FileOss fileInfo = new FileOss();
			fileInfo.setBucket(response.bucket());
			fileInfo.setObject(response.object());
			fileInfo.setFilename(originName);
			return fileInfo;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 文件下载 以流的形式获取一个文件对象
	 */
	public InputStream download(Bucket bucketEnum, String objectName) {
		GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucketEnum.getValue()).object(objectName).build();
		try {
			InputStream is = minioClient.getObject(getObjectArgs);

			return is;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 文件下载 以流的形式获取一个文件对象（断点下载）
	 * @param bucketEnum 存储桶
	 * @param objectName 存储桶里的对象名称
	 * @param offset 起始字节的位置
	 * @param length 要读取的长度 (可选，如果无值则代表读到文件结尾)
	 */
	public InputStream download(Bucket bucketEnum, String objectName, long offset, long length) {
		GetObjectArgs getObjectArgs = GetObjectArgs.builder()
			.bucket(bucketEnum.getValue())
			.object(objectName)
			.offset(offset)
			.length(length)
			.build();
		try {
			InputStream is = minioClient.getObject(getObjectArgs);
			return is;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 给文件打标签
	 */
	public boolean tagFile(Bucket bucketEnum, String fileName, List<TagEnum> tagEnums) {
		try {
			Tags tags = Tags.newObjectTags(TagEnum.toMap(tagEnums));
			SetObjectTagsArgs args = SetObjectTagsArgs.builder()
				.bucket(bucketEnum.getValue())
				.object(fileName)
				.tags(tags)
				.build();
			this.minioClient.setObjectTags(args);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 给文件打标签
	 */
	public boolean tagFile(Bucket bucketEnum, String fileName, TagEnum tagEnum) {
		try {
			Tags tags = Tags.newObjectTags(TagEnum.toMap(tagEnum));
			SetObjectTagsArgs args = SetObjectTagsArgs.builder()
				.bucket(bucketEnum.getValue())
				.object(fileName)
				.tags(tags)
				.build();
			this.minioClient.setObjectTags(args);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除⽂件
	 */
	public void fileDelete(String bucketName, String objectName) throws Exception {
		this.minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
	}

	/**
	 * 获取外链
	 * @param bucket 桶
	 * @param objectName 对象名
	 * @param expiry 过期时间,天
	 */
	public String urlGet(Bucket bucket, String objectName, Integer expiry) {
		try {
			return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
				.method(Method.GET)
				.bucket(bucket.getValue())
				.object(objectName)
				.expiry((int) TimeUnit.DAYS.toSeconds(expiry))
				.build());
		}
		catch (Exception e) {
			log.error("获取文件外链异常", e);
			return null;
		}
	}

}
