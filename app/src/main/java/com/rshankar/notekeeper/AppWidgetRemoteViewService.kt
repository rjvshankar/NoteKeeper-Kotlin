package com.rshankar.notekeeper

/**
Created by rajiv on 4/7/20
 */

import android.content.Intent
import android.widget.RemoteViewsService

class AppWidgetRemoteViewService : RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory {
        return AppWidgetRemoteViewsFactory(applicationContext)
    }
}