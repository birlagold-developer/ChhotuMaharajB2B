package com.chhotumaharajbusiness.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chhotumaharajbusiness.R;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

public class SplashScreenActivity extends AppCompatActivity {


    private final static int SPLASH_SCREEN_TIMEOUT = 3000;
    private final static int REQUEST_CODE_APPLICATION_UPDATE = 1736;

    private AppUpdateManager appUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();

        String multiplicand = "175";
        String multiplier = "175";

        String[][] partialResult = new String[multiplier.length()][(multiplicand.length() + multiplier.length())];

        int rowPosition = 0, columnPosition = 0;

        for (int i = multiplier.length() - 1; i >= 0; i--) {
            int tmpMultiplier = Integer.parseInt("" + multiplier.charAt(i));

            int carryForwardValue = 0;
            for (int j = multiplicand.length() - 1; j >= 0; j--) {
                int tmpMultiplicand = Integer.parseInt("" + multiplicand.charAt(j));
                int tmpResult = (tmpMultiplier * tmpMultiplicand) + carryForwardValue;
                String tmpResultString = String.valueOf(tmpResult);

                if (tmpResult < 9) {
                    carryForwardValue = 0;
                    partialResult[rowPosition][columnPosition++] = tmpResultString;
                } else {
                    carryForwardValue = tmpResult / 10;
                    partialResult[rowPosition][columnPosition++] = "" + tmpResultString.charAt(tmpResultString.length() - 1);
                }
            }

            partialResult[rowPosition][columnPosition] = String.valueOf(carryForwardValue);
            columnPosition = ++rowPosition;
        }

        String[] finalResultArray = new String[(multiplicand.length() + multiplier.length())];

        int iCounter = 0;
        int jCounter = 0;

        boolean flag = true;

        int columnSum = 0;
        int carryForwardValue = 0;

        while (flag) {
            String value = partialResult[iCounter++][jCounter];
            columnSum += Integer.parseInt(value == null ? "0" : value);

            if (iCounter == partialResult.length) {
                columnSum = columnSum + carryForwardValue;
                String tmpColumnSum = String.valueOf(columnSum);

                if (columnSum < 9) {
                    carryForwardValue = 0;
                    finalResultArray[jCounter] = tmpColumnSum;
                } else {
                    carryForwardValue = columnSum / 10;
                    finalResultArray[jCounter] = "" + tmpColumnSum.charAt(tmpColumnSum.length() - 1);
                }

                columnSum = 0;
                iCounter = 0;

                jCounter++;

                if (jCounter == partialResult[iCounter].length) {
                    flag = false;
                }
            }
        }

        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < finalResultArray.length; i++) {
            sb.append(finalResultArray[i]);
        }

        String finalResultString = sb.reverse().toString();

        for (int i = 0; i < finalResultString.length() - 1; i++) {
            String value = String.valueOf(finalResultString.charAt(i));
            if (!value.equals("0")) {
                finalResultString = finalResultString.substring(i, finalResultString.length());
                break;
            }
        }

        System.out.println("Final Result : " + finalResultString);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                checkApplicationUpdate();

            }
        }, SPLASH_SCREEN_TIMEOUT);

    }

    private void requestUpdate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, REQUEST_CODE_APPLICATION_UPDATE);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_APPLICATION_UPDATE) {
            if (resultCode == RESULT_OK) {
                callNextActivity();
            } else {
                checkApplicationUpdate();
            }
        }
    }

    private void checkApplicationUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {

                System.out.println("AppUpdate:result.updateAvailability()=" + result.updateAvailability());
                System.out.println("AppUpdate:result.availableVersionCode()=" + result.availableVersionCode());
                System.out.println("AppUpdate:UpdateAvailability.UPDATE_AVAILABLE=" + UpdateAvailability.UPDATE_AVAILABLE);
                if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    Toast.makeText(getApplicationContext(), "Please Update Application", Toast.LENGTH_LONG).show();
                    requestUpdate(result);
                } else {
                    callNextActivity();
                }
            }
        });
    }

    private void callNextActivity() {
        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
