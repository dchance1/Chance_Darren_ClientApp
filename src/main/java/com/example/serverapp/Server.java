package com.example.serverapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server extends Application {
    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Text area for displaying contents
        TextArea ta = new TextArea();

        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("Server"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        new Thread(() -> {
            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() -> ta.appendText("Server started at " + new Date() + '\n'));

                // Listen for a connection request
                Socket socket = serverSocket.accept();

                // Create data input and output streams
                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

                while (true) {
                    // Receive number from the client
                    double num = inputFromClient.readDouble();

                    // Display to text area
                    ta.appendText("Number received from client: " + num + "\n");

                    // Check if number is prime and send respones back to client
                    if (isPrime((int) num)) {

                        outputToClient.writeUTF("The number is prime");
                    } else {
                        outputToClient.writeUTF("The number is not prime");
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    static boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        for (int i = 2; i <= num / 2; i++) {
            if ((num % i) == 0) return false;
        }
        return true;
    }

    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
