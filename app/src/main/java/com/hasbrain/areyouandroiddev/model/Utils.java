package com.hasbrain.areyouandroiddev.model;

import android.text.Html;
import android.text.Spanned;

import java.util.Date;

public class Utils {
    private static final String TAG = "Utils", STICKYCOLOR = "387801", DEFAULTCOLOR = "000000";
    private static final String HTMLFormat =
            "<big><big><big>%d </big></big></big><font color='#0A295A'><b>%s</b></font> in <font color='#0A295A'><b>%s</b></font><br>" +
                    "<big><big><font color='#%s'>%s</font></big></big><br>" +
                    "%d Comments • %s • %s ago";


    public static Spanned HtmlFactory(RedditPost redditPost) {
        final String html_str =
                String.format(HTMLFormat,
                        redditPost.getScore(),
                        redditPost.getAuthor(),
                        redditPost.getSubreddit(),
                        redditPost.isStickyPost() ? STICKYCOLOR : DEFAULTCOLOR,
                        redditPost.getTitle(),
                        redditPost.getCommentCount(),
                        redditPost.getDomain(),
                        getDurationString(redditPost.getCreatedUTC()));
        return Html.fromHtml(html_str);
    }

    private static String getDurationString(long createdUTC) {
        long offset = (new Date().getTime() - createdUTC) / 1000;
        if (offset / 60 == 0)
            return offset + " secs";
        offset /= 60;
        if (offset / 60 == 0)
            return offset + " mins";
        offset /= 60;
        if (offset / 24 == 0)
            return offset + " hrs";
        offset /= 24;
        return offset + " days";
    }
}
