package info.androidhive.glide.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Erlan on 24.06.2017.
 */

public class DataBase extends SQLiteOpenHelper {
    public static final String DATA_BASE_NAME = "mydatabase.db";
    public static final String TABLE = "password";
    public static final String PASSWORD_COLUMN = "password";
    public static final String NAME_COLUMN = "name";
    public static final int DATABASE_VERSION = 1;
    public DataBase(Context context) {
        super(context, DATA_BASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+ TABLE + "("+ BaseColumns._ID + " integer primary key autoincrement,"+
                NAME_COLUMN + " text,"
                + PASSWORD_COLUMN + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void addPassword (String password){
        ContentValues values = new ContentValues();
        values.put(PASSWORD_COLUMN,password);
        getWritableDatabase().insert(TABLE, null, values);
    }
    public  void readData(){
        Cursor cursor = getWritableDatabase().query(TABLE,null,null,null,null,null,null);
        if (cursor != null && cursor.getCount()>0){
            while (cursor.moveToNext()) {


                String name = cursor.getString(cursor.getColumnIndex(NAME_COLUMN));
                String password = cursor.getString(cursor.getColumnIndex(PASSWORD_COLUMN));
                int id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));

                Log.e("TAG", "id = "+id +" name ="+name+" password = "+ password);
            }
        }if (cursor != null)cursor.close();
    }
    public Cursor getData(){
        return getWritableDatabase().query(TABLE,null,null,null,null,null,null);
    }
    public void deleteData(){
        getWritableDatabase().delete(TABLE,null,null);
    }
}
