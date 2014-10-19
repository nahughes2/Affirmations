package gappy.affirmations.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import gappy.affirmations.R;
import gappy.affirmations.activities.MainActivity;
import gappy.affirmations.dataitems.NewsItem;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link }
 * interface.
 */
public class NewsFragment extends Fragment {



    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    public NewsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<NewsItem> newsList = ((MainActivity)getActivity()).getNewsItems();
        if(newsList != null) {

            String[] titleArray = new String[newsList.size()];
            for (int i = 0; i < newsList.size(); i++) {
                titleArray[i] = newsList.get(i).getTitle();
            }
            mAdapter = new NewsArrayAdapter(getActivity(), titleArray);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(R.id.list);
        if(mAdapter != null) {
            mListView.setAdapter(mAdapter);
        }


        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> myAdapter, View myView, int pos, long mylng) {


            }

        });

        return view;
    }

    public class NewsArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;

        public NewsArrayAdapter(Context context, String[] values) {
            super(context, R.layout.news_list_item, values);
            this.context = context;
            this.values = values;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.news_list_item, parent, false);
            TextView titleView = (TextView) rowView.findViewById(R.id.title);
            TextView categoryView = (TextView) rowView.findViewById(R.id.category);
            TextView dateView = (TextView) rowView.findViewById(R.id.date);

            List<NewsItem> newsList = ((MainActivity)getActivity()).getNewsItems();

            titleView.setText(newsList.get(position).getTitle());
            categoryView.setText(newsList.get(position).getCategory());
            dateView.setText(newsList.get(position).getDate());

            return rowView;
        }
    }

    public void updateView(){
        if(mAdapter == null) {
            List<NewsItem> newsList = ((MainActivity)getActivity()).getNewsItems();
            if(newsList != null) {

                String[] titleArray = new String[newsList.size()];
                for (int i = 0; i < newsList.size(); i++) {
                    titleArray[i] = newsList.get(i).getTitle();
                }
                mAdapter = new NewsArrayAdapter(getActivity(), titleArray);
                mListView.setAdapter(mAdapter);
            }
        }
        mListView.invalidate();
    }

}
