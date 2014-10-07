package gappy.affirmations.dataitems;

import java.util.Date;

/**
 * Created by nhughes on 7/31/14.
 */
public class CalendarItem {

    private String mTitle;
    private String mLink;
    private String mGuide;
    private String mDescription;
    private String mDate;
    private Date mEventDate;


    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmLink() {
        return mLink;
    }

    public void setmLink(String mLink) {
        this.mLink = mLink;
    }

    public String getmGuide() {
        return mGuide;
    }

    public void setmGuide(String mGuide) {
        this.mGuide = mGuide;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

}
