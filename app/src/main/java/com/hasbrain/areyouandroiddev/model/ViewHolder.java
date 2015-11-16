package com.hasbrain.areyouandroiddev.model;

import android.widget.TextView;

public class ViewHolder {
    TextView score, author, title, comment;

    public ViewHolder(TextView core, TextView author, TextView title, TextView comment) {
        this.score = core;
        this.author = author;
        this.title = title;
        this.comment = comment;
    }
}