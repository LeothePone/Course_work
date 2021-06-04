package com.netty_chat_client_leonid_zhagaltaev;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


public class Network_class {
    private SocketChannel channel;

    private ChannelFuture closeFuture;
    private Callback onMessageReceivedCallback;

    private static final String address_server = "localhost"; // Адрес подключения
    private static final int port_server = 8888; // Порт подключения

    public Network_class(Callback onMessageReceivedCallback) { // Класс для обработки сетевых действий
        Thread t = new Thread(() -> { // Обработка действий клиента в отдельном потоке
            NioEventLoopGroup workerGroup = new NioEventLoopGroup(); // Пул потока для обработки сетевых событий
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel (NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() { // Открытие сокет канала на клиенте
                            @Override
                            // Инициализация канала
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                channel = socketChannel;
                                // обработка строки в/из bytebuff
                                socketChannel.pipeline().addLast(new StringDecoder(), new StringEncoder(),
                                        new client_Handler(onMessageReceivedCallback));
                        }
                        });
                ChannelFuture future = b.connect(address_server,port_server).sync(); // Подключение к серверу
                future.channel().closeFuture().sync(); // блокирующая операция ожидания закрытия канала
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                workerGroup.shutdownGracefully();
            }
        });
        t.start(); // Запуск потока
    }

    // Отправка сообщения
    public void sendMessage(String str) {
        channel.writeAndFlush(str); // Отправка строки и очистка буфера
    }

    // Закрытие соединения на клиенте
    public void close()
    {
        channel.close();
    }
}
