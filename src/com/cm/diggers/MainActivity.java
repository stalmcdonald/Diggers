/*
 * Crystal McDonald
 * Java II
 * 1309
 * Week1
 */
package com.cm.diggers;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Setting the view
		setContentView(R.layout.activity_main);
		
		/*		
		 * 		-----------------				SPLASH SCREEN               ------------------------
		 */
		//setup a thread 
		Thread logoTimer = new Thread(){//make it sleep. start activity for a few seconds then kill itself
			public void run(){
				try{
					sleep(11000);//5seconds
					//want to open the menu class.
					Intent menuIntent = new Intent("com.cm.diggers.TideActivity");
					//after sleep starts an activity
					startActivity(menuIntent);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				finally{
					finish();
				}
			}			
		};//thread end
		
		logoTimer.start();
		
	}//onCreate method end
	/*		
	 * 		-----------------		END	SPLASH SCREEN         ------------------------
	 */
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
