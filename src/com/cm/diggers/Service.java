/*
 * Crystal McDonald
 * Java II
 * 1309
 * Week 1
 */
package com.cm.diggers;

import com.cm.diggers.TideActivity;
import com.cm.diggers.DataFile;
import android.app.Activity;
import android.app.IntentService;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class Service extends IntentService{

//	public static final String MESSENGER_KEY = "messenger";
//	public static final String TIDE_KEY = "arg1";
//	public static final String OBJ_KEY = "obj";
	
	private Messenger messenger;

	public Service() {
		super("Service");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
		Log.i("onHandleIntent", "started");
		
		//retreiving info to handle in service
		//Bundle extras = intent.getExtras();
		//access to the handler
		//Messenger messenger = (Messenger)extras.get(MESSENGER_KEY);
		//String arg1 = extras.getString(TIDE_KEY);
		
		//value entered in and return message to main activity
		//Object message, messenger;
		 try {
           Message  message = Message.obtain();
             message.arg1 = Activity.RESULT_OK;
             message.obj = "Data Service Complete";
             //send message to activity
             messenger.send(message);//created a field for messenger because it caused an error
           
             Log.d("SERVICE CLASS", "onHandleIntent()");
           
     } catch (RemoteException e) {
             Log.e("EXCEPTION ON HANDLE INTENT", e.getMessage().toString());
             e.printStackTrace();
     }
		
	}

}
