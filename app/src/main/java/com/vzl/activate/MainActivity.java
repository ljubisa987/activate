package com.vzl.activate;

import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends GoogleApiBaseActivity {

    private final static DateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final String KEY_STEPS = "steps";
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);
    }

    @Override
    public void onClientConnected() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "onClientConnected start getting data...");

                DataReadResult result = Fitness.HistoryApi.readData(mClient, generateDataReadRequest()).await
                        (1, TimeUnit.MINUTES);
                Log.i(TAG, "onClientConnected data fetched...");
                if (!result.getBuckets().isEmpty()) {
                    retrieveSteps(result);
                } else {
                    Log.i(TAG, "result bucket is empty :( ");
                }
            }
        }).start();

    }

    private void retrieveSteps(DataReadResult dataReadResult) {
        Log.i(TAG, "trying to retrieve steps, data set bucket size: " + dataReadResult.getBuckets().size());

        final List<Integer> mSteps = new LinkedList<>();
        for (Bucket bucket : dataReadResult.getBuckets()) {
            List<DataSet> dataSets = bucket.getDataSets();
            for (DataSet dataSet : dataSets) {
                for (DataPoint dp : dataSet.getDataPoints()) {
                    String date = sDateFormat.format(dp.getTimestamp(TimeUnit.MILLISECONDS));
                    mSteps.add(dp.getValue(Field.FIELD_STEPS).asInt());
                    Log.i(TAG, KEY_STEPS + " : " + dp.getValue(Field.FIELD_STEPS).asInt() + " on " + date);
                }
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextView.setText("Count of data set:" + mSteps.size());
            }
        });

    }

    DataReadRequest generateDataReadRequest() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -10);
        long startTime = cal.getTimeInMillis();

        Log.i(TAG, "Range Start: " + sDateFormat.format(startTime));
        Log.i(TAG, "Range End: " + sDateFormat.format(endTime));

        DataSource ESTIMATED_STEP_DELTAS = new DataSource.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_steps")
                .setAppPackageName("com.google.android.gms")
                .build();
        return new DataReadRequest.Builder()
                .aggregate(ESTIMATED_STEP_DELTAS, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.logoutFromApi).setEnabled(mIsConnected);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutFromApi:
                Fitness.ConfigApi.disableFit(mClient);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
