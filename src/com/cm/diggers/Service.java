/*
 * Crystal McDonald
 * Java II
 * 1309
 * Week 1
 * 
 * (S.Dubin)
 */
package com.cm.diggers;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

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

	public static final String MESSENGER_KEY = "messenger";
	public static final String TIDE_KEY = "arg1";
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
		Message  message = Message.obtain();
        
		//Retrieving info to handle in service
		Bundle extras = intent.getExtras();
		//access to the handler
		messenger = (Messenger)extras.get(MESSENGER_KEY);
		String arg1 = extras.getString(TIDE_KEY);
		
		//value entered in and return message to main activity
		//Object message, messenger;
		
		//Call JSON web api here
		URL finalUrl =null;
		try {
			finalUrl = new URL("http://api.wunderground.com/api/3e64fa36c4f09bdd/tide/q/WA/seattle.json");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			
			String response = WebFile.getURLSTringResponse(finalUrl);
			Log.i("SERVICE", response);
			
			//Store your Data into a file here
			JSONObject json;
			try {
				json = new JSONObject(response);
				DataFile.storeStringFile(getBaseContext(), "tideInfo.txt", json.toString(), false);
				Log.d("JSON DEBUG", json.toString());
				message.arg1 = Activity.RESULT_OK;
			} catch (JSONException e) {
                
				Log.e("JSON EXCEPTION", e.getMessage().toString());
				e.printStackTrace();
				message.arg1 = Activity.RESULT_CANCELED;
			}
            
		} catch (Exception e) {
			Log.e("URL STRING RESPONSE EXCEPTION", e.getMessage().toString());
		}
		
		
        try {
            
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
