/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robot;

/**
 *
 * @author patrik
 */
// A Java program for a Client 
import java.net.*;
import java.io.*;

public class Client {

    // initialize socket and input output streams 
    private Socket socket = null;
    private DataInputStream input = null;
    private DataInputStream serverBack = null;
    private DataOutputStream out = null;

    // constructor to put ip address and port 
    public Client(String address, int port) throws IOException {
        // establish a connection 
        try {
            socket = new Socket(address, port);

            // takes input from terminal 
            input = new DataInputStream(System.in);

            // sends output to the socket 
            out = new DataOutputStream(socket.getOutputStream());
            serverBack = new DataInputStream(socket.getInputStream());
        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException i) {
            System.out.println(i);
        }

        // string to read message from input 
        String line = "";

//        String message = "12";
        String message = "";
        // keep reading until "Over" is input 
        boolean condition = true;
        message = serverBack.readLine();
        System.out.println(message);
        out.write("Robot\r\n".getBytes());

        message = serverBack.readLine();
        System.out.println(message);
        out.write("518\r\n".getBytes());

        message = serverBack.readLine();
        System.out.println(message);

        
        while (true) {
            try {

                try {
                    if (message.contains("3")) {
                        message = serverBack.readLine();
                        System.out.println(message);
                        break;
                    }
                    if (message.contains("5")) {
                        break;
                    }
                    
                } catch (Exception e) {

                }

                line = input.readLine();

                line = line + "\r\n";
                out.write(line.getBytes());

                message = serverBack.readLine();
                System.out.println(message);

            } catch (IOException i) {
                System.out.println(i);
            }
        }

        // close the connection 
        try {
            input.close();
            out.close();
            socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) throws IOException {
        Client client = new Client("127.0.0.1", 3998);
    }
}

// FOTO 8 ABCDEFGH\x00\x00\x02\x24
