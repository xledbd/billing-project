package com.xledbd;

import com.xledbd.dao.SessionFactorySingleton;

import java.net.ServerSocket;
import java.util.Date;

public class App 
{
    public static void print_log(String msg)
    {
        System.out.printf("[%tT] %s\n", new Date(), msg);
    }

    public static void main( String[] args ) {
        SessionFactorySingleton.getInstance();
        ServerSocket serverSocket = null;
        try {
            print_log("Server starting...");
            serverSocket = new ServerSocket(2525);
            while (true) {
                Thread t = new Thread(new ClientThread(serverSocket.accept()));
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            print_log("Server terminating...");
            try {
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
