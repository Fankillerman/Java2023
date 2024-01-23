package org.chatbot.server;

import org.chatbot.database.DatabaseConnection;
import org.chatbot.logic.ChatbotLogic;
import org.chatbot.response.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ChatbotLogic chatbotLogic;

    public ClientHandler(Socket socket) throws IOException, SQLException {
        this.clientSocket = socket;
        this.chatbotLogic = new ChatbotLogic(new DatabaseConnection("jdbc:mysql://localhost/chatbot_db", "chatbot-app", "fjbh124555b&jkkj@@232"));
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                Response response = chatbotLogic.processInput(inputLine);
                out.println(response.getMessage());
                out.flush(); // Принудительная отправка данных
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            chatbotLogic.exit();
        }
    }
}
