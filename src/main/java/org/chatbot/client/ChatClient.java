package org.chatbot.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader userInputReader;

    public ChatClient(String address, int port) throws Exception {
        socket = new Socket(address, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        userInputReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void send(String message) {
        out.println(message);
    }

    public String receive() throws Exception {
        return in.readLine();
    }

    public void close() throws Exception {
        in.close();
        out.close();
        socket.close();
        userInputReader.close();
    }

    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient("localhost", 3307);

        System.out.println("Connected to chatbot. Type your messages:");
        String userInput;
        while ((userInput = client.userInputReader.readLine()) != null) {
            client.send(userInput);
            System.out.println("Chatbot says: " + client.receive());
        }
        client.close();
    }
}
