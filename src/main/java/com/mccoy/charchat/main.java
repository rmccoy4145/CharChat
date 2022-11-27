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
                new ChatServer().startServer();
                break;
            case "client":
                new ChatClient().startClient();
                break;
            default:
                System.out.println("Please enter a valid mode");
                System.exit(1);
        }

    }

}