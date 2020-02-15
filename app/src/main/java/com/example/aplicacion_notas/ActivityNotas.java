package com.example.aplicacion_notas;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ActivityNotas extends AppCompatActivity {

    private SQLiteDatabase db;
    DBquerys dbquerys;

    EditText textoTitulo;
    EditText textoNotas;
    TextView textoFecha;
    TextView textoCategoria;

    String datoSpinner;

    Button btnfecha;
    Calendar calendario;
    DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        textoTitulo = (EditText) findViewById(R.id.txt_titulo);
        textoNotas = (EditText) findViewById(R.id.txt_nota);
        textoFecha = (TextView) findViewById(R.id.txt_fecha);
        textoCategoria = (TextView) findViewById(R.id.tv_categoria);

        //Abrimos la base de datos 'DBNotas' en modo escritura
        dbquerys = new DBquerys(this);
        dbquerys.abrirbd();

        datoSpinner = getIntent().getExtras().getString("textospinner");
        textoCategoria.setText(datoSpinner);
        btnfecha = (Button) findViewById(R.id.btn_elegirfecha);
    }

    public void onClickadd(View view){

        //Recuperamos los valores de los campos de texto
        String titulo = textoTitulo.getText().toString();
        String contenido = textoNotas.getText().toString();
        String fechacreacion = textoFecha.getText().toString();
        String categoria = textoCategoria.getText().toString();

        //long res = db.insert("Notas", null, nuevoRegistro);
        long res = dbquerys.crearnota(titulo,contenido,fechacreacion,categoria);
        if (res != -1) {
            setResult(RESULT_OK);
            finish();
        }else{
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    //Evento que se produce cuando el usuario quiere seleccionar la fecha
    public void onClickfecha(View view){
        //Instanciamos nuestro calendario e inicializamos nuestras variables.
        calendario = Calendar.getInstance();
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH);
        int anyo = calendario.get(Calendar.YEAR);

        //Sobreescribimos el evento para que recoja la fecha seleccionada en nuestro tv;
        dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                textoFecha.setText(dayOfMonth + "-" + (month+1) + "-" + year);
            }
        }, dia,mes,anyo);

        dpd.show();
        dpd.updateDate(anyo,mes,dia);

    }

    public void onClickCancel(View view){
        onBackPressed();
    }
}
