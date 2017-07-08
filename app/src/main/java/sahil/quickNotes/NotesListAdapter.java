package sahil.quickNotes;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by SAHIL SINGLA on 15-06-2017.
 */

public class NotesListAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;
    Context context;
    public NotesListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context=context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View listItemView=cursorInflater.inflate(R.layout.notes_list_item,viewGroup,false);
        return listItemView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView title=(TextView) view.findViewById(R.id.title);
        TextView body=(TextView) view.findViewById(R.id.body);
        TextView date=(TextView) view.findViewById(R.id.date);

        populateCard(cursor, title,body,date);
        formatCard(title,body,date);
    }

    private void formatCard(TextView title, TextView body, TextView date) {
        Typeface bodyFont=Typeface.createFromAsset(context.getAssets(),"roboto (1)/Roboto-Thin.ttf");
        Typeface titleFont=Typeface.createFromAsset(context.getAssets(),"roboto (1)/Roboto-Light.ttf");
        Typeface dateFont=Typeface.createFromAsset(context.getAssets(),"roboto (1)/Roboto-Regular.ttf");
        body.setTypeface(bodyFont);
        title.setTypeface(titleFont);
        date.setTypeface(dateFont);
    }

    private void populateCard(Cursor cursor, TextView title, TextView body, TextView date) {
        title.setText(cursor.getString(cursor.getColumnIndex(NotesSqliteManager.COLUMN_TITLE)));
        body.setText(cursor.getString(cursor.getColumnIndex(NotesSqliteManager.COLUMN_DATA)));
        date.setText(cursor.getString(cursor.getColumnIndex(NotesSqliteManager.COLUMN_DATE)));
    }


}
