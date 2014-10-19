package gappy.affirmations.dataitems;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nhughes on 7/31/14.
 */
public class NewsItem {

    private String mTitle;
    private String mLink;
    private String mGuide;
    private String mDescription;
    private String mCategory;
    private String mDate;



    public String getTitle() {
        return mTitle;
    }

    public String getLink() {
        return mLink;
    }

    public String getGuide() {
        return mGuide;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getDate() {
        return mDate;
    }


    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmLink(String mLink) {
        this.mLink = mLink;
    }

    public void setmGuide(String mGuide) {
        this.mGuide = mGuide;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public void setmDate(String date) {
        //convert string to date
        //Wed, 8 Oct 2014 16:30:00 GMT

        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:SS Z");
        SimpleDateFormat TEST_DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy");
        try {
            Date newDate = DATE_FORMAT.parse(date);
            mDate = TEST_DATE_FORMAT.format(newDate);
        } catch (ParseException e) {
            mDate = "";
        }

    }

}
