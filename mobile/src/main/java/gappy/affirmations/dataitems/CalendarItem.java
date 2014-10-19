package gappy.affirmations.dataitems;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nhughes on 7/31/14.
 */
public class CalendarItem {

    private String mTitle;
    private String mLink;
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

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String date) {
        Date convertedDate = null;
        //convert string to date
        //Wed, 8 Oct 2014 16:30:00 GMT

        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy:HH:mm:SS Z");
        mDate = DATE_FORMAT.format(date);
    }

    private Date convertDate(String date) {
        Date convertedDate = null;
        //convert string to date
        //Wed, 8 Oct 2014 16:30:00 GMT

        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy:HH:mm:SS Z");
        return convertedDate;

    }

}
