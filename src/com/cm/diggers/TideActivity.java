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
	        		   
	        		 //Detects the network connection
	        	  		_connected = WebFile.getConnectionStatus(_context);
	        	  		if(_connected){
	        	  			Log.i("NETWORK CONNECTION ", WebFile.getConnnectionType(_context));
	        	  		}
	        		
	        		   
	        		   //Callback Method
	        		   Handler myHandler = new Handler(){
	        			   
	        			   public void handleMessage(Message msg){
	        				   super.handleMessage(msg);
	        				   
	        				   updateUI();
	        			   }
	        		   };
	        		   
	        		   	
	        		   String tempUrl = "";
	        		   tempUrl = new String(baseURL + c + ".json");
	        		    
                       
                       URL finalURL;                       
                       try{

                    	   //fixed finalURL
                    	   finalURL = new URL(tempUrl);
                    	   Log.i("FINAL URL", finalURL.toString());
                    	   
                    	   Messenger myMessenger = new Messenger(myHandler);
                    	   Intent myIntent = new Intent(_context, Service.class);
                    	   myIntent.putExtra("messenger", myMessenger);
                    	   myIntent.putExtra("tidal_city", c);
                    	   myIntent.putExtra("final_URL", finalURL.toString());
                    	   Log.i("TIDE ACTIVITY", "Starting Service");
                    	   //start the service the handleMessage method wont be called yet
                    	   startService(myIntent);

                            //call to AsyncTask 
                            //LocRequest lr = new LocRequest();
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
       	public void onPostExecute(String result){
       		Log.i("JSON RESULTS", result);
       		
    	} 		
    }
    public void updateUI() {
		// TODO Auto-generated method stub
		//Read data from file and parse JSON
		JSONObject job = null;
        JSONArray recordArray, locArray = null;
        JSONObject field = null;
        
        String JSONString = DataFile.readStringFile(getBaseContext(), "tideInfo.txt", false);                           
        String tideHeight = null;
        String tideInfo = null;
        String date = null;
        String tideType = null; 
        
        try {          
            job = new JSONObject(JSONString);
            locArray = job.getJSONObject("tide").getJSONArray("tideInfo");
            recordArray = job.getJSONObject("tide").getJSONArray("tideSummary");
            
            //Log.i("recordArray",recordArray.toString());

            for(int i = 0; i < recordArray.length(); i++) {
                    //Log.i("recordArray, field",recordArray.getJSONObject(i).toString());
                    field = recordArray.getJSONObject(i);

                    tideHeight = field.getJSONObject("data").get("height").toString();
                    date = field.getJSONObject("date").get("pretty").toString();
                    tideType = field.getJSONObject("data").get("type").toString();

                    Log.i("Parsed JSON data", "On "+date+", date the tide height will be "+tideHeight
                                    +" for a tide type of "+tideType);

             for(int i1 = 0; i1 < locArray.length(); i1++) {
                     field = locArray.getJSONObject(i1);

                    tideInfo = field.getJSONObject("tideSite").get("tideSite").toString();
                    
                    //Update your display text here.
                    tidesite.setText("Location:" +tideInfo);
                    calendar.setText("Date->"+date);
                    tidepre.setText("Tide Prediction:"+tideType);
                    waveheight.setText("Swell: "+tideHeight);
                    }     
            }
    } catch (JSONException e) {
            Log.e("JSON EXCEPTION", e.toString());
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
    

}//end activity
