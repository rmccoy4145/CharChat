package com.mccoy.charchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatServer
{
    static List<ChatHandler> chatHandlers = new ArrayList<>();

    public void startServer() throws IOException
    {
        System.out.println("Starting CharChat server...");
        ServerSocket serverSocket = new ServerSocket(10888);
        while(true)
        {
            ChatHandler chatHandler = new ChatHandler(serverSocket.accept());
            chatHandlers.add(chatHandler);
            chatHandler.start();
        }
    }

    public static void broadcastMessage(ChatMessage message)
    {
        for (ChatHandler handler : chatHandlers)
        {
            handler.sendMessage(message);
        }
    }

    class ChatHandler extends Thread
    {

        UUID uuid = UUID.randomUUID();
        Socket socket;
        PrintWriter out;

        ChatHandler(Socket socket)
        {
            this.socket = socket;
        }

        @Override
        public void run()
        {

            try
            {
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("Welcome to CharChat! Your UUID is " + uuid.toString());
                System.out.println("Client connected: " + uuid.toString());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String clientMessage;
                while (true)
                {
                    if ((clientMessage = in.readLine()) != null)
                    {
                        if (".".equals(clientMessage))
                        {

                            broadcastMessage(new ChatMessage(getTimeStamp(), uuid.toString(), " Has disconnected!"));
                            System.out.println("Client disconnected: " + uuid.toString());
                            break;
                        }
                        broadcastMessage(new ChatMessage(getTimeStamp(), uuid.toString(), clientMessage));
                    }
                }
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        String getTimeStamp()
        {
            return Instant.now().toString();
        }

        public void sendMessage(ChatMessage message)
        {
            out.println(message.toString());
        }

    }
}
