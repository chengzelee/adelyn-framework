package cn.adelyn.framework.oss.event;

import io.minio.messages.Event;
import org.springframework.context.ApplicationEvent;

/**
 * minio 事件
 */
public class MinioEvent extends ApplicationEvent {

	private Event event;

	public MinioEvent(Event event) {
		super(event);
		this.event = event;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
}
