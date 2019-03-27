package com.billdoerr.android.carputer.asynctaskutils;


/**
 *
 */
public interface TaskResponse {

    void taskFinished(TaskResult taskResult);

    void hideProgress();

    void showProgress();

    void taskTimeout(TaskResult taskResult);

    void taskCanceled(TaskResult taskResult);


}
