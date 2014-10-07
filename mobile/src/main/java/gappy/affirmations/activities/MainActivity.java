package gappy.affirmations.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import gappy.affirmations.NewsParser;
import gappy.affirmations.R;
import gappy.affirmations.dataitems.NewsItem;
import gappy.affirmations.fragments.NavigationDrawerFragment;
import gappy.affirmations.fragments.NewsFragment;


public class MainActivity extends Activity

        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
                   NewsFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private int newsFragment;
    private int calendarFragment;

    public boolean isLoadingDone() {
        return loadingDone;
    }

    private boolean loadingDone;
    private List<NewsItem> newsItems;

    private NewsFragment newsFragmentPointer;

    public List<NewsItem> getNewsItems() {
        return newsItems;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, new NewsFragment());
        fragmentTransaction.commit();

        newsFragmentPointer = (NewsFragment) getFragmentManager().findFragmentById(R.id.newsFragment);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        if(position == 0) {
            newsFragment = fragmentManager.beginTransaction()
                    .replace(R.id.container, NewsFragment.newInstance())
                    .commit();
        } else if(position == 1) {
//            fragmentManager.beginTransaction()
//                    .replace(R.id.container, CalendarFragment.newInstance(position + 1))
//                    .commit();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if(fragment.getClass().equals(NewsFragment.class)) {
            newsFragmentPointer = (NewsFragment)fragment;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_news);
                break;
            case 2:
                mTitle = getString(R.string.title_calendar);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        new DownloadXmlTask().execute(getString(R.string.news_feed));
        new DownloadXmlTask().execute(getString(R.string.calendar_feed));

    }

    @Override
    public void onFragmentInteraction(String id) {
        //TODO
    }

    private class DownloadXmlTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            loadingDone = false;
            try {
                newsItems =  loadXmlFromNetwork(urls[0]);
                return null;
            } catch (IOException e) {
                return getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {
                return getResources().getString(R.string.xml_error);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            updateLayout();
            loadingDone = true;
        }
    }


    private void updateLayout() {
        newsFragmentPointer.updateView();
    }

    private List<NewsItem> loadXmlFromNetwork(String urlString) throws XmlPullParserException,
            IOException {
        InputStream stream = null;
        // Instantiate the parser
        NewsParser newsParser = new NewsParser();
        List<NewsItem> entries = null;

        try {
            stream = downloadUrl(urlString);
            newsItems = newsParser.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return entries;
    }

    // Given a string representation of a URL, sets up a connection and gets
// an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }

}
