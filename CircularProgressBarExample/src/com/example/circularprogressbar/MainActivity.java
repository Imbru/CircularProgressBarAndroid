package com.example.circularprogressbar;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.cm.utility.circularprogressbarlibrary.CircularProgressBar;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	private CircularProgressBar circularProgressBar;
	private ProgressBar progressBar;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		circularProgressBar = (CircularProgressBar) findViewById(R.id.pb_demo);
		progressBar = (ProgressBar) findViewById(R.id.pb_progress);

		final Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {

				do {
					 mHandler.post(new Runnable() {
                         public void run() {
                        		progressBar.setProgress(progressBar.getProgress()+1);
            					circularProgressBar.setProgress(circularProgressBar.getProgress()+1.0f);
            					circularProgressBar.setCenterText((int)circularProgressBar.getProgress()+"%");	
                         }
                     });
					
					if(circularProgressBar.getProgress()==100) {
						break;
					}

					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				} while (circularProgressBar.getProgress()!=100);

			}
		});
		thread.start();

	}


}
