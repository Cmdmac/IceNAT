package com.hankcs.network;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.ice4j.StackProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.URISyntaxException;


public class Peer
{
    public void start() {
        try
        {
            System.setProperty(StackProperties.DISABLE_IPv6, "true");
            IceClient client = new IceClient(8888, "text");
            client.init();
            client.exchangeSdpWithPeer();
            client.startConnect();
            final DatagramSocket socket = client.getDatagramSocket();
            final SocketAddress remoteAddress = client
                    .getRemotePeerSocketAddress();
            System.out.println(socket.toString());
            new Thread(new Runnable()
            {

                public void run()
                {
                    while (true)
                    {
                        try
                        {
                            byte[] buf = new byte[1024];
                            DatagramPacket packet = new DatagramPacket(buf,
                                    buf.length);
                            socket.receive(packet);
                            System.out.println(packet.getAddress() + ":" + packet.getPort() + " says: " + new String(packet.getData(), 0, packet.getLength()));
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            new Thread(new Runnable()
            {

                public void run()
                {
                    try
                    {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                        String line;
                        // 从键盘读取
                        while ((line = reader.readLine()) != null)
                        {
                            line = line.trim();
                            if (line.length() == 0)
                            {
                                break;
                            }
                            byte[] buf = (line).getBytes();
                            DatagramPacket packet = new DatagramPacket(buf, buf.length);
                            packet.setSocketAddress(remoteAddress);
                            socket.send(packet);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void test() {

    }

    public static void main(String[] args) throws URISyntaxException {
        Socket socket = IO.socket("http://localhost:8080");
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                socket.emit("news", "hi");
//                socket.disconnect();
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                System.out.println("disconnect");
            }

        }).on(Socket.EVENT_MESSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                System.out.println(objects[0]);
            }
        });
        socket.connect();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                socket.emit("join", "{\"sdp\":\"aaaaaaa\"}");
            }
        }).start();
    }

}