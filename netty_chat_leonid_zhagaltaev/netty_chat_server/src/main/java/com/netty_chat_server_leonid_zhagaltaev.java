package com;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class netty_chat_server_leonid_zhagaltaev {
    private static final int port_server = 8888; // Порт подключения

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // Пулл потоков для подключающихся клиентов
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // Пулл потоков для обработки данных сетевого взаимодействия
        try {
            ServerBootstrap b = new ServerBootstrap(); // Инициализация объекта server bootstrap
            b.group(bossGroup,workerGroup) // Использование пула потоков для подключающихся клиентов и серверного взаимодействия
            .channel(NioServerSocketChannel.class) // Socket канал для подключения
                .childHandler(new ChannelInitializer<SocketChannel>() { // Получение информации о подключении из socket канала
                @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception { // инциализация канала
                    // Инициализация для обработки bytebuf и строк
                    socketChannel.pipeline().addLast(new StringDecoder(), new StringEncoder(), new MainHandler());
                    }
                });
            ChannelFuture future = b.bind(port_server).sync(); // Старт сервера на порте 8888
            future.channel().closeFuture().sync(); // Блокирующую операция для канала, ожидающая завершение работы сервера

        } catch (Exception e) { // Перехват и вывод исключений
            e.printStackTrace();
        }
        finally {
            bossGroup.shutdownGracefully(); // закрытие пула потоков после отключения сервера
            workerGroup.shutdownGracefully();
        }
    }

}
