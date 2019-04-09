package com.bhagya.mapapplication.jobschedular;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import com.bhagya.mapapplication.asynctask.ConnectivityCheckerAndDataUpdater;
import java.util.List;
/**
 * Created by Bhagya on 11/21/2017.
 */
public class JobschedulerForUpdateData extends JobService {
private ConnectivityCheckerAndDataUpdater connectivityCheckerAndDataUpdater;
    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final JobParameters params) {
        connectivityCheckerAndDataUpdater=new ConnectivityCheckerAndDataUpdater(){
            @Override
            protected void onPostExecute(String s) {
                Toast.makeText(getApplicationContext()," somthing"+s,Toast.LENGTH_LONG).show();
                Log.d("BackGroundTask","BackGroundTask"+s);
                jobFinished(params,false);
            }
        };
        connectivityCheckerAndDataUpdater.execute();
        return true;
    }
    @Override
    public boolean onStopJob(JobParameters params) {
        connectivityCheckerAndDataUpdater.cancel(true);
        return false;
    }
}
