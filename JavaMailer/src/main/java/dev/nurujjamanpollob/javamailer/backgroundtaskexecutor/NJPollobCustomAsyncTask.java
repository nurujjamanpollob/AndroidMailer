package dev.nurujjamanpollob.javamailer.backgroundtaskexecutor;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SuppressWarnings({"unused"})
public abstract class NJPollobCustomAsyncTask<Progress, Result> {


    private int numOfThreads = 1;
    private static ExecutorService exc;
    private static final int MESSAGE_POST_RESULT = 0x1;


    public void runThread() {

        preExecute();

        exc = Executors.newFixedThreadPool(numOfThreads);

        exc.execute(() -> {

            Result result = doBackgroundTask();

            postResult(result);


        });

    }


    private void postResult(Result result) {
        Message message = getHandler().obtainMessage(MESSAGE_POST_RESULT, new NJPollobCustomAsyncTaskResult<>(this, result));
        message.sendToTarget();
    }


    private void postProgress(Progress progress){
        Message message = getHandler().obtainMessage(MESSAGE_POST_RESULT, new NJPollobCustomAsyncTaskProgressUpdate<>(this, progress));
        message.sendToTarget();
    }

    @MainThread
    protected void preExecute() {


    }

    @WorkerThread
    protected abstract Result doBackgroundTask();



    @MainThread
    protected void onTaskFinished(Result result) {

        // Cancel all work instance
        cancelWork();

    }

    @SuppressWarnings({"UnusedDeclaration"})
    @MainThread
    public void defineThreadCount(int numOfThread) {
        this.numOfThreads = numOfThread;
    }


    public void cancelWork() {

        if (!exc.isShutdown()) {

            exc.shutdown();

        }


    }


    @MainThread
    @SuppressWarnings({"UnusedDeclaration"})
    protected void onProgressUpdated(Progress progress) {

    }


    @SuppressWarnings({"UnusedDeclaration"})
    @WorkerThread
    protected final void publishProgress(Progress values) {

        postProgress(values);
    }



    private Handler getHandler() {
        return new Handler(Looper.getMainLooper());
    }

    private static class NJPollobCustomAsyncTaskProgressUpdate<Progress> {

        @SuppressWarnings({"unchecked"})
        public NJPollobCustomAsyncTaskProgressUpdate(
                @SuppressWarnings("rawtypes")
                        NJPollobCustomAsyncTask task,
                Progress result) {

            // call on progress updated
            task.onProgressUpdated(result);

        }

    }



    private static class NJPollobCustomAsyncTaskResult<Result> {


        @SuppressWarnings({"unchecked"})
        NJPollobCustomAsyncTaskResult(@SuppressWarnings("rawtypes")NJPollobCustomAsyncTask task, Result result) {

            task.cancelWork();

            try {

                // Invoke task finish
                task.onTaskFinished(result);

            }catch (Exception ignored){
                // Call Looper Prepare
                Looper.prepare();
                // Invoke task finish
                task.onTaskFinished(result);

            }

        }

    }







    public void setTaskFinishedEventListener(OnAsyncTaskFinishedListener finishedListener){


    }


}
