package sahil.quickNotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

/**
 * Created by SAHIL SINGLA on 14-06-2017.
 */

public class NotesSqliteManager extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="NOTES_DATABASE";
    public static final int DATABASE_VERSION=1;
    public static final String TABLE_NAME="NOTES";
    public static final String COLUMN_ID="_id";
    public static final String COLUMN_TITLE="title";
    public static final String COLUMN_DATA="data";
    public static final String COLUMN_DATE="datetime";

    public NotesSqliteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String statement="Create table "+TABLE_NAME+"("+COLUMN_ID+" Integer Primary Key, "+COLUMN_TITLE+" Text, "+COLUMN_DATA+" Text, "+COLUMN_DATE+" text)";
        sqLiteDatabase.execSQL(statement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String statement="Drop table "+TABLE_NAME+" if exists";
        sqLiteDatabase.execSQL(statement);
        onCreate(sqLiteDatabase);
    }

    public void addNote(Note note)
    {
        try {


            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = note.getValues();
            database.insert(TABLE_NAME, null, values);
            database.close();
        }
        catch (Exception e)
        {
            Log.d("Adding Error: ","error in adding :"+e);
        }
    }
    public Cursor getNotes()
    {
        ArrayList<Note> noteArrayList = new ArrayList<>();
        Cursor cursor=null;
        try {

            SQLiteDatabase database = this.getReadableDatabase();
            cursor = database.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_DATA, COLUMN_DATE}, null, null, null, null, COLUMN_ID+" DESC");
        }
        catch (Exception e)
        {
            Log.d("Get All Error: ","error in fetching: "+e);
        }
        return cursor;
    }
    public void deleteNote(int id)
    {
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            database.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
            database.close();
        }
        catch (Exception e)
        {
            Log.d("DeleteNOte Error: ","error in deletion :"+e);
        }
    }
    public void updateRecord(Note note)
    {
        try {
            if(note.getId()==null)
            {
                addNote(note);
                return;
            }
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = note.getValues();
            database.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(note.getId())});
        }
        catch (Exception e)
        {
            Log.d("Update Error: ","error in updation :"+e);
        }
    }


}
