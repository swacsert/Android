package com.example.aplicacion_notas;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SQLiteDatabase db;
    DBquerys dbquerys;
    private ListView lv_notas;
    int posicionspinner;
    String textoSpinner;
    public static String MOSTRARTODO = "Todas las categorías";

    Context context;
    MiAdaptadorNotas miadaptador;
    Spinner spinner;

    private ArrayList<Notas> notas_arraylist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        pedirpermisos();


        spinner = (Spinner) findViewById(R.id.sp_categorias);
        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(this,R.array.categoriasejemplo, android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adaptador);
        spinner.setOnItemSelectedListener(this);

        //Abrimos la base de datos 'DBNotas' en modo escritura
        //NotasSqliteHelper notasdb = new NotasSqliteHelper(this, "DBNotas", null, 1);
        dbquerys = new DBquerys(this);
        dbquerys.abrirbd();

        /* Se podría añadir un TextView si el LV esta vacío informando al usuario que no existen notas con esa categoría, pero resulta obvio.
        if(notas_arraylist.isEmpty()){
            TextView textovacio = (TextView) findViewById(R.id_nota.tv_vacio);
            lv_notas.setEmptyView(textovacio);
        }*/

        miadaptador = new MiAdaptadorNotas(this,R.layout.list_item_notas, notas_arraylist);
        lv_notas = findViewById(R.id.listview_notas);
        lv_notas.setAdapter(miadaptador);
        lv_notas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterview, View view, int position, long id) {

                // Podemos conseguir el id_nota sin necesidad de enseñarlo.
                int idnota = ((Notas) adapterview.getAdapter().getItem(position)).getId();
                String titnota = ((Notas) adapterview.getAdapter().getItem(position)).getTitulo();
                String contnota = ((Notas) adapterview.getAdapter().getItem(position)).getContenido();
                String fecha = ((Notas) adapterview.getAdapter().getItem(position)).getFechacreacion();
                String categoria = ((Notas) adapterview.getAdapter().getItem(position)).getCategoria();

                Intent i2 = new Intent(getApplicationContext(),ModificarNota.class);

                i2.putExtra("idnota" , idnota);
                i2.putExtra("titulonota",titnota);
                i2.putExtra("contenidonota", contnota);
                i2.putExtra("fechanota",fecha);
                i2.putExtra("categorianota",categoria);
                // Si selecciona una categoria recojo la posicion
                i2.putExtra("spinnercat",posicionspinner);
                //i2.putExtra("spinnertext",textoSpinner);

                startActivityForResult(i2,11);

            }
        });

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            textoSpinner = parent.getItemAtPosition(position).toString();
            posicionspinner = position;
            cargardatoscategoria();
    }

    //Método para cargar las notas pertenecientes a una categoría en concreto.
    private void cargardatoscategoria() {

        if (textoSpinner.equals(MOSTRARTODO)){
            cargardatos();
        }else{

            Cursor c = dbquerys.cargarnotas_filtro(textoSpinner);
            if(!notas_arraylist.isEmpty()){ notas_arraylist.clear();}
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            int id_2 = c.getInt(0);
                            String titulo = c.getString(1);
                            String contenido = c.getString(2);
                            String fechacreacion = c.getString(3);
                            String categoria = c.getString(4);

                            Notas n = new Notas(id_2, titulo, contenido, fechacreacion, categoria);
                            notas_arraylist.add(n);

                        }while(c.moveToNext());
                    }
                    // Metodo que notifica el nuevo arraylist que acabo de añadir (recomendable usar solo con add, insert, remove ,clear)
                    // El adaptador tiene como referencia una lista
                    miadaptador.notifyDataSetChanged();
                    c.close();
                    //runOnUiThread();

                } else {
                    Toast.makeText(this, "No existen notas con esa categoría", Toast.LENGTH_LONG).show();}
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 10){
            if(resultCode == RESULT_OK){
                Toast.makeText(this,"Se ha añadido una nota correctamente", Toast.LENGTH_LONG).show();
                cargardatoscategoria();
            }/* Podríamos mostrar también si no se ha realizado nada, pero parece innecesario. else{
                Toast.makeText(this,"No se ha añadido ninguna nota",Toast.LENGTH_SHORT).show();
            }*/
        }

        if(requestCode == 11) {
            if (resultCode == RESULT_OK) {
                int resultado = data.getIntExtra("resultado",0);

                // Si el resultado que nos devuelve es 1, se ha modificado si es 2 borrado, se usan 2 ifs por seguridad ya que damos un defaultvalue
                if(resultado == 1){
                    Toast.makeText(this,"Se ha modificado la nota correctamente", Toast.LENGTH_LONG).show();
                    cargardatoscategoria();
                }
                if(resultado == 2){
                    Toast.makeText(this,"Se ha borrado la nota correctamente",Toast.LENGTH_LONG).show();
                    cargardatoscategoria();
                }
            }
        }

    }
    //Método que nos lleva a la Activity de insertar notas.
    public void onClickAgregar(View view){

        if (textoSpinner.equals(MOSTRARTODO)){
            Toast.makeText(this, "Debes seleccionar una categoría", Toast.LENGTH_SHORT).show();
        }else{
            Intent i = new Intent(this, ActivityNotas.class);
            i.putExtra("textospinner" , textoSpinner);
            startActivityForResult(i,10);
        }
    }

    //Método para cargar todas las notas existentes.
    public void cargardatos(){

        Cursor c = dbquerys.cargarnotas();

        if(!notas_arraylist.isEmpty()){ notas_arraylist.clear();}
        if (c!=null) {
            if (c.moveToFirst()) {
                do {
                    int id = c.getInt(0);
                    String titulo = c.getString(1);
                    String contenido = c.getString(2);
                    String fechacreacion = c.getString(3);
                    String categoria = c.getString(4);

                    Notas n = new Notas(id, titulo, contenido, fechacreacion, categoria);
                    notas_arraylist.add(n);


                    }while(c.moveToNext());
            }
                miadaptador.notifyDataSetChanged();
                c.close();
        }else {Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show();}
    }

    public void pedirpermisos(){
        int permiso_leer = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permiso_escribir = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        final int CODIGO_PERMISO = 112;


        // Si alguno de los dos permisos no esta permitido, vuelve a pedirlos
        if(permiso_escribir!= PackageManager.PERMISSION_GRANTED || permiso_leer!= PackageManager.PERMISSION_GRANTED){
            // Si estamos en la version 23 o superior , requerimos permisos
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},CODIGO_PERMISO);
            }
        }
    }





}
