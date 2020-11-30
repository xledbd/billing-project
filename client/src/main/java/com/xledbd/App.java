package com.xledbd;

import com.google.gson.Gson;
import com.xledbd.entity.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Socket socket;
    public static ObjectInputStream inputStream;
    public static ObjectOutputStream outputStream;
    public static User user;

    public static Gson gson = new Gson();

    public static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("loginMenu"));

        stage.setTitle("Добро пожаловать");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        outputStream.writeObject("exit");
    }

    static void setRoot(String fxml) throws IOException {
        scene = new Scene((loadFXML(fxml)));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        try {
            socket = new Socket("127.0.0.1", 2525);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}