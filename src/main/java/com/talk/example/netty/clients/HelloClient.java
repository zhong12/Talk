package com.talk.example.netty.clients;

import com.sun.xml.internal.ws.resources.SenderMessages;
import com.talk.example.netty.pojo.Command;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created by zhongjing on 2016/06/08 0008.
 */
public class HelloClient {
    public static void main(String[] args) {
        // Client服务启动器
        ClientBootstrap bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        // 设置一个处理服务端消息和各种消息事件的类(Handler)

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new ObjectEncoder(),new HelloClientHandler());
            }
        });

        bootstrap.connect(new InetSocketAddress("127.0.0.1",8080));
    }

    private static class HelloClientHandler extends SimpleChannelHandler {
        /**
         * 当绑定到服务端的时候触发，打印"Hello world, I'm client."
         * @param ctx
         * @param e
         */
        @Override
        public void channelConnected(ChannelHandlerContext ctx,
                                     ChannelStateEvent e) {
            this.sendObject(e.getChannel());
        }

        /**
         * 发送Object
         * @param channel
         */
        private void sendObject(Channel channel) {
            Command command =new Command();
            command.setActionName("Hello action.");
            channel.write(command);
        }

    }
}
