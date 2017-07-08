package sahil.quickNotes;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import static android.view.View.GONE;

public class FinalActivity extends AppCompatActivity {
    FloatingActionButton finalFab, deleteFab;
    Toolbar bar;
    EditText editTitle,editBody;
    Integer id;
    DeleteConfirmFragment fragment;
    Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        init();
        setBarActions();
        backgroundChangeListeners();
        checkNewNote();
        initFieldsIfEditing();
        setActionListeners();
    }

    private void setActionListeners() {
        finalFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });

        deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                removePreviousFragment(transaction);
                displayNewFragment(transaction);
            }
        });
    }

    private void displayNewFragment(FragmentTransaction transaction) {
        fragment = new DeleteConfirmFragment();
        fragment.newInstance(FinalActivity.this, id);
        fragment.show(transaction, "deleteConfirmationDialog");
    }

    private void removePreviousFragment(FragmentTransaction transaction) {
        Fragment prev = getFragmentManager().findFragmentByTag("deleteConfirmationDialog");
        if (prev != null) {
            transaction.remove(prev);
        }
        transaction.addToBackStack(null);
    }

    private void initFieldsIfEditing() {
        String title=b.getString(NotesSqliteManager.COLUMN_TITLE,null);
        String body=b.getString(NotesSqliteManager.COLUMN_DATA,null);
        if(id!=null)
        {
            editTitle.setText(title);
            editBody.setText(body);
        }
    }

    private void checkNewNote() {
        b=getIntent().getExtras();
        id=b.getInt(NotesSqliteManager.COLUMN_ID, -1);
        if(id==-1)
        {
            deleteFab.setVisibility(GONE);
            id=null;
        }
    }

    private void backgroundChangeListeners() {
        editTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {


            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View view, boolean b) {

                editTitle.setBackground(getResources().getDrawable(R.drawable.shape_rectangular_border));
                editBody.setBackground(getResources().getDrawable(R.drawable.shape_rectangle_colorless_border));
                editTitle.setElevation(2.0f);
                editBody.setElevation(0f);
            }
        });

        editBody.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View view, boolean b) {
                editBody.setBackground(getResources().getDrawable(R.drawable.shape_rectangular_border));
                editTitle.setBackground(getResources().getDrawable(R.drawable.shape_rectangle_colorless_border));
                editBody.setElevation(2.0f);
                editTitle.setElevation(0f);
            }
        });

    }

    private void init() {
        bar=(Toolbar) findViewById(R.id.finalToolbar);
        finalFab=(FloatingActionButton) findViewById(R.id.finalFab);
        deleteFab=(FloatingActionButton) findViewById(R.id.deleteFab);
        editTitle=(EditText) findViewById(R.id.editTitle);
        editBody=(EditText) findViewById(R.id.editBody);
    }

    private void setBarActions() {
        bar.setTitle("Edit Note");
        bar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close));
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void saveNote() {
        ProgressDialog pd=new ProgressDialog(FinalActivity.this);
        pd.setIndeterminate(true);
        pd.setMessage("Updating...");
        pd.setTitle("Please Wait...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        String title=editTitle.getText().toString();
        String body=editBody.getText().toString();
        if(title==null || body==null || title.length()==0 || body.length()==0)
        {
            pd.dismiss();
            Toast.makeText(FinalActivity.this,"Invalid Note!",Toast.LENGTH_SHORT).show();
            return;
        }
        long curStamp= System.currentTimeMillis();
        Date date=new Date(curStamp);
        String day=String.valueOf(date.getDate());
        String month=String.valueOf(date.getMonth()+1);
        String year=String.valueOf(date.getYear()+1900);
        String hour=String.valueOf(date.getHours());
        String min=String.valueOf(date.getMinutes());
        StringBuffer buffer=new StringBuffer(day+"/"+month+"/"+year+" "+hour+":"+min);
        Note note;
        if(id==null)
        note=new Note(title,body,buffer.toString());
        else
        note=new Note(id,title,body,buffer.toString());
        NotesSqliteManager manager=new NotesSqliteManager(FinalActivity.this);
        manager.updateRecord(note);
        pd.dismiss();
        Toast.makeText(getApplicationContext(),"Updated!",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void clickedYes() {
        ProgressDialog pd=new ProgressDialog(FinalActivity.this);
        pd.setIndeterminate(true);
        pd.setMessage("Updating...");

        new NotesSqliteManager(FinalActivity.this).deleteNote(id);

        pd.dismiss();
        Toast.makeText(getApplicationContext(),"Deleted!",Toast.LENGTH_SHORT).show();
        fragment.dismiss();
        finish();
    }

    public void clickedNo() {
        fragment.dismiss();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
