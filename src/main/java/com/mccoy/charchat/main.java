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

    static List<ChatMessage> chatRoom = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws IOException
    {

        if (args.length < 1 || args[0].isEmpty())
        {
            System.out.println("Usage: [server|client]");
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
            new ChatSocket(serverSocket.accept()).start();
        }
    }

    public static void startClient() throws IOException
    {
        System.out.println("Starting CharChat client...");
        Socket socket = new Socket("localhost", 10888);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        out.println("## connected ##");
        String userInput;
        System.out.println(in.readLine());
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
            userInput = stdIn.readLine();
            if (userInput != null)
            {
                out.println(userInput);
            }
        }
    }

    static class ChatSocket extends Thread
    {

        UUID uuid = UUID.randomUUID();
        Socket socket;

        ChatSocket(Socket socket)
        {
            this.socket = socket;
        }

        @Override
        public void run()
        {
            PrintWriter out = null;
            try
            {
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("Welcome to CharChat! Your UUID is " + uuid.toString());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String clientMessage;
                while (true)
                {
                    if ((clientMessage = in.readLine()) != null)
                    {
                        if (".".equals(clientMessage))
                        {

                            chatRoom.add(new ChatMessage(getTimeStamp(), uuid.toString(), " Has disconnected!"));
                            out.println("good bye");
                            System.out.println(chatRoom.get(chatRoom.size() - 1));
                            break;
                        }
                        chatRoom.add(new ChatMessage(getTimeStamp(), uuid.toString(), clientMessage));
                        out.println(chatRoom.toString());
                        System.out.println(chatRoom.get(chatRoom.size() - 1));
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

    }

}