package server;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class Server {
    private static final int PORT = 1337;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server kuulab pordil " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream())
            ) {
                int requestCount = in.readInt();
                System.out.println("Kliendilt saadud " + requestCount + " päringut.");

                for (int i = 0; i < requestCount; i++) {
                    int requestType = in.readInt();
                    int length = in.readInt();
                    byte[] data = new byte[length];
                    in.readFully(data);
                    String payload = new String(data, StandardCharsets.UTF_8);

                    if (requestType == 1) { // Echo päring
                        System.out.println("ECHO: " + payload);
                        out.writeInt(0); // Status OK
                        out.writeInt(payload.getBytes(StandardCharsets.UTF_8).length);
                        out.write(payload.getBytes(StandardCharsets.UTF_8));

                    } else if (requestType == 2) { // Faili päring
                        System.out.println("FAILI PÄRING: " + payload);
                        Path filePath = Paths.get(payload);

                        // Kontrollime, kas fail on olemas ja loetav
                        if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
                            System.out.println("Viga: Faili ei leitud või see ei ole regulaarne fail.");
                            out.writeInt(1); // Error
                        } else {
                            byte[] fileData = Files.readAllBytes(filePath);
                            out.writeInt(0); // Status OK
                            out.writeInt(fileData.length);
                            out.write(fileData);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
