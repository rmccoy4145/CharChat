package com.mccoy.charchat;

public class ChatMessage
{
    private String message;
    private String senderUUID;
    private String timestamp;

    public ChatMessage(String timestamp,String senderUUID, String message )
    {
        this.message = message;
        this.senderUUID = senderUUID;
        this.timestamp = timestamp;
    }

    public String toString()
    {
        return String.format("[%s]%s: %s", timestamp, "Client_" + senderUUID, message);
    }
}
