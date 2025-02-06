package com.example.examplemod;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
// import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.util.ByteBufferBackedInputStream;

public class LineListener {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private ByteBuffer buf = ByteBuffer.allocate(1000);
    private ByteBufferBackedInputStream buffer =new ByteBufferBackedInputStream(buf);//= "";
    private Scanner sc = new Scanner(buffer);
    private Consumer<String> action;
    
    public LineListener(int port,Consumer<String> line_action) throws IOException {
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
        this.action = line_action;
    }

    public LineListener(int port)throws IOException{
        this(port, a -> System.out.println(a));
    }

    public void read() throws IOException{
        int readyChannels = selector.selectNow();

        if (readyChannels == 0) {
            return;
        }
        // Get the set of keys that are ready
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();
            buf.clear();
            if (key.isReadable()) {
                SocketChannel channel = (SocketChannel) key.channel();
                try {
                    ByteBuffer tmp = ByteBuffer.allocate(512);
                    channel.read(buf);
                    // System.out.print(tmp.asCharBuffer());

                } catch (IOException e) {
                    key.cancel();
                    throw e;
                    // TODO: handle exception
                }
            } 
            // Remove the processed key from the set
            keyIterator.remove();
        }
        buf.flip();
        sc = new Scanner(StandardCharsets.US_ASCII.decode(buf).toString());
        // buf.flip();
        // System.out.println(buf.asCharBuffer());
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            this.action.accept(line);
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
                
                // // Set the client channel to non-blocking
                client.configureBlocking(false);
                // // Register the client with the selector for OP_READ events
                client.register(selector, SelectionKey.OP_READ);

                System.out.println("Accepted a new connection from: " + client.getRemoteAddress());
            } 
            keyIterator.remove();
        }
        
    }

    public static void main(String[] args){
        System.out.println("hi");
        LineListener listener;
        try {
            listener = new LineListener(8083);
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
