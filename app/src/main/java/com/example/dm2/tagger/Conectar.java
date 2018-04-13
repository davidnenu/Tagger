package com.example.dm2.tagger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by dm2 on 13/04/2018.
 */

public class Conectar {


    public void conectar(){

        Socket sock;
        try {
            sock = new Socket("172.20.203.113", 1149);
            System.out.println("Connecting...");

            // sendfile
            String hola="hola";
            byte [] mybytearray  = hola.getBytes();
            OutputStream os = sock.getOutputStream();
            System.out.println("Sending...");
            os.write(mybytearray,0,mybytearray.length);
            os.flush();

            sock.close();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}
