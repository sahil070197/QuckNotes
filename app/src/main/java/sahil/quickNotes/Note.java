package sahil.quickNotes;

import android.content.ContentValues;

/**
 * Created by SAHIL SINGLA on 14-06-2017.
 */

public class Note {
    private String title;
    private String text;
    private String date;
    private Integer id;

    public Note(int id,String title, String text, String date) {
        this.title = title;
        this.text = text;
        this.date = date;
        this.id=id;
    }


    public Note(String title, String text, String date) {
        this.title = title;
        this.text = text;
        this.date = date;
        this.id=null;
    }

    public Integer getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }
    public ContentValues getValues()
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(NotesSqliteManager.COLUMN_TITLE,getTitle());
        contentValues.put(NotesSqliteManager.COLUMN_DATA,getText());
        contentValues.put(NotesSqliteManager.COLUMN_DATE,getDate());
        return contentValues;
    }
}
