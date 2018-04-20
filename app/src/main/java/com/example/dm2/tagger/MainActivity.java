package com.example.dm2.tagger;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {

    private String etiquetas;
    TextView txtEtiqueta;
    private Socket conexion;
    private DataOutputStream dos;
    private String ip="213.254.95.118";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtEtiqueta=(TextView)findViewById(R.id.txtEtiqueta);
        etiquetas="";
    }


    public void conectar(View v){
        if(etiquetas.equals("")){
            Toast.makeText(getApplicationContext(), "Elige etiquetas para conectar", Toast.LENGTH_LONG).show();
        }else{
            myTask mt=new myTask();
            mt.execute();
            Toast.makeText(getApplicationContext(),"Etiquetas enviadas",Toast.LENGTH_LONG).show();
        }
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return(inetAddress.getHostAddress().toString());
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void tagg(View v){

        Intent in=new Intent(MainActivity.this,ListaTaggs.class);
        startActivityForResult(in,1);

    }

    protected void onActivityResult (int requestCode, int resultCode,
                                     Intent data){
        if (requestCode==1 && resultCode==RESULT_OK ) {
            txtEtiqueta.setText("");
            etiquetas = data.getExtras().getString("resultado");

            String [] strPartes=etiquetas.split("#");

            for(int i=1;i<strPartes.length;i++){
                txtEtiqueta.append("#"+strPartes[i]+"\n");
            }
        }
    }

    class myTask extends AsyncTask<Void,Void,Void> {

        protected Void doInBackground(Void... params){
            try{
                conexion=new Socket(ip,6780);
                dos = new DataOutputStream(conexion.getOutputStream());
                dos.writeUTF(etiquetas+":"+getLocalIpAddress());
                dos.close();
                //s.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

    }


}
