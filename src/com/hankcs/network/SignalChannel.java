package com.hankcs.network;

import java.io.*;
import java.net.Socket;

public class SignalChannel {
    Socket mSocket;
    OutputStream mChannelOutput;
//    InputStream mChannelInput;

    public void send(String msg) {
        try {
            mChannelOutput.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onReceive(String data) {
        System.out.println(data);
    }

    public void start(String host, int port) {
        try {
            //建立socket服务。指定连接的主机和port
            mSocket = new Socket(host, port);
            //获取socket流中的输出流。将数据写入该流，通过网络传送给服务端
            mChannelOutput = (OutputStream) mSocket.getOutputStream();
            //获取socket流中的输入流。将服务端反馈的数据获取到，并打印
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream in = mSocket.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                        String line = "";
                        StringBuilder stringBuilder = new StringBuilder();
                        while ((line = bufferedReader.readLine()) != null) {
                            System.out.println(line);
                            stringBuilder.append(line);
                            if (line.equals("bounder--bounder")) {
                                String buffer = stringBuilder.toString();
                                onReceive(buffer);
                                stringBuilder.trimToSize();
                            }
                        }
                        bufferedReader.close();
                        in.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();

//            System.out.println(new String(buf,0,len));
//            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
