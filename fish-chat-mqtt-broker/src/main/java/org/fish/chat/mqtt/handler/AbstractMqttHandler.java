/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.mqtt.handler;


import io.netty.channel.ChannelHandlerContext;
import org.fish.chat.mqtt.service.MqttBizService;
import org.fish.chat.mqtt.session.manager.ChannelSessionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Comments for AbstracMqttHandler.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月10日 下午8:31:21
 */
public abstract class AbstractMqttHandler<T> implements InitializingBean {

    protected ChannelSessionManager channelSessionManager;

    protected MqttBizService mqttBizService;

    public abstract void channelRead(ChannelHandlerContext ctx, T msg) throws Exception;
    
    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(channelSessionManager, "channelSessionManager must not null!");
        Assert.notNull(mqttBizService, "mqttBizService must not null!");
    }

    /**
     * @param channelSessionManager the channelSessionManager to set
     */
    public void setChannelSessionManager(ChannelSessionManager channelSessionManager) {
        this.channelSessionManager = channelSessionManager;
    }

    /**
     * @param mqttBizService the mqttBizService to set
     */
    public void setMqttBizService(MqttBizService mqttBizService) {
        this.mqttBizService = mqttBizService;
    }

}
