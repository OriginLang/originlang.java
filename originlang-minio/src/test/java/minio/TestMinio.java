package minio;

import com.originlang.minio.FileController;
import com.originlang.minio.MinioAutoConfiguration;
import com.originlang.minio.MinioTemplate;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * todo 测试minio
 *
 * @author snow
 * @version 1.0.0
 * @since 5/6/25
 */
@SpringBootTest(classes = { MinioAutoConfiguration.class, FileController.class, MinioTemplate.class },
		args = { "--spring.profiles.active=minio-test" })
public class TestMinio {

	@Resource
	MinioTemplate minioTemplate;

	// @Test
	public void bucket() {
		System.out.println("bucket");
		minioTemplate.bucketCreate("test");
	}

}
