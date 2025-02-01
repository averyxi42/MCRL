package com.example.examplemod;

// import java.net.*;
// import java.io.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class BroadcastServer {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    public BroadcastServer(int port) throws IOException {
        // Create a ServerSocketChannel
        serverSocketChannel = ServerSocketChannel.open();

        // Set it to non-blocking mode
        serverSocketChannel.configureBlocking(false);

        // Bind to a specific port
        serverSocketChannel.bind(new InetSocketAddress(port));

        // Create a Selector
        selector = Selector.open();
        // Register the channel with the selector for OP_ACCEPT events
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server started listening");
    }

    public void broadcast(ByteBuffer msg) throws IOException{
        // Check if any registered channels are ready for I/O operations
        int readyChannels = selector.selectNow();

        if (readyChannels == 0) {
            return;
        }
        // Get the set of keys that are ready
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectedKeys.iterator();


        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();

            if (key.isWritable()) {
                SocketChannel channel = (SocketChannel) key.channel();
                try {
                    channel.write(msg);

                } catch (IOException e) {
                    key.cancel();
                    throw e;
                    // TODO: handle exception
                }
            } 
            // Remove the processed key from the set
            keyIterator.remove();
        }
    }

    public void handle_connections() throws IOException{
        // Check if any registered channels are ready for I/O operations
        int readyChannels = selector.selectNow();

        if (readyChannels == 0) {
            return;
        }
        // Get the set of keys that are ready
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();

            if (key.isAcceptable()) {
                // Accept the new connection
                ServerSocketChannel server = (ServerSocketChannel) key.channel();
                SocketChannel client = server.accept();

                // Set the client channel to non-blocking
                client.configureBlocking(false);

                // Register the client with the selector for OP_READ events
                client.register(selector, SelectionKey.OP_WRITE);

                System.out.println("Accepted a new connection from: " + client.getRemoteAddress());
            } 
            // Remove the processed key from the set
            keyIterator.remove();
        }
        
    }
}

// // Demonstrating Server-side Programming

// public class VideoServer {
  
//     // Initialize socket and input stream
//     private Socket s = null;
//     private ServerSocket ss = null;
//     private DataInputStream in = null;

//     // Constructor with port
//     public VideoServer(int port) {
      
//         // Starts server and waits for a connection
//         try
//         {
//             ss = new ServerSocket(port);
//             System.out.println("Server started");

//             System.out.println("Waiting for a client ...");

//             s = ss.
//             System.out.println("Client accepted");
            
//             s.
//             // Takes input from the client socket
//             in = new DataInputStream(
//                 new BufferedInputStream(s.getInputStream()));

//             String m = "";

//             // Reads message from client until "Over" is sent
//             while (!m.equals("Over"))
//             {
//                 try
//                 {
//                     m = in.readUTF();
//                     System.out.println(m);

//                 }
//                 catch(IOException i)
//                 {
//                     System.out.println(i);
//                 }
//             }
//             System.out.println("Closing connection");

//             // Close connection
//             s.close();
//             in.close();
//         }
//         catch(IOException i)
//         {
//             System.out.println(i);
//         }
//     }

//     public static void main(String args[])
//     {
//         Server s = new Server(5000);
//     }
// }
