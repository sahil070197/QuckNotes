package sahil.quickNotes;

import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class DeleteConfirmFragment extends DialogFragment {

    Context context;
    int noteId;
    public DeleteConfirmFragment()
    {}

    public void newInstance(Context c, int noteId)
    {
        this.context=c;
        this.noteId=noteId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_delete_confirm,container,false);
        Button yes=(Button) v.findViewById(R.id.yes);
        Button no=(Button) v.findViewById(R.id.no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FinalActivity)getActivity()).clickedYes();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FinalActivity)getActivity()).clickedNo();
            }
        });
        return  v;
    }
}
