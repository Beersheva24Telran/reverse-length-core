package telran.net;

import java.net.*;

import org.json.JSONObject;

import java.io.*;

public class Main {
    private static final int PORT = 4000;

    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(PORT);) {
            System.out.println("Server is listening on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                runSession(socket);
            }
        }
    }

    private static void runSession(Socket socket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintStream writer = new PrintStream(socket.getOutputStream())) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                String response = getResponse(line);
                writer.println(response);
            }
        } catch (Exception e) {
            System.out.println("client closed connection abnormally");
        }
    }

    private static String getResponse(String line) {
        JSONObject jsonObj = new JSONObject(line);
        String type = jsonObj.getString("type");
        String string = jsonObj.getString("string");
        String res = switch(type){
            case "reverse" -> new StringBuilder(string).reverse().toString();
            case "length" -> string.length() + "";
            default -> type + " Wrong type";
        };
        return res;
    }
}