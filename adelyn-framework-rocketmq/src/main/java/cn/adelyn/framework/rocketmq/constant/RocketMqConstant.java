package cn.adelyn.framework.rocketmq.constant;

/**
 * @author chengze
 * @date 2023/1/30
 * @desc rocketMq全局配置
 */
public interface RocketMqConstant {

    // 延迟消息 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h (1-18)

    /**
     * 默认发送消息超时时间
     */
     int TIMEOUT = 3000;

     /**
      * retryTimes
      */
     int retryTimes = 2;


    /**
     * topic
     */
}
