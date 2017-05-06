package com.pizzaTracking.android.Common;

/**
 * Created by Moataz.Moustafa on 5/3/2017.
 */

public abstract class DataRetrieved {

    private Object retData;
    public void SetDataAndRun(Object data){
        retData = data;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                OnDataRetrieved(retData);
            }
        };
        task.run();
    }
    public abstract void OnDataRetrieved(Object data);
 }
