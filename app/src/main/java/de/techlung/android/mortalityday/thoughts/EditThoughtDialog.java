package de.techlung.android.mortalityday.thoughts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.techlung.android.mortalityday.R;
import de.techlung.android.mortalityday.greendao.extended.DaoFactory;
import de.techlung.android.mortalityday.greendao.generated.Thought;
import de.techlung.android.mortalityday.util.ThoughtUtil;

public class EditThoughtDialog {

    @Bind(R.id.thoughts_edit_category) Spinner category;
    @Bind(R.id.thoughts_edit_text) EditText thoughtText;

    Thought thought;
    OnThoughtEditedListener listener;

    public void showEditThoughtDialog(Context context, OnThoughtEditedListener listener) {
        showEditThoughtDialog(context, listener, null);
    }

    public void showEditThoughtDialog(Context context, @Nullable OnThoughtEditedListener listener, @Nullable Thought thoughtParam) {
        this.thought = thoughtParam;
        this.listener = listener;

        initThought();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.thoughts);

        View view = LayoutInflater.from(context).inflate(R.layout.thoughts_create, null, false);
        ButterKnife.bind(this, view);

        category.setSelection(thought.getCategory());
        thoughtText.setText(thought.getText());

        builder.setView(view);
        builder.setPositiveButton(R.string.alert_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveThought();
            }
        });

        builder.setNegativeButton(R.string.alert_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void initThought() {
        if (thought == null) {
            thought = new Thought();
            thought.setKey(ThoughtUtil.generateKey());
            thought.setCategory(Integer.valueOf(0));
            thought.setRating(0);
            thought.setShared(false);
            thought.setText("");
        }
    }

    private void saveThought(){
        thought.setText(thoughtText.getText().toString());
        thought.setCategory(category.getSelectedItemPosition());

        DaoFactory.getInstance().getExtendedThoughtDao().insertOrReplace(thought);
        if (listener != null) {
            listener.onThoughtEdited();
        }
    }

    public interface OnThoughtEditedListener {
        void onThoughtEdited();
    }
}
