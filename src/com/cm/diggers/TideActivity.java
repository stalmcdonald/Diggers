/*
 * 
 * Crystal McDonald
 * Java II
 * 1309
 * Week 1
 * (Working with the help of S.Dubin, Course Instructor FSU)
 */
package com.cm.diggers;

import com.cm.diggers.R;
import com.cm.diggers.R.string;
import com.cm.diggers.DataFile;
import com.cm.diggers.Service;


import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import java.util.HashMap;
import android.util.Log;

import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TideActivity extends Activity { 
	
	//Create my custom API URL
	//pulling city tide prediction from the wunderground api
	//string reference URL
	static final String baseURL = "http://api.wunderground.com/api/3e64fa36c4f09bdd/tide/q/WA/";
	
	//text view will change for tide text
	 TextView tvCity,tvPrediction, tvWater;
	 TextView calendar, tidepre, waveheight, tidesite;
	 EditText etCity;
	 Context _context;
	 //JSONObject results, type, tide, tideInfo;
	 HashMap<String, String> _history;
	 Boolean _connected = false;//want to assume not connected
	 private Button b;  //global button
	 
	  /** Called when the activity is first created. */
	        @Override
	   public void onCreate(Bundle tidalcycle) {
	           super.onCreate(tidalcycle);
	           _context = this;
	           _history = getHistory();
	           Log.i("HISTORY READ",_history.toString());

	      		
	           setContentView(R.layout.tide);
	           b = (Button)findViewById(R.id.bPrediction);
	           
	           etCity = (EditText)findViewById(R.id.etCity);
	           
	           tvCity = (TextView)findViewById(R.id.tvCity);
	           tvPrediction = (TextView)findViewById(R.id.tvPrediction);
	           tvWater = (TextView)findViewById(R.id.tvWater);
	           tidesite = (TextView)findViewById(R.id.tidesite);//location
	           calendar = (TextView)findViewById(R.id.calendar);//date
	           tidepre = (TextView)findViewById(R.id.tidepre);//high or low tide
	           waveheight = (TextView)findViewById(R.id.waveheight);//swell height
	           
	           //set a button for onclicklistener
	           b.setOnClickListener(new OnClickListener() {
	       		
	        	   //gets text entered in edit text and appends to textview along with data pulled from json
                   @SuppressWarnings("deprecation")
				@Override
                   public void onClick(View v) {
                          
                       // getting text edited and appending it to a string
                       String c = etCity.getText().toString();
                       String p = etCity.getText().toString();
                       String w = etCity.getText().toString();
                       String cal = calendar.getText().toString();//date
                       String ts = tidesite.getText().toString();//location
                       String tp = tidepre.getText().toString();//high/low
                       String wh = waveheight.getText().toString();//swell
                       StringBuilder URL = new StringBuilder(baseURL);
                          
                       // this hides the keyboard after user selects the predict button
                       InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	        		   imm.hideSoftInputFromWindow(b.getWindowToken(), 0);
	        		   
	        		   // sdubin, changed this a little   
	        		   String tempUrl = "";
	        		   tempUrl = new String(baseURL + c + ".json");
                       // sdubin, end changes/additions
	        		    
	        		   Handler myDataHandler = new Handler() {
                            public void handleMessage (Message msg) {
                            	
                                    Log.i("SERVICES", "handleMessage");
                                    String response = null;
                                   //validate contents of the message, check the object as well to send data back
                                    if (msg.arg1 == RESULT_OK && msg.obj != null) {//arg1 used for status info
                                            try {
                                                    response = (String) msg.obj;
                                                    Log.i("JSON response", response);
                                                   
                                                    //displayData();                                                               

                                            } catch (Exception e) {
                                                    Log.e("JSON RESPONSE", e.getMessage().toString());
                                            } 
                                            
                                    }

                            }
                           
                    };
	        		   //implementing messenger and intent
	        		   Messenger dataMessenger = new Messenger(myDataHandler);
	        		   Intent startDataServiceIntent = new Intent(getBaseContext(),Service.class);
	        		   startDataServiceIntent.putExtra("messenger", dataMessenger);
	        		   startDataServiceIntent.putExtra(c, tempUrl);
	        		   startService(startDataServiceIntent);
                       
                       URL finalURL;                       
                       try{

                    	   //fixed finalURL
                    	   finalURL = new URL(tempUrl);

                             Log.i("FINAL URL", finalURL.toString());
                             
                             //call to AsyncTask 
                             LocRequest lr = new LocRequest();
                            // lr.execute(finalURL);
                           
                            
                     } catch (MalformedURLException e){
                    
                             Log.e("BAD URL", "MALFORMED URL");
                             tvCity.setText("Can not provide information at this time");
                             tvPrediction.setText( p + " Tide Prediction: UNKNOWN");
                             tvWater.setText(w + ": Location: UNKOWN");
                             etCity.setText(URL);
                     } finally {
                             // This is done even if try block fails
                                 Log.i("LOG", "I have hit the finally statement");
                     }

               }
            });
                          
                          
    }
	   
 		
	
	public String dataToString(){
		return "In " + etCity + " The tide prediction: High";
	}
	
	private JSONObject JSONObject(String result) {
		// TODO Auto-generated method stub
		return null;
	}


	//create method to get history from Hard drive
    @SuppressWarnings("unchecked")
	private HashMap<String, String> getHistory(){
    	Object stored = DataFile.readObjectFile(_context, "history", false);
    	
    	HashMap<String, String> history;
    	if(stored == null){
    		Log.i("HISTORY", "NO HISTORY FILE FOUND");
    		history = new HashMap<String, String>();
    	}else{
    		history = (HashMap<String, String>)stored;
    	}
    	return history;
    }
    
    @SuppressWarnings("unused")
	private class LocRequest extends AsyncTask<URL,Void,String>{
    	//override 2 separate functions
    	@Override
    	protected String doInBackground(URL...urls){
    		String response = "";
    		
    		//pass an array even though it only holds one
    		for(URL url: urls){
    			Log.e("URL DOB", url.toString());
    			response = WebFile.getURLSTringResponse(url);
    		}
    		return response;
    	}
    	
    	//onPostExecute now inside the LocRequest class, it is a 
    	// required interface class for AsyncTask
    	@Override
       	protected void onPostExecute(String result){
       		Log.i("JSON RESULTS", result);
       		
       		
       		try{
       			//parsing through JSON Data accepts a string as a parameter
       			JSONObject json = new JSONObject("tide");
       				JSONObject results = json.getJSONObject("utcdate").getJSONObject("data");
       			if(results.getString("type") != null){
       				Toast toast = Toast.makeText(_context, "Invalid City Entered ", Toast.LENGTH_LONG);
       				toast.show();
//       				String type;
//					tvCity.setText("In " + c + " The tide prediction:" + type);
//                  tvPrediction.setText( p + " tide prediction:"+ results);
//                  tvWater.setText(w + ": Puget Sound");
       			}else{
       				Toast toast = Toast.makeText(_context, "Tide Info" + results.get("pretty"), Toast.LENGTH_LONG);
       				toast.show();

       				//makes sure history is there
       				_history.put(results.getString("string"), results.toString());
       				//target file to write history to harddrive
       				DataFile.storeObjectFile(_context, "tide", _history, false);
       				DataFile.storeStringFile(_context, "tideInfo", results.toString(), true);
       			}
       		} 
       		catch (JSONException e){
       			Log.e("JSON", "JSON OBJECT EXCEPTION " + e.toString());
       		}
    	}
       		
    }
    	
    
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.tide, container, false);
    }
    
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        
        view = view.findViewWithTag(R.id.class);
    }

/* uncommented for now will need before turn in
 checking network connection
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//Detects the network connection
  		_connected = WebFile.getConnectionStatus(_context);
  		if(_connected){
  			Log.i("NETWORK CONNECTION ", WebFile.getConnnectionType(_context));
  		}
	}
*/	
  
}//end activity
