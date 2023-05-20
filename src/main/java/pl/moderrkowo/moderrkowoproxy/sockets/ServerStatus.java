package pl.moderrkowo.moderrkowoproxy.sockets;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerStatus {

    private final String ip;
    private final int port;

    private String name;
    private String status;
    private int playerCount;
    private int maxPlayersCount;

    public ServerStatus(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getOnline() {
        return playerCount;
    }

    public int getMaxPlayers() {
        return maxPlayersCount;
    }

    public String getStatus() {
        return status;
    }

    public void update() throws IOException {

        Socket socket = new Socket();
        OutputStream outputStream;
        DataOutputStream dataOutputStream;
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        socket.setSoTimeout(1000);

        socket.connect(new InetSocketAddress(ip, port), 1000);
        outputStream = socket.getOutputStream();
        dataOutputStream = new DataOutputStream(outputStream);
        inputStream = socket.getInputStream();
        inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_16BE);
        dataOutputStream.write(new byte[]{(byte) 0xFE, (byte) 0x01});
        int packetId = inputStream.read();

        if (packetId == -1) {
            dataOutputStream.close();
            outputStream.close();
            inputStreamReader.close();
            inputStream.close();
            socket.close();
            throw new IOException("Premature end of stream");
        }
        if (packetId != 0xFF) {
            dataOutputStream.close();
            outputStream.close();
            inputStreamReader.close();
            inputStream.close();
            socket.close();
            throw new IOException("Invalid packet ID (" + packetId + ").");
        }

        int length = inputStreamReader.read();

        if (length == -1) {
            dataOutputStream.close();
            outputStream.close();
            inputStreamReader.close();
            inputStream.close();
            socket.close();
            throw new IOException("Premature end of stream");
        }
        if (length == 0) {
            dataOutputStream.close();
            outputStream.close();
            inputStreamReader.close();
            inputStream.close();
            socket.close();
            throw new IOException("Invalid string length");
        }

        char[] chars = new char[length];

        if (inputStreamReader.read(chars, 0, length) != length) {
            dataOutputStream.close();
            outputStream.close();
            inputStreamReader.close();
            inputStream.close();
            socket.close();
            throw new IOException("Premature end of stream.");
        }

        String string = new String(chars);
        if (string.startsWith("§")) {
            String[] data = string.split("\0");
            String motd = data[3];

            int onlinePlayers = Integer.parseInt(data[4]);
            int maxPlayers = Integer.parseInt(data[5]);
            this.status = motd;
            this.playerCount = onlinePlayers;
            this.maxPlayersCount = maxPlayers;


            dataOutputStream.close();
            outputStream.close();
            inputStreamReader.close();
            inputStream.close();
            socket.close();
        }
    }
}