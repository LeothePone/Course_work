package com.netty_chat_client_leonid_zhagaltaev;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Network_class network;

    @FXML
    private TextField msgField; // сообщение из поля отправки в макете

    @FXML
    private TextArea mainArea; // Поле вывода сообщений

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        network = new Network_class((args) -> {
            mainArea.appendText((String)args[0]); // Вывод в окно собственных сообщений
        }); // Инициализация сетевого класса при запуске клиента
    }

    // Действие при нажатии кнопки отправить
    public void sendMSGAction(ActionEvent actionEvent) {
        network.sendMessage(msgField.getText()); // Отправка содержимого поля для ввода сообщений
        msgField.setText(""); // Очистка поля для ввода сообщений
        msgField.requestFocus(); // Перевод фокуса на поле для ввода сообщений
    }

    // Выход из программы
    public void exitAction(ActionEvent actionEvent) {
        network.close(); // Закрыть подключение к серверу при выходе
        Platform.exit(); // Выход из программы
    }
}
