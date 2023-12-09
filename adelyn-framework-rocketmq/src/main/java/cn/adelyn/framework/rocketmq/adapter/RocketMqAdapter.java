package cn.adelyn.framework.rocketmq.adapter;

import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.shaded.org.slf4j.Logger;
import org.apache.rocketmq.shaded.org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

/**
 * @author chengze
 * @date 2023/1/30
 * @desc rocketMq
 */
@Configuration
public class RocketMqAdapter {

    private static final Logger log = LoggerFactory.getLogger(RocketMqAdapter.class);

    @Value("${rocketmq.name-server:}")
    private String nameServer;

    public void sendMessage(String topic, String messageKey, String messageTag, String messageBody) throws ClientException{
        final ClientServiceProvider provider = ClientServiceProvider.loadService();

        final Producer producer = ProducerSingleton.getInstance(topic);
        final Message message = provider.newMessageBuilder()
                // Set topic for the current message.
                .setTopic(topic)
                // Message secondary classifier of message besides topic.
                .setTag(messageTag)
                // Key(s) of the message, another way to mark message besides message id.
                .setKeys(messageKey)
                .setBody(messageBody.getBytes(StandardCharsets.UTF_8))
                .build();
        try {
            final SendReceipt sendReceipt = producer.send(message);
            log.info("Send message successfully, messageId={}", sendReceipt.getMessageId());
        } catch (Throwable t) {
            log.error("Failed to send message", t);
        }
        // Close the producer when you don't need it anymore.
        // You could close it manually or add this into the JVM shutdown hook.
        // producer.shutdown();
    }

}
