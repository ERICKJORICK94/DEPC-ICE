package com.smartapp.depc_ice.Activities.CrearClientes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Utils;

import java.sql.SQLException;

public class CrearClientesActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks {

    private View layout;
    private LayoutInflater layoutInflater;

    private EditText cedula;
    private EditText direccion;
    private EditText nombre;
    private EditText apellido;
    private EditText razon_social;
    private EditText telefono;
    private EditText celular;
    private EditText correo;
    private RadioButton nacionalidad;
    private Button registrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.crear_clientes_layout);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

        cedula = (EditText) layout.findViewById(R.id.cedula);
        direccion = (EditText) layout.findViewById(R.id.direccion);
        nombre = (EditText) layout.findViewById(R.id.nombre);
        apellido = (EditText) layout.findViewById(R.id.apellido);
        razon_social = (EditText) layout.findViewById(R.id.razon_social);
        telefono = (EditText) layout.findViewById(R.id.telefono);
        celular = (EditText) layout.findViewById(R.id.celular);
        correo = (EditText) layout.findViewById(R.id.correo);
        nacionalidad = layout.findViewById(R.id.nacional);
        registrar = (Button) layout.findViewById(R.id.registrar);

        nacionalidad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    cedula.setHint("Cédula/Ruc");
                else
                    cedula.setHint("Pasaporte");
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   if(cedula.getText().length()!=10){
                    if(nacionalidad.isChecked() == true){
                        //if(cedula.getText().length()!=13){
                        if(!ValidaCedula(cedula.getText().toString())){
                            //Toast.makeText(CrearClientesActivity.this, "Ingrese una cédula o RUC válido",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    else if(cedula.getText().length()==0){
                        Toast.makeText(CrearClientesActivity.this, "Ingrese un Pasaporte válido", Toast.LENGTH_LONG).show();
                        return;
                    }
               // }
                if(direccion.getText().length()<5){
                    Toast.makeText(CrearClientesActivity.this, "Ingrese una dirección válida", Toast.LENGTH_LONG).show();
                    return;
                }
                if(nombre.getText().length()<3){
                    Toast.makeText(CrearClientesActivity.this, "Ingrese un nombre válido", Toast.LENGTH_LONG).show();
                    return;
                }
                if(apellido.getText().length()<3){
                    Toast.makeText(CrearClientesActivity.this, "Ingrese un apellido válido", Toast.LENGTH_LONG).show();
                    return;
                }
                if(razon_social.getText().length()<3){
                    Toast.makeText(CrearClientesActivity.this, "Ingrese una razón social válida", Toast.LENGTH_LONG).show();
                    return;
                }
                if(telefono.getText().length() > 0 && telefono.getText().length()<9){
                    Toast.makeText(CrearClientesActivity.this, "Ingrese una teléfono válido", Toast.LENGTH_LONG).show();
                    return;
                }
                if(celular.getText().length() > 0 && celular.getText().length()<10){
                    Toast.makeText(CrearClientesActivity.this, "Ingrese un celular válido", Toast.LENGTH_LONG).show();
                    return;
                }


                if(telefono.getText().length() == 0 && celular.getText().length() == 0){
                    Toast.makeText(CrearClientesActivity.this, "Ingrese un teléfono o un celular ", Toast.LENGTH_LONG).show();
                    return;
                }
                else  {
                    String mensaje = "", campos = "";
                    if (telefono.getText().length() == 0)
                        campos += "\n - Teléfono ";
                    if (celular.getText().length() == 0)
                        campos += "\n - Celular";
                    if (correo.getText().length() == 0)
                        campos += "\n - Correo";
                    if (campos.length() != 0)
                        mensaje = "No ha ingresado los siguientes campos:" + campos;

                    mensaje = mensaje + "\n ¿Seguro que desea continuar?";
                    new AlertDialog.Builder(CrearClientesActivity.this)
                            .setMessage(mensaje)
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {



                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            })
                            .show();



                }
            }
        });

    }

    @Override
    public void doRetry() {

    }

    public boolean ValidaCedula(String cedula){
        //Declaración de variables a usar

        boolean valida=false;
        byte primeros2, tercerD, Dverificador, multiplicar, suma=0, aux;
        byte []digitos=new byte[9];

        //Primer try comprueba la longitud de cadena que no sea diferente de 10

        try {

            if(cedula.length()!=10) {
                if (cedula.length() != 13){
                    Toast.makeText(CrearClientesActivity.this, "Cantidad de dígitos de cédula o RUC incorrectos", Toast.LENGTH_LONG).show();
                    return  false;
                }
                else
                    cedula = cedula.substring(0,10);
            }
            //Segundo try comprueba que todos los dígitos sean numéricos
            try {

                //Transformación de cada carácter a un byte
                Dverificador= Byte.parseByte(""+cedula.charAt(9));
                primeros2= Byte.parseByte(cedula.substring(0, 2));
                tercerD= Byte.parseByte(""+cedula.charAt(2));
                for (byte i=0; i<9; i++) {
                    digitos[i]= Byte.parseByte(""+cedula.charAt(i));
                }
                //Verificar segundo dígito
                if(primeros2>=1 & primeros2<=24) {
                    if(tercerD<=6) {
                        //Módulo 10 multiplicar digitos impares por 2
                        for (byte i=0; i<9; i=(byte) (i+2)) {
                            multiplicar=(byte) (digitos[i]*2);
                            if(multiplicar>9) {
                                multiplicar=(byte) (multiplicar-9);
                            }
                            suma=(byte) (suma+multiplicar);
                        }
                        //Módulo 10 multiplicar digitos pares por 1
                        for (byte i=1; i<9; i=(byte) (i+2)) {
                            multiplicar=(byte) (digitos[i]*1);
                            suma=(byte) (suma+multiplicar);
                        }
                        //Obtener la decena superior de la suma
                        aux=suma;
                        while(aux%10!=0) {
                            aux=(byte) (aux+1);
                        }
                        suma=(byte) (aux-suma);
                        //Comprobar la suma con dígito verificador (Último Dígito)
                        if(suma!=Dverificador) {
                            Toast.makeText(CrearClientesActivity.this, "Ingrese una cédula o RUC válida", Toast.LENGTH_LONG).show();

                        }
                        else
                            valida=true;

                    }

                }

            }catch(NumberFormatException e) {

                System.out.println("La cédula o RUC debe contener solo dígitos numéricos.");

            }

        }catch(Exception e) {

            Toast.makeText(CrearClientesActivity.this, "Cédula o RUC incorrectO", Toast.LENGTH_LONG).show();

        }
        finally {
            return valida;
        }


    }
}
