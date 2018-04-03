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
        new Peer().start();
    }

}