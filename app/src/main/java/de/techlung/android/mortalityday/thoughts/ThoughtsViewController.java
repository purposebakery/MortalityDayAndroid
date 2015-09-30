package de.techlung.android.mortalityday.thoughts;


import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.animators.FadeInUpAnimator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.techlung.android.mortalityday.MainActivity;
import de.techlung.android.mortalityday.R;
import de.techlung.android.mortalityday.baasbox.BaasBoxMortalityDay;
import de.techlung.android.mortalityday.greendao.extended.DaoFactory;
import de.techlung.android.mortalityday.greendao.generated.Thought;
import de.techlung.android.mortalityday.settings.Preferences;
import de.techlung.android.mortalityday.util.MortalityDayUtil;

public class ThoughtsViewController {
    public static final String TAG = ThoughtsViewController.class.getName();

    Activity activity;
    View view;

    @Bind(R.id.thoughts_recycler_view) UltimateRecyclerView recyclerView;
    @Bind(R.id.thoughts_add_button) View addButton;

    private UltimateViewAdapter recyclerAdapter;
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
        thoughts.addAll(DaoFactory.getInstanceLocal().getExtendedThoughtDao().getAllThoughts());
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

        recyclerView.setItemAnimator(new FadeInUpAnimator());
        recyclerView.getItemAnimator().setAddDuration(300);
        recyclerView.getItemAnimator().setRemoveDuration(300);
    }

    private void initAddButton() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.TakingOff).duration(200).playOn(addButton);
                YoYo.with(Techniques.Landing).duration(200).delay(200).playOn(addButton);

                showEditThoughtDialog(null);
            }
        });
    }

    private void showEditThoughtDialog(@Nullable Thought thought) {
        (new EditThoughtDialog()).showEditThoughtDialog(activity, thought);
    }

    public void reloadAdapter() {
        initData();
        recyclerAdapter.notifyDataSetChanged();
    }

    public class MyAdapter extends UltimateViewAdapter<MyAdapter.ViewHolder> {
        private List<Thought> mDataset;
        private ViewHolder vh;
        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            @Bind(R.id.thoughts_text) TextView text;
            @Bind(R.id.thoughts_category) TextView category;
            @Bind(R.id.thoughts_author) TextView author;
            @Bind(R.id.thoughts_date) TextView date;

            @Bind(R.id.thoughts_item_cloud_icon) ImageView icon;
            @Bind(R.id.thoughts_item_cloud_icon_container) View iconContainer;
            @Bind(R.id.thoughts_item_root) View root;

            public ViewHolder(View v) {
                super(v);
                ButterKnife.bind(this, v);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(List<Thought> myDataset) {
            mDataset = myDataset;
        }


        @Override
        public ViewHolder getViewHolder(View view) {
            return null;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
            // create a new view
            View v = LayoutInflater.from(activity).inflate(R.layout.thoughts_item, null, false);
            // set the view's size, margins, paddings and layout parameters
            vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            final Thought thought = mDataset.get(position);

            holder.text.setText(thought.getText());
            holder.category.setText(activity.getResources().getStringArray(R.array.thoughts_categories)[thought.getCategory()]);
            holder.author.setVisibility(View.GONE);
            holder.date.setText(MortalityDayUtil.getDateFormatted(thought.getDate().getTime()));


            if (thought.getShared()) {
                holder.icon.setImageResource(R.drawable.ic_action_cloud_done);
            } else {
                if (Preferences.isAutomaticSharing()) {
                    holder.icon.setImageResource(R.drawable.ic_action_cached);
                } else {
                    holder.icon.setImageResource(R.drawable.ic_action_cloud_upload);
                }
            }

            holder.iconContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!thought.getShared() && !Preferences.isAutomaticSharing()) {
                        BaasBoxMortalityDay.sendThought(thought);
                    }
                }
            });

            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEditThoughtDialog(thought);
                }
            });

        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
            return null;
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        @Override
        public int getAdapterItemCount() {
            return 0;
        }

        @Override
        public long generateHeaderId(int i) {
            return 0;
        }
    }
}
