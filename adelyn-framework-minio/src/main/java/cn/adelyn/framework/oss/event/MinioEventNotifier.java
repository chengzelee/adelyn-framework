package cn.adelyn.framework.oss.event;

import io.minio.messages.Event;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 事件监听
 */
@Component
public class MinioEventNotifier {

	@EventListener
	public void onApplicationEvent(MinioEvent minioEvent) {
		Event event = minioEvent.getEvent();
		// do something 可以放线程池
	}
}
