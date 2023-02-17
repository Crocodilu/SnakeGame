package com.example.snakegame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String PLAYERS_TABLE = "PLAYERS_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_SCORE = "SCORE";
    public static final String COLUMN_USERNAME = "USERNAME";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "players.db", null, 1);
    }

    @Override // se executa prima data cand se deschide baza de date, trebuia sa includa crearea tabelului
    public void onCreate(SQLiteDatabase db) {
        // crearea tabelului
        String createTableStatement= "CREATE TABLE " + PLAYERS_TABLE + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_SCORE + " INT, "
                + COLUMN_USERNAME + " TEXT)";

        db.execSQL(createTableStatement);
    }

    @Override // se executa cand versiunea bazei de date se modifică
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(PlayerModel playerModel){
        SQLiteDatabase db = this.getWritableDatabase();     // baza de date in care putem scrie
        ContentValues cv = new ContentValues();             // pentru a pune informatii

        // put(cheie-valoare) pentru (destinatie-valoare)
        cv.put(COLUMN_SCORE,playerModel.getScore());
        cv.put(COLUMN_USERNAME, playerModel.getUsername());

        long insert = db.insert(PLAYERS_TABLE, null, cv);
        if(insert == -1){       // daca operatia de adaugare nu a reusit
            return false;       // se returneaza o valoare falsa
        }
        else{                   // altfel
            return true;        // se returneaza o valoare adevarata
        }
    }//addOne

    public List<PlayerModel> getEveryone(){
        List<PlayerModel> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " +PLAYERS_TABLE;       // preluam datele din baza de date
        SQLiteDatabase db = this.getReadableDatabase();             // baza de date din care putem citi
        Cursor cursor = db.rawQuery(queryString, null);  // cursorul care va merge de la un element la altul

        if(cursor.moveToFirst()){ // punem cursorul pe primul element, iar daca acesta nu e null putem continua
            // se trece prin elemente (setul de rezultate) cu ajutorul cursorului si se creaza noi obiecte student
            // pe care le inseram în lista

            do{
                int playerID= cursor.getInt(0);
                int playerSCORE= cursor.getInt(1);
                String playerUSERNAME= cursor.getString(2);

                PlayerModel playerModel=new PlayerModel(playerID, playerSCORE, playerUSERNAME);
                returnList.add(playerModel);   // plasez studentul in lista
            } while (cursor.moveToNext());      // pana cand nu mai avem elemente in lista (ajungem la un element null)
        }
        else{
            // cursorul nu a selectat nicio intrare în baza de date, prin urmare nu adaugam nimic la lista
        }

        // se inchide intotdeauna cursorul și baza de date
        cursor.close();
        db.close();
        return returnList;  //returneaza lista creata in bucla do-while
    }//getEveryone

    public List<PlayerModel> getBest(){
        List<PlayerModel> returnList = new ArrayList<>();
        int best = -1;
        int playerIDbest = 0;
        int playerSCOREbest = 0;
        String  playerUSERNAMEbest = null;

        String queryString = "SELECT * FROM " +PLAYERS_TABLE;       // preluam datele din baza de date
        SQLiteDatabase db = this.getReadableDatabase();             // baza de date din care putem citi
        Cursor cursor = db.rawQuery(queryString, null);  // cursorul care va merge de la un element la altul

        if(cursor.moveToFirst()){ // punem cursorul pe primul element, iar daca acesta nu e null putem continua
            // se trece prin elemente (setul de rezultate) cu ajutorul cursorului si se creaza noi obiecte student
            // pe care le inseram în lista

            do{
                int playerID= cursor.getInt(0);
                int playerSCORE= cursor.getInt(1);
                String playerUSERNAME= cursor.getString(2);

                if(playerSCORE > best){
                    playerIDbest = playerID;
                    playerSCOREbest = playerSCORE;
                    playerUSERNAMEbest = playerUSERNAME;
                    best = playerSCORE;
                }
            } while (cursor.moveToNext());      // pana cand nu mai avem elemente in lista (ajungem la un element null)
        }
        else{
            // cursorul nu a selectat nicio intrare în baza de date, prin urmare nu adaugam nimic la lista
        }

        PlayerModel playerModel=new PlayerModel(playerIDbest, playerSCOREbest, playerUSERNAMEbest);
        returnList.add(playerModel);   // plasez jucătorul in lista

        // se inchide intotdeauna cursorul și baza de date
        cursor.close();
        db.close();
        return returnList;  //returneaza lista creata in bucla do-while
    }//getBest
}
