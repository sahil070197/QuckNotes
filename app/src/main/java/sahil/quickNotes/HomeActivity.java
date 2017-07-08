package sahil.quickNotes;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import android.os.Handler;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {
    ListView notesList;
    Cursor cursor;
    TextView noNotes;
    NotesListAdapter adapter;
    FloatingActionButton fab;
    Toolbar toolbar;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setAddNoteListener();
        populateList();

        notesList.setOnItemClickListener(this);

    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Quick Notes");
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        notesList=(ListView) findViewById(R.id.notesList);
        noNotes=(TextView) findViewById(R.id.noNotesText);

    }

    private void populateList() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                NotesSqliteManager manager=new NotesSqliteManager(HomeActivity.this);
                cursor=manager.getNotes();
                if(cursor. getCount()!=0)
                {
                    noNotes.setVisibility(View.GONE);
                }
                if(cursor!=null)
                {
                    adapter=new NotesListAdapter(HomeActivity.this,cursor,0);
                    notesList.setAdapter(adapter);
                }
                else
                {
                    Log.d("Error : ","In setting cursor adapter");
                }
            }
        });
    }

    private void setAddNoteListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(HomeActivity.this,FinalActivity.class);
                i.putExtra(NotesSqliteManager.COLUMN_ID,-1);
                startActivityForResult(i,234);
            }
        });
    }

    private void launchEditor(int id, String title, String body, String date) {
        Intent intent=new Intent(HomeActivity.this,FinalActivity.class);
        intent.putExtra(NotesSqliteManager.COLUMN_ID,id);
        intent.putExtra(NotesSqliteManager.COLUMN_TITLE,title);
        intent.putExtra(NotesSqliteManager.COLUMN_DATA,body);
        intent.putExtra(NotesSqliteManager.COLUMN_DATE,date);
        startActivityForResult(intent,200);
    }

    private void updateList() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                NotesSqliteManager manager=new NotesSqliteManager(HomeActivity.this);
                cursor=manager.getNotes();
                if(cursor.getCount()==0)
                {
                    noNotes.setVisibility(View.VISIBLE);
                }
                else
                {
                    noNotes.setVisibility(View.GONE);
                }
                if(cursor!=null)
                {
                    adapter=new NotesListAdapter(HomeActivity.this,cursor,0);
                    notesList.setAdapter(adapter);
                }
                else
                {
                    Log.d("Error : ","In setting cursor adapter");
                }
            }
        });

    }

    private void actionLogout() {
        PreferenceHandler handler=new PreferenceHandler(this);
        handler.setPreference(PreferenceHandler.loginStatus,"false");
        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
        finish();
    }


    boolean doublePressed=false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if(doublePressed)
        {
            //Exit
            finish();
            System.exit(0);
        }
        Toast.makeText(this,"Press again to exit",Toast.LENGTH_SHORT).show();
        doublePressed=true;
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                doublePressed=false;
            }
        }, 2000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null) {
            adapter.notifyDataSetChanged();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (id == R.id.logout) {
            actionLogout();
        }

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(cursor!=null) {
            cursor.moveToPosition(i);
            int id = cursor.getInt(cursor.getColumnIndex(NotesSqliteManager.COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndex(NotesSqliteManager.COLUMN_TITLE));
            String body = cursor.getString(cursor.getColumnIndex(NotesSqliteManager.COLUMN_DATA));
            String date=cursor.getString(cursor.getColumnIndex(NotesSqliteManager.COLUMN_DATE));
            launchEditor(id,title,body,date);
         }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 234 || requestCode == 200)
        {
            updateList();
        }
    }

}
