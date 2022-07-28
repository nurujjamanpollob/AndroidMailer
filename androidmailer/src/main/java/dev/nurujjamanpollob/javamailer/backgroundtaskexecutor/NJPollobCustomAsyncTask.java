/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-", "$today.year")2022 Nurujjaman Pollob, All Right Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * If you have contributed in codebase,
 * and want to add your name or copyright in a particular class or method,
 * you must follow this following pattern:
 * <code>
 *     // For a new method created by you,
 *     //like this example method with name fooMethod()
 *     //then use following format:
 *
 *     >>>
 *     @author $Name and $CurrentYear.
 *     $Documentation here.
 *     $Notes
 *     public boolean fooMethod(){}
 *     <<<
 *
 *     // For an existing method
 *
 *     >>>
 *     $Current Method Documentation(Update if needed)
 *
 *     Updated by $YourName
 *     $Update summery
 *     $Notes(If any)
 *     <<<
 *
 *     // For a new class of file, that is not created by anyone else
 *     >>>
 *     Copyright (c) $CurrentYear $Name, All right reserved.
 *
 *     $Copyright Text.
 *     $Notes(If Any)
 *     <<<
 *
 *     // For a existing class, if you want to add your own copyright for your work.
 *
 *     >>>
 *     $Current Copyright text
 *
 *     $YourCopyrightText
 *     <<<
 *
 *     Done! Clean code!!
 * </code>
 *
 *
 */

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
