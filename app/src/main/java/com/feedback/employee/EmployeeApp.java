package com.feedback.employee;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by user on 24/07/17.
 */

public class EmployeeApp extends Application {

    public static FirebaseDatabase secondDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
       /* try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setApiKey("AIzaSyAGI53BJS1w4Iqqh9moJV1hZFPgrTY-3Sk")
                    .setApplicationId("1:449402630928:android:437412bb9661e18e")
                    .setDatabaseUrl("https://feedback-e7e31.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(getApplicationContext(), options, "HrFeedback");
            FirebaseApp secondApp = FirebaseApp.getInstance("HrFeedback");
            secondDatabase = FirebaseDatabase.getInstance(secondApp);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
