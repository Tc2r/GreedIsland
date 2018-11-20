package com.tc2r.greedisland.utils;

import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.Logger;


/**
 * Created by Nudennie.white on 3/26/18.
 */

public class PerformanceTracking extends AppCompatActivity {

    public static String Transaction = "NAMEOFTRANSACTION";
    public static void SetUsername(String username)
    {
        Logger.i("Setting username: " + username);

    }


    public static void TrackEvent(String breadcrumb)
    {
        Logger.d(breadcrumb);
        if(breadcrumb.length() > 140)
        {
            Logger.w("Trying to log a breadcrumb of more than 140 characters, this is unsupported");
        }
    }

    public static void TransactionBegin(String transactionName)
    {
        Logger.i("TransactionBegin: " + transactionName);

    }

    public static void TransactionEnd(String transactionName)
    {
        Logger.i("TransactionEnd: " + transactionName);

    }

    public static void TransactionFail(String transactionName)
    {
        Logger.e("TransactionFail: " + transactionName);

    }

    public static void TransactionCancel(String transactionName)
    {
        Logger.w("TransactionEnd: " + transactionName);

    }

    public static void TransactionSetValue(String transactionName, int value)
    {
        Logger.i("TransactionSetValue: " + transactionName + " value: " + value);


    }

    public static void TransactionGetValue(String transactionName)
    {
        Logger.i("TransactionGetValue: " + transactionName);

    }

    public static void HandledException(Exception exception)
    {
        Logger.w("HandlededException: " + exception);

    }
}
