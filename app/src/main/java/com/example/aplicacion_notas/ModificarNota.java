package com.example.aplicacion_notas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ModificarNota extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SQLiteDatabase db;
    DBquerys dbquerys;
    int id_nota;
    int id_spinner;

    String titulo;
    String contenido;
    String fecha;
    String categoria;

    Spinner sp_categorias;
    EditText titulomod;
    TextView fechamod;
    EditText notamod;
    Calendar calendario;
    DatePickerDialog dpd;
    String [] arraycategorias;
    Intent ivolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificarnota);

        titulomod = (EditText) findViewById(R.id.et_titulo);
        fechamod = (TextView) findViewById(R.id.tv_fecha);
        notamod = (EditText) findViewById(R.id.et_contenido);

        sp_categorias = (Spinner) findViewById(R.id.spinner_categoria);

        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(this,R.array.categoriasejemplo, android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_categorias.setAdapter(adaptador);
        rellenarcanpos();
        sp_categorias.setOnItemSelectedListener(this);

        //Abrimos la base de datos 'DBNotas' en modo escritura
        dbquerys = new DBquerys(this);
        dbquerys.abrirbd();
        ivolver = new Intent();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position == 0){
            sp_categorias.setSelection(id_spinner);
            Toast.makeText(this,"Tu nota debe pertenecer a una categoría",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public void rellenarcanpos(){

        // Valores que recogemos de la activity anterior
        id_nota = getIntent().getExtras().getInt("idnota");
        titulo = getIntent().getExtras().getString("titulonota");
        contenido = getIntent().getExtras().getString("contenidonota");
        fecha = getIntent().getExtras().getString("fechanota");
        categoria = getIntent().getExtras().getString("categorianota");

        id_spinner = getIntent().getExtras().getInt("spinnercat");

        // Valores que le asignamos
        /* si no selecciona una categoría en concreto hago un bucle para
            buscar que categoría es la que tiene esa nota */
        arraycategorias = getResources().getStringArray(R.array.categoriasejemplo);
        if (id_spinner == 0){

            for(int i = 0; i <= arraycategorias.length ; i++){
                if(arraycategorias[i].equals(categoria)){
                    id_spinner = i;
                    sp_categorias.setSelection(id_spinner);
                    break;
                }
            }

        }else{
            sp_categorias.setSelection(id_spinner);
        }
        titulomod.setText(titulo);
        fechamod.setText(fecha);
        notamod.setText(contenido);
    }

    public void onclickModificar(View view){

        String t = titulomod.getText().toString();
        String f = fechamod.getText().toString();
        String c = notamod.getText().toString();
        String cat = sp_categorias.getSelectedItem().toString();

        int result = dbquerys.actualizarnota(id_nota,t,c,f,cat);
        //int result = db.update("Notas",actualizar, "_id=" + id_nota,null);

        ivolver.putExtra("resultado",1);

        if(result !=0){
            setResult(RESULT_OK,ivolver);
            finish();
        }else{
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    public void onClickCancel(View view){
        onBackPressed();
    }

    public void onclickBorrar(View view){

        //int result = db.delete("Notas","_id=" + id_nota,null);
        int result = dbquerys.borrarnota(id_nota);
        ivolver.putExtra("resultado",2);

        if(result !=0){
            setResult(RESULT_OK,ivolver);
            finish();
        }else{
            finish();
        }
    }

    // Evento que se produce cuando el usuario quiere seleccionar la fecha
    public void onclickmodificarFecha(View view){

        calendario = Calendar.getInstance();
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH);
        int anyo = calendario.get(Calendar.YEAR);

        dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fechamod.setText(dayOfMonth + "-" + (month+1) + "-" + year);
            }
        }, dia,mes,anyo);

        dpd.show();
        dpd.updateDate(anyo,mes,dia);

    }
}
