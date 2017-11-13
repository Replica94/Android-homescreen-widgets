package com.example.widgettesteri.widgettest2;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by MacodiusMaximus on 12.11.2017.
 */



public class DataRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new DataWidget.DataRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
