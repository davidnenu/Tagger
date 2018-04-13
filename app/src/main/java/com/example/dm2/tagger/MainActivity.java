package com.example.dm2.tagger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String etiquetas;
    TextView txtEtiqueta;

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
            Conectar cn=new Conectar();
            //cn.conectar();

        }
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
}
