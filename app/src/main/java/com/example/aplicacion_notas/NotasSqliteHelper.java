package com.example.aplicacion_notas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotasSqliteHelper extends SQLiteOpenHelper {

    private static final String BD_NAME = "DBNotas";
    private static final int BD_VERSION = 1;

    // Query de la creacion de la tabla notas
    private static final String SQLCREAR = "CREATE TABLE Notas (_id INTEGER primary key autoincrement, " +
            "titulo TEXT, contenido TEXT, fechacreacion TEXT, categoria TEXT NOT NULL)";

    // Constructor , hemos hecho algunas variables static por si se quiere renombar más adelante a la BD
    public NotasSqliteHelper(Context context)
    {
            super(context, BD_NAME , null, BD_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creacion de la tabla Notas
        db.execSQL(SQLCREAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva){
        // Se elemina la version anterior
        db.execSQL("DROP TABLE IF EXISTS Notas");
        // Se crea con la nueva version
        db.execSQL(SQLCREAR);
    }



}
