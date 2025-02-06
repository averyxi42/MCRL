package com.example.examplemod;

import java.io.IOException;
import java.net.InetSocketAddress;
// import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class ActionListener {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private Scanner sc;
    public ActionListener(int port) throws IOException {
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

        System.out.println("Action Listener started listening");
    }

    public void read() throws IOException{
        // Check if any registered channels are ready for I/O operations
        // int readyChannels = selector.selectNow();

        // if (readyChannels == 0) {
        //     return;
        // }
        // // Get the set of keys that are ready
        // Set<SelectionKey> selectedKeys = selector.selectedKeys();
        // Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
        if(sc ==null) return;
        
        while(sc.hasNextLine()){
            String input_type = sc.nextLine();
            System.out.println(input_type);
            if(input_type.equals("mouse")){
            }
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

        while (keyIterator.hasNext() & sc==null) {
            SelectionKey key = keyIterator.next();
            if (key.isAcceptable()) {
                // Accept the new connection
                ServerSocketChannel server = (ServerSocketChannel) key.channel();
                SocketChannel client = server.accept();
                
                // // Set the client channel to non-blocking
                client.configureBlocking(false);
                // // Register the client with the selector for OP_READ events
                // client.register(selector, SelectionKey.OP_READ);
                sc = new Scanner(client);

                System.out.println("Accepted a new connection from: " + client.getRemoteAddress());
            } 
        }
        
    }

    public static void main(String[] args){
        System.out.println("hi");
        ActionListener listener;
        try {
            listener = new ActionListener(8083);
            while(true){
                listener.handle_connections();
                listener.read();

            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }

    }

}
