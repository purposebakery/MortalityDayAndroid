package de.techlung.android.mortalityday.gathering;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.baasbox.android.BaasBox;
import com.baasbox.android.BaasDocument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.techlung.android.mortalityday.MainActivity;
import de.techlung.android.mortalityday.R;
import de.techlung.android.mortalityday.baasbox.BaasBoxMortalityDay;
import de.techlung.android.mortalityday.greendao.extended.DaoFactory;
import de.techlung.android.mortalityday.greendao.generated.Thought;
import de.techlung.android.mortalityday.greendao.generated.ThoughtDao;
import de.techlung.android.mortalityday.settings.Preferences;
import de.techlung.android.mortalityday.thoughts.ThoughtManager;
import de.techlung.android.mortalityday.util.MortalityDayUtil;

public class GatheringViewController implements PopupMenu.OnMenuItemClickListener{
    public static final String TAG = GatheringViewController.class.getName();

    private enum Sort {DATE_DESC, RATING_DESC}

    Activity activity;
    View view;

    @Bind(R.id.thoughts_recycler_view) RecyclerView recyclerView;

    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;

    private List<BaasDocument> thoughts = new ArrayList<BaasDocument>();
    private Sort currentSort = Sort.DATE_DESC;

    public GatheringViewController(ViewGroup container) {
        activity = MainActivity.getInstance();
        view = createView(LayoutInflater.from(activity), container);
    }

    public View getView() {
        return view;
    }

    private View createView(LayoutInflater inflater, final ViewGroup container) {

        view = inflater.inflate(R.layout.gathering_main, container, false);
        ButterKnife.bind(this, view);

        initData();
        initList();

        return view;
    }

    private void initData() {
        thoughts.clear();
        thoughts.addAll(ThoughtManager.getSharedThoughts());

        if (currentSort == Sort.RATING_DESC) {
            Collections.sort(thoughts, new Comparator<BaasDocument>() {
                @Override
                public int compare(BaasDocument lhs, BaasDocument rhs) {
                    return rhs.getInt(ThoughtDao.Properties.Rating.name) - lhs.getInt(ThoughtDao.Properties.Rating.name);
                }
            });
        } else if (currentSort == Sort.DATE_DESC) {
            Collections.sort(thoughts, new Comparator<BaasDocument>() {
                @Override
                public int compare(BaasDocument lhs, BaasDocument rhs) {
                    return (int) (rhs.getLong(ThoughtDao.Properties.Date.name) - lhs.getLong(ThoughtDao.Properties.Date.name));
                }
            });
        }
    }

    public void reloadData() {
        BaasBoxMortalityDay.getAllThoughts();
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

    public void showPopupMenu(View v) {
        PopupMenu popup = new PopupMenu(activity, v);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.gathering_menu, popup.getMenu());
        popup.show();
    }

    public void reloadAdapter() {
        initData();
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gathering_menu_sort_date:
                currentSort = Sort.DATE_DESC;
                reloadAdapter();
                return true;
            case R.id.gathering_menu_sort_rating:
                currentSort = Sort.RATING_DESC;
                reloadAdapter();
                return true;
            default:
                return false;
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<BaasDocument> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            @Bind(R.id.thoughts_text) TextView text;
            @Bind(R.id.thoughts_category) TextView category;
            @Bind(R.id.thoughts_author) TextView author;
            @Bind(R.id.thoughts_date) TextView date;

            @Bind(R.id.gathering_item_rating) TextView rating;
            @Bind(R.id.gathering_item_root) View root;
            @Bind(R.id.gathering_item_voteup_icon_container) View voteUp;
            @Bind(R.id.gathering_item_votedown_icon_container) View voteDown;
            @Bind(R.id.gathering_item_delete_icon_container) View delete;

            public ViewHolder(View v) {
                super(v);
                ButterKnife.bind(this, v);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(List<BaasDocument> myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gathering_item, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            final BaasDocument thought = mDataset.get(position);

            holder.text.setText(thought.getString(ThoughtDao.Properties.Text.name));
            holder.category.setText(activity.getResources().getStringArray(R.array.thoughts_categories)[thought.getInt(ThoughtDao.Properties.Category.name)]);
            holder.rating.setText("" + thought.getInt(ThoughtDao.Properties.Rating.name));
            holder.author.setText(thought.getString(ThoughtDao.Properties.Author.name));
            holder.date.setText(MortalityDayUtil.getDateFormatted(thought.getLong(ThoughtDao.Properties.Date.name)));

            if (!Preferences.isAdmin() && DaoFactory.getInstanceLocal().getExtendedThoughtMetaDao().getThoughtMeta(thought.getString(ThoughtDao.Properties.Key.name)) != null){
                holder.voteUp.setVisibility(View.INVISIBLE);
                holder.voteDown.setVisibility(View.INVISIBLE);
            } else {
                holder.voteUp.setVisibility(View.VISIBLE);
                holder.voteDown.setVisibility(View.VISIBLE);

                holder.voteUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        voteUp(thought);
                    }
                });

                holder.voteDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        voteDown(thought);
                    }
                });
            }

            if (Preferences.isAdmin()) {
                holder.delete.setVisibility(View.VISIBLE);
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete(thought);
                    }
                });
            } else {
                holder.delete.setVisibility(View.GONE);
            }

        }

        private void delete(BaasDocument thought) {
            BaasBoxMortalityDay.deleteThought(thought);
        }

        private void voteUp(BaasDocument thought) {
            BaasBoxMortalityDay.voteUpThought(thought);
        }

        private void voteDown(BaasDocument thought) {
            BaasBoxMortalityDay.voteDownThought(thought);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

}
