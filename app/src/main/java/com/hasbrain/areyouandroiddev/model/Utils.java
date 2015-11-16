package com.hasbrain.areyouandroiddev.model;

import android.text.Html;
import android.text.Spanned;

import java.util.Date;

public class Utils {
    private static final String STICKYCOLOR = "387801", DEFAULTBLACK = "000000", DEFAULTWHITE = "ffffff";
    //    private static final String HTMLFormat =
//            "<big><big><big>%d </big></big></big><font color='#0A295A'><b>%s</b></font> in <font color='#0A295A'><b>%s</b></font><br>" +
//                    "<big><big><font color='#%s'>%s</font></big></big><br>" +
//                    "%d Comments • %s • %s ago";
    private static final String AUTHOR_SUBREDDIT = "<font color='#0A295A'><b>%s</b></font> in <font color='#0A295A'><b>%s</b></font>";
    private static final String COMMENT_DOMAIN_CREATEDTIME = "%d Comments • %s • %s ago";
    private static final String TITLE_FORMAT = "<font color='#%s'>%s</font>";

//    public static Spanned HtmlFactory(RedditPost redditPost) {
//        final String html_str =
//                String.format(HTMLFormat,
//                        redditPost.getScore(),
//                        redditPost.getAuthor(),
//                        redditPost.getSubreddit(),
//                        redditPost.isStickyPost() ? STICKYCOLOR : DEFAULTCOLOR,
//                        redditPost.getTitle(),
//                        redditPost.getCommentCount(),
//                        redditPost.getDomain(),
//                        getDurationString(redditPost.getCreatedUTC()));
//        return Html.fromHtml(html_str);
//    }

    public static Spanned buildTitleText(String title, boolean isStickyPost, boolean isLandscape) {
        return Html.fromHtml(String.format(TITLE_FORMAT,
                isStickyPost ? STICKYCOLOR : (isLandscape ? DEFAULTWHITE : DEFAULTBLACK),
                title));
    }

    public static Spanned buildAuthorAndSubredditText(String author, String subreddit, boolean isLandscape) {
        if (isLandscape)
            return Html.fromHtml(author);
        else
            return Html.fromHtml(String.format(AUTHOR_SUBREDDIT, author, subreddit));
    }

    public static String buildCommentDomainCreatedtimeText(int comments, String domain, long createdUTC) {
        return String.format(COMMENT_DOMAIN_CREATEDTIME, comments, domain, getDurationString(createdUTC));
    }

    private static String getDurationString(long createdUTC) {
        long offset = (new Date().getTime() / 1000 - createdUTC);
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
