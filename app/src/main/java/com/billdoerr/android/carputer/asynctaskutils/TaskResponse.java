package com.billdoerr.android.carputer.asynctaskutils;


/**
 *
 */
public interface TaskResponse {

    void taskFinished(TaskResult taskResult);

//    void hideProgress();

    @SuppressWarnings("EmptyMethod")
    void showProgress();

//    void taskTimeout(TaskResult taskResult);

    void taskCanceled(TaskResult taskResult);

}
