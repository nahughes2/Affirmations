package gappy.affirmations;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import gappy.affirmations.dataitems.NewsItem;

/**
 * Created by nhughes on 9/30/14.
 */
public class NewsParser {

    private static final String ns = null;

    public List parse(InputStream input) throws XmlPullParserException, IOException {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(input, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            input.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException,
            IOException {
        List entries = new ArrayList();
        int event = parser.getEventType();
        NewsItem newsItem = new NewsItem();
        //parse whole document
        while (event != XmlPullParser.END_DOCUMENT)
        {
            String name=parser.getName();
            switch (event){
                case XmlPullParser.START_TAG:
                    if (parser.next() == XmlPullParser.TEXT) {
                        if(name.equals("title")){
                            newsItem.setmTitle(parser.getText());
                            parser.next();
                        } else if (name.equals("link")) {
                            newsItem.setmLink(parser.getText());
                            parser.next();
                        } else if (name.equals("description")) {
                            newsItem.setmDescription(parser.getText());
                            parser.next();
                        } else if (name.equals("category")) {
                            newsItem.setmCategory(parser.getText());
                            parser.next();
                        } else if (name.equals("pubDate")) {
                            newsItem.setmDate(parser.getText());
                            entries.add(newsItem);
                            newsItem = new NewsItem();
                            parser.next();
                        }
                    }
                    break;
            }
            event = parser.next();
        }

        return entries;
    }

}
