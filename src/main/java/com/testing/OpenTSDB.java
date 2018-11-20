/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testing;


/**
 *
 * @author sehzad
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Random;


public class OpenTSDB {
    
    
    public static void main(String[] args) throws Exception 
    {
        Socket echoSocket = null;
        PrintWriter out = null;
        String host = null;
        int port = 0;

        Random randomGenerator = new Random();

        while (true) {
           try {
               if (echoSocket == null) {  // Only open the connection if necessary.
                    echoSocket = new Socket("localhost", 4242);
                    out = new PrintWriter(echoSocket.getOutputStream(), true);
               }

               int orderCount = randomGenerator.nextInt(100);
               String s = "put MyRandomData " + (new Date().getTime()) + " " + orderCount + " cpu=1\n";

               System.out.println(s);
               out.println(s);
               out.flush();
           } catch (IOException e) {
               System.err.println("Couldn't get I/O for the connection to: " + host);
               out.close();
               echoSocket.close();
               out = null;
               echoSocket = null;
           }

           Thread.sleep(500);
       }
    }

                     

    
    
    
}


