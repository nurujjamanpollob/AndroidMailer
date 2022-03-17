package dev.nurujjamanpollob.javamailer.backgroundtaskexecutor;

public interface OnAsyncTaskFinishedListener{

    default void onTaskFinishedByWorker(@SuppressWarnings("rawtypes")NJPollobCustomAsyncTask asyncTask){}
    default void onTaskCancelled(@SuppressWarnings("rawtypes")NJPollobCustomAsyncTask asyncTask){}


}