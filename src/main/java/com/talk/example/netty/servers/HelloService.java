package com.talk.example.netty.servers;

import com.talk.example.netty.pojo.Command;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Created by zhongjing on 2016/06/08 0008.
 */
public class HelloService {
    public static void main(String[] args) {
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));
        //设置一个处理客户端消息和各种消息事件的类(Handler)
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {

                /**
                 * 梳理思路：
                 * 通过Netty传递，都需要基于流，以ChannelBuffer的形式传递。所以，Object -> ChannelBuffer.Netty提供了转换工具，
                 * 需要我们配置到Handler。样例从客户端 -> 服务端，单向发消息，所以在客户端配置了编码，服务端解码。如果双向收发，则
                 * 需要全部配置Encoder和Decoder
                 */

                /**
                 * 注册到Server的Handler是有顺序的，如果你颠倒一下注册顺序：会强转失败
                 *
                 * return Channels.pipeline(new ObjectDecoder(
                 *              new HelloServiceHandler(),ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                 */
                return Channels.pipeline(new ObjectDecoder(
                        ClassResolvers.cacheDisabled(this.getClass().getClassLoader())),new HelloServiceHandler());
            }
        });

        //开放8080端口供客户端访问。
        bootstrap.bind(new InetSocketAddress(8080));
    }

    private static class HelloServiceHandler extends SimpleChannelHandler{
        /**
         * 当有客户端绑定到服务端的时候触发，打印"Hello world, I'm server."
         * @param ctx
         * @param e
         */
        @Override
        public void channelConnected(
                ChannelHandlerContext ctx,
                ChannelStateEvent e) {
            System.out.println("服务端启动成功");
        }

        /**
         * 当接受到消息的时候触发
         * @param ctx
         * @param e
         * @throws Exception
         */
        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            super.messageReceived(ctx, e);
            Command command = (Command)e.getMessage();
            System.out.println("传递成功："+command.getActionName());
        }
    }
}
