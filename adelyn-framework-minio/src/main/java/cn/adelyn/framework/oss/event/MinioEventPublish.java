package cn.adelyn.framework.oss.event;

import cn.adelyn.framework.oss.config.MinioTemplate;
import io.minio.CloseableIterator;
import io.minio.ListenBucketNotificationArgs;
import io.minio.Result;
import io.minio.messages.EventType;
import io.minio.messages.NotificationRecords;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * minio 事件发布
 */
@Component
@Profile("minio")
public class MinioEventPublish implements InitializingBean {

	@Autowired
	private MinioTemplate minioTemplate;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void afterPropertiesSet() {
		String[] events = {
				EventType.OBJECT_CREATED_ANY.toString(),
				EventType.OBJECT_REMOVED_ANY.toString()
		};
		ListenBucketNotificationArgs listenBucketNotificationArgs = ListenBucketNotificationArgs.builder()
//											 .bucket()
				.events(events).build();

		// 不断监听minio发的事件，并发布到springboot，由MinioEventNotifier处理
		while (true) {
			try (CloseableIterator<Result<NotificationRecords>> ci = minioTemplate.listenBucketNotification(listenBucketNotificationArgs)) {
				while (ci.hasNext()) {
					NotificationRecords records = ci.next().get();
					records.events().forEach((event) -> applicationEventPublisher.publishEvent(new MinioEvent(event)));
				}
			} catch (Exception e) {

			}
		}
	}
}
