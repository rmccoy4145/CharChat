package com.mccoy.charchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.*;

public class main
{

    static List<ChatHandler> chatHandlers = new ArrayList<>();


    public static void main(String[] args) throws IOException
    {

        if (args.length < 1 || args[0].isEmpty())
        {
            System.out.println("Usage: [server|client|viewclient]");
            System.exit(1);
        }

        String mode = args[0];

        mode = mode.toLowerCase().trim();
        switch (mode)
        {
            case "server":
                startServer();
                break;
            case "client":
                startClient();
                break;
            default:
                System.out.println("Please enter a valid mode");
                System.exit(1);
        }

    }

    public static void startServer() throws IOException
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

    public static void startClient() throws IOException
    {
        System.out.println("Starting CharChat client...");
        Socket socket = new Socket("localhost", 10888);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println("## connected ##");
        System.out.println(in.readLine());
        InputHandler inputHandler = new InputHandler(out);
        inputHandler.start();
        while(true)
        {
            if(!socket.isConnected())
            {
                System.out.println("Server disconnected");
                System.exit(1);
            }
            String serverResponse = in.readLine();
            if (serverResponse != null)
            {
                System.out.println(serverResponse);
            }
        }
    }

    static class InputHandler extends Thread
    {
        private BufferedReader in;
        private PrintWriter out;

        public InputHandler(PrintWriter out)
        {
            this.in = new BufferedReader(new InputStreamReader(System.in));
            this.out = out;
        }

        @Override
        public void run()
        {
            String userInput;
            try
            {
                while ((userInput = in.readLine()) != null)
                {
                    out.println(userInput);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    static class ChatHandler extends Thread
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
        static String getTimeStamp()
        {
            return Instant.now().toString();
        }

        public void sendMessage(ChatMessage message)
        {
            out.println(message.toString());
        }

    }

}