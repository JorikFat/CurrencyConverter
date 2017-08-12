package ru.jorik.currencyconverter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 111 on 12.08.2017.
 */

public class ValuteDateBase extends SQLiteOpenHelper {

    static String TABLE_NAME = "Valute";
    static int VERSION = 1;
    //поля
    String ID = "id";
    String NAME = "name";
    String NUM_CODE = "numCode";
    String CHAR_CODE = "charCode";
    String NOMINAL = "nominal";
    String VALUE = "value";

    String COUNT_QUERY = "SELECT * FROM " + TABLE_NAME;
    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + ID + " TEXT PRIMARY KEY,"
            + NAME + " TEXT,"
            + NUM_CODE + " INTEGER,"
            + CHAR_CODE + " TEXT,"
            + NOMINAL + " INTEGER,"
            + VALUE + " INTEGER"
            + ")";

    public ValuteDateBase(Context context) {
        super(context, TABLE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createItem(Valute valute){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, valute.id);
        contentValues.put(NUM_CODE, valute.numCode);
        contentValues.put(CHAR_CODE, valute.charCode);
        contentValues.put(NOMINAL, valute.nominal);
        contentValues.put(NAME, valute.name);
        contentValues.put(VALUE, valute.value);
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    public Valute readItem(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                ID+" = ?",
                new String[]{String.valueOf(id)},
                null,null,null,null
        );
        if (cursor.moveToFirst()){
            Valute rValute = new Valute();
            rValute.id = id;
            rValute.numCode = cursor.getInt(cursor.getColumnIndex(NUM_CODE));
            rValute.charCode = cursor.getString(cursor.getColumnIndex(CHAR_CODE));
            rValute.nominal = cursor.getInt(cursor.getColumnIndex(NOMINAL));
            rValute.name = cursor.getString(cursor.getColumnIndex(NAME));
            rValute.value = cursor.getInt(cursor.getColumnIndex(VALUE));
            return rValute;
        }
        cursor.close();
        return null;
    }

    public void updateItem(Valute valute){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, valute.id);
        contentValues.put(NUM_CODE, valute.numCode);
        contentValues.put(CHAR_CODE, valute.charCode);
        contentValues.put(NOMINAL, valute.nominal);
        contentValues.put(NAME, valute.name);
        contentValues.put(VALUE, valute.value);
        db.update(TABLE_NAME, contentValues, ID + " = ?", new String[]{String.valueOf(valute.id)});
        db.close();
    }

    public int getCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(COUNT_QUERY, null);
        return cursor.getCount();
    }

    //// TODO: 12.08.2017 протестировать этот метод
    private ContentValues valuteToValues(Valute valute) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, valute.id);
        contentValues.put(NUM_CODE, valute.numCode);
        contentValues.put(CHAR_CODE, valute.charCode);
        contentValues.put(NOMINAL, valute.nominal);
        contentValues.put(NAME, valute.name);
        contentValues.put(VALUE, valute.value);
        return contentValues;
    }


    public List<Valute> getAllValutes(){
        SQLiteDatabase db = getWritableDatabase();
        List<Valute> rList = new ArrayList<Valute>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                rList.add(cursorToValuteConvert(c));
            } while (c.moveToNext());
        }

        return rList;
    }

    private Valute cursorToValuteConvert(Cursor cursor){
        Valute rValute = new Valute();
        rValute.id = cursor.getString(cursor.getColumnIndex(ID));
        rValute.numCode = cursor.getInt(cursor.getColumnIndex(NUM_CODE));
        rValute.charCode = cursor.getString(cursor.getColumnIndex(CHAR_CODE));
        rValute.nominal = cursor.getInt(cursor.getColumnIndex(NOMINAL));
        rValute.name = cursor.getString(cursor.getColumnIndex(NAME));
        rValute.value = cursor.getInt(cursor.getColumnIndex(VALUE));
        return rValute;
    }
}
