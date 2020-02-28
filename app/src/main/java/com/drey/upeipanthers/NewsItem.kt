package com.drey.upeipanthers

import android.os.Build
import android.text.Html

class NewsItem(title1: String,
               val link: String,
               description1: String,
               val image_url: String
) {

    var title = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(title1, Html.FROM_HTML_MODE_COMPACT).toString()
    } else {
        Html.fromHtml(title1).toString()
    }

    var description = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(description1, Html.FROM_HTML_MODE_COMPACT).toString()
    } else {
        Html.fromHtml(description1).toString()
    }

}
