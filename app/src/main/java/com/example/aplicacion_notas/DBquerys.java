package com.example.aplicacion_notas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBquerys {

    //Campos de la base de datos notas
    private static final String ID = "_id";
    private static final String TITULO = "titulo";
    private static final String CONTENIDO = "contenido";
    private static final String FECHA = "fechacreacion";
    private static final String CATEGORIA = "categoria";
    private static final String NOMBRE_TABLA = "Notas";


    Context contexto;
    NotasSqliteHelper notasdb;
    private SQLiteDatabase db;


    public DBquerys(Context context) {
        this.contexto = context;
    }
    // Método simple para la creacion de las notas de un contentvalues
    private ContentValues crearcontenedor(String titulo, String contenido , String fecha , String categoria){

        ContentValues valores = new ContentValues();
        valores.put(TITULO,titulo);
        valores.put(CONTENIDO,contenido);
        valores.put(FECHA,fecha);
        valores.put(CATEGORIA,categoria);
        return valores;

    }

    // Abrimos la bd en modo escritura
    public void abrirbd() {
        //Abrimos la base de datos 'DBNotas' en modo escritura
        notasdb = new NotasSqliteHelper(contexto);
        db = notasdb.getWritableDatabase();
    }
    // Cierra la bd
    public void cerrarbd(){
        notasdb.close();
    }
    //Método que nos permite crear una nota , devuelve
    public long crearnota(String titulo,String contenido, String fecha, String categoria){
        return db.insert(NOMBRE_TABLA,null,crearcontenedor(titulo,contenido,fecha,categoria));
    }

    public int actualizarnota(Integer id,String titulo, String contenido , String fecha , String categoria){
        return db.update(NOMBRE_TABLA,crearcontenedor(titulo,contenido,fecha,categoria),ID + "=" + id,null);
    }

    public int borrarnota(Integer id){
        return db.delete(NOMBRE_TABLA,ID + "=" + id,null);
    }

    public Cursor cargarnotas(){

        String s = ", ";
        String query = "SELECT " + ID  + s + TITULO + s + CONTENIDO + s + FECHA + s + CATEGORIA + " FROM " + NOMBRE_TABLA;
        return db.rawQuery(query,null);
    }

    public Cursor cargarnotas_filtro(String categoria){

        String s = ", ";
        String query_where = "SELECT " + ID  + s + TITULO + s + CONTENIDO + s + FECHA + s + CATEGORIA + " FROM " +
                NOMBRE_TABLA + " WHERE " + CATEGORIA + " = ?";
        Cursor c = db.rawQuery(query_where, new String[] {categoria.trim()});

        return c;
    }



}

