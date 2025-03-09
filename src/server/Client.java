package server;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 1337;

    public static void main(String[] args) {
        if (args.length % 2 != 0) {
            System.out.println("Kasutamine: java server.Client <request_type> <value> ...");
            return;
        }

        try (Socket socket = new Socket(HOST, PORT);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            System.out.println("Ühendus serveriga loodud!");
            int requestCount = args.length / 2;
            out.writeInt(requestCount);

            for (int i = 0; i < args.length; i += 2) {
                String requestType = args[i];
                String payload = args[i + 1];

                int type = requestType.equalsIgnoreCase("echo") ? 1 :
                        requestType.equalsIgnoreCase("file") ? 2 : -1;
                if (type == -1) {
                    System.out.println("Tundmatu päringutüüp: " + requestType);
                    continue;
                }

                byte[] payloadData = payload.getBytes(StandardCharsets.UTF_8);
                System.out.println("Saadan päringu: " + requestType + " -> " + payload);
                out.writeInt(type);
                out.writeInt(payloadData.length);
                out.write(payloadData);

                int status = in.readInt();
                if (status == 0) { // OK
                    int length = in.readInt();
                    byte[] responseData = new byte[length];
                    in.readFully(responseData);
                    String response = new String(responseData, StandardCharsets.UTF_8);

                    if (type == 1) {
                        System.out.println("Echo vastus: " + response);
                    } else if (type == 2) {
                        Path receivedDir = Paths.get("received");
                        if (!Files.exists(receivedDir)) {
                            Files.createDirectory(receivedDir);
                        }
                        Path filePath = receivedDir.resolve(payload);
                        Files.write(filePath, responseData);
                        System.out.println("Fail salvestatud: " + filePath);
                    }
                } else {
                    System.out.println("Viga päringuga: " + payload);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
