package de.techlung.android.mortalityday.thoughts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
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

    public void showEditThoughtDialog(Context context, @Nullable final Thought thoughtParam) {
        this.thought = thoughtParam;

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
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.alert_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        if (thoughtParam != null) {
            builder.setNeutralButton(R.string.alert_delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DaoFactory.getInstanceLocal().getExtendedThoughtDao().deleteByKey(thoughtParam.getKey());
                    dialog.dismiss();
                }
            });
        }

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
        thought.setText(thoughtText.getText().toString().trim());
        thought.setCategory(category.getSelectedItemPosition());
        thought.setShared(false);

        DaoFactory.getInstanceLocal().getExtendedThoughtDao().insertOrReplace(thought);
        ThoughtManager.processLocalThoughtsChanged();
    }
}
