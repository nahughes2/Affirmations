package gappy.affirmations.activities;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import gappy.affirmations.CalendarParser;
import gappy.affirmations.NewsParser;
import gappy.affirmations.R;
import gappy.affirmations.dataitems.CalendarItem;
import gappy.affirmations.dataitems.NewsItem;
import gappy.affirmations.fragments.CalendarFragment;
import gappy.affirmations.fragments.NewsFragment;


public class MainActivity extends FragmentActivity {

    private List<NewsItem> newsItems;
    private List<CalendarItem> calendarItems;

    private static final int NUM_PAGES = 2;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    public List<NewsItem> getNewsItems() {
        return newsItems;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        setContentView(R.layout.view_pager);

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new NewsFragment());
        fragments.add(new CalendarFragment());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

            }
        };

        actionBar.addTab(
                actionBar.newTab()
                        .setText("News")
                        .setTabListener(tabListener));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Calendar ")
                        .setTabListener(tabListener));

    }

    @Override
    public void onStart() {
        super.onStart();

        new DownloadNewsTask().execute(getString(R.string.news_feed));
        new DownloadCalendarTask().execute(getString(R.string.calendar_feed));

    }

    private void updateLayout() {
        Fragment theNewsFragment = ((ScreenSlidePagerAdapter)mPagerAdapter).getItem(0);
        ((NewsFragment)theNewsFragment).updateView();
    }

    private class DownloadNewsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                newsItems =  loadXmlFromNetwork(urls[0]);
                if(newsItems != null) {
                    newsItems.remove(0);
                }
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
        }
    }

    private class DownloadCalendarTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                calendarItems =  loadXmlFromNetworkCalendar(urls[0]);
                if(calendarItems != null) {
                    calendarItems.remove(0);
                }
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
        }
    }

    private List<NewsItem> loadXmlFromNetwork(String urlString) throws XmlPullParserException,
            IOException {
        InputStream stream = null;


            // Instantiate the parser
            NewsParser newsParser = new NewsParser();
            List<NewsItem> entries = null;

            try {
                stream = downloadUrl(urlString);
                entries = newsParser.parse(stream);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (stream != null) {
                    stream.close();
                }

            return entries;

        }
    }

    private List<CalendarItem> loadXmlFromNetworkCalendar(String urlString) throws
            XmlPullParserException,
            IOException {
        InputStream stream = null;


        // Instantiate the parser
        CalendarParser calendarParser = new CalendarParser();
        List<CalendarItem> entries = null;

        try {
            stream = downloadUrl(urlString);
            entries = calendarParser.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }

            return entries;

        }
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

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments;

        public ScreenSlidePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
