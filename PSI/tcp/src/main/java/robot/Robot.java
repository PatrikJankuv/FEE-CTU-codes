package robot;

// Java implementation of  Server side 
// It contains two classes : Server and ClientHandler 
// Save file as Server.java 
import java.io.*;
import java.util.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

// Server class 
class Server {

    public static void main_server(int port) throws IOException {
        // server is listening on port 
        ServerSocket ss = new ServerSocket(port);
        System.out.println("Server is running on port: " + port);

        // running infinite loop for getting 
        // client request 
        while (true) {
            Socket s = null;

            try {
                // socket object to receive incoming client requests 
                s = ss.accept();

                System.out.println("A new client is connected : " + s);

                // obtaining out streams 
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

               
                // create a new thread object 
                Thread t = new ClientHandler(s, dos);

                // Invoking the start() method 
                t.start();

            } catch (IOException e) {
                s.close();
                e.printStackTrace();
            }
        }
    }

}

// ClientHandler class 
// for multitasking
class ClientHandler extends Thread {

    final Timer timer;
    final DataOutputStream dos;
    final Socket s;
    final BufferedInputStream buf;
    final static int TIMERSECONDS = 45;
    static final Logger LOG = Logger.getLogger("ClientHandler");

    ;

    // Constructor 
    public ClientHandler(Socket s, DataOutputStream dos) throws IOException {
        this.s = s;
        this.dos = dos;
        this.timer = new Timer();
        this.buf = new BufferedInputStream(s.getInputStream());
    }

    @Override
    public void run() {
        //45 seconds
        long start = System.currentTimeMillis();
        long end = start + TIMERSECONDS * 1000; //45

        while (System.currentTimeMillis() < end) {
            boolean answer;
            //robot validation
            try {
                answer = loginSession();
            } catch (IOException ex) {
                System.err.print("Login failed");
                break;
            }

            while (answer) {

                try {
                    int current;

                    try {
                        current = buf.read();
                    } catch (IOException ex) {
                        sendMessage(501);
                        current = '0';
                    }

                    switch (current) {
                        case 'I':
                            answer = readInfo();
                            break;
                        case 'F':
                            answer = readFoto();
                            break;
                        default:
                            answer = false;
                            sendMessage(501);
                    }

                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                }

            }

            if (System.currentTimeMillis() >= end) {
                sendMessage(502);
            }

            try {
                // closing resources 
                this.buf.close();
                this.dos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeConnection() {
        System.out.println("Client " + this.s + " sends exit...");
        System.out.println("Closing this connection.");
        try {
            this.s.close();
            this.buf.close();
            this.dos.close();
        } catch (IOException ex) {
            System.err.print("close connection failed");

        }
        System.out.println("Connection closed");
    }

    //login session
    private boolean loginSession() throws IOException {
        int passFromClient;
        int newUser;

        //send user to client and read login
        sendMessage(200);
        newUser = readUsername();

        //send user to client and read password
        sendMessage(201);
        passFromClient = readPAssword();

        //compare pass anduser
        if (newUser != -1 && newUser == passFromClient) {
            sendMessage(202);
            return true;
        } else {
            sendMessage(500);
            return false;
        }

    }

    //read info
    private boolean readInfo() throws IOException {
        StringBuilder info = new StringBuilder();
        int current;
        int last = 0;

        info.append('I');

        //validate if is it "INFO "
        current = buf.read();
        if (current != 'N') {
            sendMessage(501);
            return false;
        }
        info.append((char) current);

        current = buf.read();
        if (current != 'F') {
            sendMessage(501);
            return false;
        }
        info.append((char) current);

        current = buf.read();
        if (current != 'O') {
            sendMessage(501);
            return false;
        }
        info.append((char) current);

        current = buf.read();
        if (current != ' ') {
            sendMessage(501);
            return false;
        }
        info.append((char) current);

        do {
            // read the input
            current = buf.read();
            info.append((char) current);

            // escape sequence met and remove \r from info
            if (last == '\r' && current == '\n') {
                info.deleteCharAt(info.length() - 1);
                break;
            }

            last = current;

        } while (current != -1);

//        LOG.info(info.toString());
        sendMessage(202);
        return true;
    }

    //read foto
    private boolean readFoto() throws IOException {
        int current;

        //validate if is it "FOTO "
        current = buf.read();
        if (current != 'O') {
            sendMessage(501);
            return false;
        }

        current = buf.read();
        if (current != 'T') {
            sendMessage(501);
            return false;
        }

        current = buf.read();
        if (current != 'O') {
            sendMessage(501);
            return false;
        }

        current = buf.read();
        if (current != ' ') {
            sendMessage(501);
            return false;
        }

        try {
            int fotoLength;
            fotoLength = readPhotoLength();

            long calcSum = readPhoto(buf, fotoLength);
            long receivedChecksum = readCheckSum(buf);

            //compare sum
            if (calcSum == receivedChecksum) {
                sendMessage(202);
                return true;
            } else {
                sendMessage(300);
                return true;
            }
        } catch (IOException ex) {
            sendMessage(501);
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            closeConnection();
        }

        return true;
    }

    private long readCheckSum(BufferedInputStream input) {
        int checkSum;
        byte[] array = new byte[4];

        for (int i = 0; i < 4; i++) {
            try {
                array[i] = (byte) buf.read();
            } catch (IOException ex) {
                sendMessage(501);
                closeConnection();
            }
        }
        checkSum = ByteBuffer.wrap(array).getInt();

        return checkSum;
    }

    private long readPhoto(BufferedInputStream input, int numberOfBytes) throws IOException {
        int counter = 0;
        int current = 0;
        long calculatedChecksum = 0;

        // Prepare file
        // Calculate checksum and save the photo to file
        while (current != -1 && counter < numberOfBytes) {
            // Read the input
            current = input.read();
            calculatedChecksum += current;
            counter++;
        }

        return calculatedChecksum;
    }

    private int readPhotoLength() throws IOException {
        int current;
        StringBuilder passInput = new StringBuilder();

        do {
            //read every char
            current = buf.read();

            //escape sequence, 
            //current == ' ', for foto length read
            if (current == ' ') {
                break;
            }

            //chech if char is digit and add to pass
            if (Character.isDigit(current)) {
                passInput.append((char) current);
            } else {
                sendMessage(501);
                closeConnection();
            }

        } while (current != -1);

        try {
            int passInt = Integer.parseInt(passInput.toString());
            return passInt;
        } catch (Exception ex) {
            return -1;
        }
    }

    //read password
    private int readPAssword() throws IOException {
        int last = 0;
        int current;
        StringBuilder passInput = new StringBuilder();

        do {
            //read every char
            current = buf.read();

            //escape sequence, 
            //current == ' ', for foto length read
            if ((current == '\n' && last == '\r')) {
                break;
            }

            //for escape sequence
            last = current;

            //chech if char is digit and add to pass
            if (Character.isDigit(current)) {
                passInput.append((char) current);
            }

        } while (current != -1);

        try {
            int passInt = Integer.parseInt(passInput.toString());
            return passInt;
        } catch (Exception ex) {
            return 1;
        }

    }

    //method for reading login name
    private int readUsername() throws IOException {
        int last = 0;
        int calcPass = 0;
        int current;
        int counter = 0;
        StringBuilder user = new StringBuilder();

        do {
            // read every char
            current = buf.read();

            // save the first 5 chars of the login
            if (counter++ < 5) {
                user.append((char) current);
            }

            // escape sequence met, subtract '\r' value from calculated password's value
            if (current == '\n' && last == '\r') {
                calcPass -= last;
                break;
            }

            // iterate
            last = current;
            calcPass += current;

        } while (current != -1);

        try {
            String firstFive = user.toString();

            if (isItRobot(firstFive)) {
                return calcPass;
            } else {
                //if not robot but send good password
                return -1;
            }

        } catch (Exception ex) {
            return -1;
        }

    }
    
    //check if client is robot
    private boolean isItRobot(String user) {
        if (user.length() > 4) {
            return user.substring(0, 5).equals("Robot");
        } else {
            return false;
        }
    }
    
    //sending user to client
    private void sendMessage(int i) {
        try {
            String message = "";

            switch (i) {
                case 200:
                    message = "200 LOGIN\r\n";
                    break;
                case 201:
                    message = "201 PASSWORD\r\n";
                    break;
                case 202:
                    message = "202 OK\r\n";
                    break;
                case 300:
                    message = "300 BAD CHECKSUM\r\n";
                    break;
                case 500:
                    message = "500 LOGIN FAILED\r\n";
                    break;
                case 501:
                    message = "501 SYNTAX ERROR\r\n";
                    break;
                case 502:
                    message = "502 TIMEOUT\r\n";
                    break;
            }

            dos.write(message.getBytes());
            dos.flush();
        } catch (IOException ex) {
            System.err.println("Connection closed");
        }
    }


}


public class Robot {

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.err.println("Client: java robot.Robot <hostname> <port>");
            System.err.println("Server: java robot.Robot <port>");
            System.exit(1);
        }
        if (args.length > 1) {
            System.out.println("Starting client...\n");
        } else {
            System.out.println("Starting server...\n");
            try {
                Server.main_server(Integer.parseInt(args[0]));
            } catch (Exception e) {
                Server.main_server(Integer.parseInt(args[0]));
            }

        }
    }
}
