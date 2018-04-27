package com.example.dm2.tagger;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private String etiquetas;
    TextView txtEtiqueta;
    private Socket conexion;
    private DataOutputStream dos;
    private DataInputStream dis;
    private String ip="213.254.95.118";
    private String s="";
    private Uri fileUri;
    Bitmap bmp;
    byte[] byteArray;

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

            // Check Camera
            if (getApplicationContext().getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA)) {
                // Open default camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                // start the image capture Intent
                startActivityForResult(intent, 100);


                myTask mt=new myTask();
                mt.execute();
                //Toast.makeText(getApplicationContext(),"Etiquetas enviadas",Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(),"Recibido:" + s, Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
            }



        }
    }



    public void tagg(View v){

        Intent in=new Intent(MainActivity.this,ListaTaggs.class);
        startActivityForResult(in,1);

    }


    class myTask extends AsyncTask<String,Void,String> {

        protected String doInBackground(String... params){
            try{
                DataInputStream flujoEntrada = null;

                conexion=new Socket(ip,6780);
                dos = new DataOutputStream(conexion.getOutputStream());
                dos.writeUTF(etiquetas);

                //Enviar bytes del bitmap

                try {
                    flujoEntrada = new DataInputStream(conexion.getInputStream());
                    // Recibimos
                    int i = flujoEntrada.readInt();
                    dos.write(byteArray);
                } catch (java.net.SocketException e) {
                }




                dos.close();
                flujoEntrada.close();

            }catch (IOException e){
                e.printStackTrace();
            }
            return s;
        }
        protected void onPostExecute(String s2){
            txtEtiqueta.setText(s2);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {

            //selectedImage = data.getData();

            bmp = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byteArray = stream.toByteArray();
            bmp.recycle();

            ImageView imageView = findViewById(R.id.Imageprev);
            imageView.setImageBitmap(bmp);
        }
        if (requestCode==1 && resultCode==RESULT_OK ) {
            txtEtiqueta.setText("");
            etiquetas = data.getExtras().getString("resultado");

            String [] strPartes=etiquetas.split("#");

            for(int i=1;i<strPartes.length;i++){
                txtEtiqueta.append("#"+strPartes[i]+"\n");
            }
        }
    }


}
