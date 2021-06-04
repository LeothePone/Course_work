package com;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.ArrayList;
import java.util.List;

// Обработчик на сервере
public class MainHandler extends SimpleChannelInboundHandler<String> {
    private static final List<Channel> channels = new ArrayList<>(); // Массив каналов
    private String client_name;
    private static int new_Client_index = 1; // Индекс подключаемого клиента

    // Когда клиент подключается
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился: "+ ctx);
        channels.add(ctx.channel());
        client_name = "Клиент №" + (new_Client_index) ; // Присвоение номера клиента
        new_Client_index++; // + 1 к номеру подключаемого клиента
        // Вывод сообщения о подключении нового клиента
        broadcastMessage("SERVER", "Подключился новый клиент: "+client_name+ ". "
                +"Для смены имени введите команду:/Сменить_имя <имя>");
    }

    // Когда клиент присылает сообщение
    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("Сообщение: "+s);
        if (s.startsWith("/"))
        { // Служебные команды чата
            if (s.startsWith("/Сменить_имя ")) { // Если строка начинается с команды
                String newName = s.split("\\s", 2)[1];
                broadcastMessage("Server","Клиент " + client_name+" сменил ник на :"+newName);
                client_name = newName; // Берём имя из строки
            }
            return; // Выход из метода
        }
        broadcastMessage(client_name, s); // Иначе обычное сообщение
    }

    // посылка сообщений
    public void broadcastMessage(String client_name, String message) {
        // Создание сообщения полученного от клиента: имя клиента, сообщение
        String out = String.format("[%s]: %s\n", client_name, message);
        for (Channel c : channels) { // Пересылка сообщения всем каналам
            c.writeAndFlush(out);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент " + client_name + " вышел из сети");
        channels.remove(ctx.channel());
        broadcastMessage("SERVER", "Клиент " + client_name + " вышел из сети");
        ctx.close();
    }

    // Обработка исключений
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Клиент " + client_name + " отключился");
        channels.remove(ctx.channel()); // Закрытие канала при отключении клиента
        broadcastMessage("Server", "Клиент: "+ client_name+" Вышел из");
        ctx.close(); // Закрытие соединения с клиентом при получении исключения
    }
}
