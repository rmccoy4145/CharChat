package com.mccoy.charchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient
{

    public void startClient() throws IOException
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

    class InputHandler extends Thread
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
}
