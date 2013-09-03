/*
 * Crystal McDonald
 * Java II
 * 1309
 * Week1
 */
package com.cm.diggers;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Setting the view
		setContentView(R.layout.activity_main);
		
		//tvInfo = (TextView) findViewById(R.id.textview1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
