package de.techlung.android.mortalityday.thoughts;


import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.techlung.android.mortalityday.MainActivity;
import de.techlung.android.mortalityday.R;
import de.techlung.android.mortalityday.baasbox.BaasBoxMortalityDay;
import de.techlung.android.mortalityday.greendao.extended.DaoFactory;
import de.techlung.android.mortalityday.greendao.generated.Thought;

public class ThoughtsViewController {
    public static final String TAG = ThoughtsViewController.class.getName();

    Activity activity;
    View view;

    @Bind(R.id.thoughts_recycler_view) RecyclerView recyclerView;
    @Bind(R.id.thoughts_add_button) View addButton;

    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;

    private List<Thought> thoughts = new ArrayList<Thought>();

    public ThoughtsViewController(ViewGroup container) {
        activity = MainActivity.getInstance();
        view = createView(LayoutInflater.from(activity), container);
    }

    public View getView() {
        return view;
    }

    private View createView(LayoutInflater inflater, final ViewGroup container) {

        view = inflater.inflate(R.layout.thoughts_main, container, false);
        ButterKnife.bind(this, view);

        initData();
        initList();
        initAddButton();

        return view;
    }

    private void initData() {
        thoughts.clear();
        thoughts.addAll(DaoFactory.getInstance().getExtendedThoughtDao().getAllThoughts());
    }

    private void initList() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        recyclerLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(recyclerLayoutManager);

        // specify an recyclerAdapter (see also next example)
        initListAdapter();

    }

    private void initListAdapter() {
        recyclerAdapter = new MyAdapter(thoughts);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void initAddButton() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.TakingOff).duration(200).playOn(addButton);
                YoYo.with(Techniques.Landing).duration(200).delay(200).playOn(addButton);

                (new EditThoughtDialog()).showEditThoughtDialog(activity, new EditThoughtDialog.OnThoughtEditedListener() {
                    @Override
                    public void onThoughtEdited() {
                        reloadAdapter();
                    }
                });
            }
        });
    }

    private void reloadAdapter() {
        initData();
        recyclerAdapter.notifyDataSetChanged();

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<Thought> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            @Bind(R.id.thoughts_item_text) TextView text;
            @Bind(R.id.thoughts_item_cloud_icon) ImageView icon;
            @Bind(R.id.thoughts_item_cloud_icon_container) View iconContainer;

            public ViewHolder(View v) {
                super(v);
                ButterKnife.bind(this, v);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(List<Thought> myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.thoughts_item, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            final Thought thought = mDataset.get(position);

            holder.text.setText(thought.getText());

            if (thought.getShared()) {
                holder.icon.setImageResource(R.drawable.ic_action_cloud_done);
            } else {
                holder.icon.setImageResource(R.drawable.ic_action_cloud_upload);
            }

            holder.iconContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!thought.getShared()) {
                        BaasBoxMortalityDay.sendThought(thought, new BaasBoxMortalityDay.OnBassResultListener() {
                            @Override
                            public void onBaasResult(boolean success) {
                                reloadAdapter();
                            }
                        });
                    }
                }
            });

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
