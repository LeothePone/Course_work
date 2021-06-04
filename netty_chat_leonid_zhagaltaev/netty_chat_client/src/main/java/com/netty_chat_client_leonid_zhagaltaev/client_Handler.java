package com.netty_chat_client_leonid_zhagaltaev;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class client_Handler extends SimpleChannelInboundHandler<String>
{
    private Callback onMessageReceivedCallback;

    public client_Handler(Callback onMessageReceivedCallback) {
        this.onMessageReceivedCallback = onMessageReceivedCallback;
    }

    // Чтение из канала
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        // Вывод собственных сообщений
        if (onMessageReceivedCallback != null) {
            onMessageReceivedCallback.callback(s);
        }
    }

    // Обработка исключения
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }
}
