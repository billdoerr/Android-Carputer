package com.billdoerr.android.carputer.taskutils;

import android.content.Context;

import com.billdoerr.android.carputer.settings.Node;

import java.util.List;

//  Class AsyncTask parameters
public class TaskRequest {
    public Context context;
    public List<Node> nodes;
    public String cmd;
    public String taskName;
}
