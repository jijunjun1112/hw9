package com.example.stockpartone;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.Session.StatusCallback;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.facebook.UiLifecycleHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private static final String TAG = "MainFragment";
	private UiLifecycleHelper uiHelper;
	private Button shareButton;
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	
	EditText symbol_input_edit;
	int flag=0;
	int q=1;
	String [] autoStr=null;
	
	//for image
	String stringWithHtml;
	private Spanned spannedValue;
	//json
	JSONObject json;
	String json_stock_info_str;
	//for textview
	TextView textView1;
	TextView textView2;
	TextView textView3;
	TextView textView4;
	TextView textView5;
	TextView textView6;
	TextView textView7;
	TextView textView8;
	TextView textView9;
	TextView textView10;
	TextView textView11;
	TextView textView12;
	TextView textView13;
	
	TextView textView14;
	TextView textView15;
	TextView textView16;
	TextView textView17;
	TextView textView18;
	TextView textView19;
	TextView textView20;
	TextView textView21;
	TextView textView22;
	TextView textView23;
	TextView textView24;
	TextView textView25;
	Button search_btn;
	Button news_btn;
	Button facebook_btn;
	AutoCompleteTextView textView;
	
	private MainFragment mainFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//uiHelper = new UiLifecycleHelper(this, callback);
	    //uiHelper.onCreate(savedInstanceState);
	    
		setContentView(R.layout.activity_main);
		textView1=(TextView) findViewById(R.id.textView1);
		textView2=(TextView) findViewById(R.id.textView2);
		textView3=(TextView) findViewById(R.id.textView3);
		textView4=(TextView) findViewById(R.id.textView4);
		textView5=(TextView) findViewById(R.id.textView5);
		textView6=(TextView) findViewById(R.id.textView6);
		textView7=(TextView) findViewById(R.id.textView7);
		textView8=(TextView) findViewById(R.id.textView8);
		textView9=(TextView) findViewById(R.id.textView9);
		textView10=(TextView) findViewById(R.id.textView10);
		textView11=(TextView) findViewById(R.id.textView11);
		textView12=(TextView) findViewById(R.id.textView12);
		textView13=(TextView) findViewById(R.id.textView13);
		textView14=(TextView) findViewById(R.id.textView14);
		textView15=(TextView) findViewById(R.id.textView15);
		textView16=(TextView) findViewById(R.id.textView16);
		textView17=(TextView) findViewById(R.id.textView17);
		textView18=(TextView) findViewById(R.id.textView18);
		textView19=(TextView) findViewById(R.id.textView19);
		textView20=(TextView) findViewById(R.id.textView20);
		textView21=(TextView) findViewById(R.id.textView21);
		textView22=(TextView) findViewById(R.id.textView22);
		textView23=(TextView) findViewById(R.id.textView23);
		textView24=(TextView) findViewById(R.id.textView24);
		textView25=(TextView) findViewById(R.id.textView25);
				
		search_btn=(Button) findViewById(R.id.Search);
		news_btn=(Button) findViewById(R.id.button1);
		facebook_btn=(Button) findViewById(R.id.button2);
		
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }
		autoComplete();

		search_btn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				String symbol=textView.getText().toString();
				InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);			
				getStockInfo(symbol);
			}
			
		});
		news_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getApplicationContext(),NewsActivity.class);
				intent.putExtra("result", json_stock_info_str);
				startActivity(intent);
				
			}
		});
		facebook_btn.setOnClickListener(new View.OnClickListener(){
			Session session = Session.getActiveSession();
			@Override
			public void onClick(View v) {
				if (!session.isOpened()){
					onClickLogin();
				}else {
					
					publishFeedDialog();
				}
				
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		Log.i("onActivityResult","onActivityResult");
		super.onActivityResult(arg0, arg1, arg2);
		Session.getActiveSession().onActivityResult(this, arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		Log.i("saveinstantcestate","saveinstantcestate");
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
	}

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	    	if(flag==1){
	    		publishFeedDialog();
	    		flag=0;
	    	}
	    	
	    	Log.i("sessionstatechange","sessionstatechange");
	    	if(pendingPublishReauthorization==true){
	    		Log.i(TAG,"pendingPublishReauthorization:true");
	    	}else{
	    		Log.i(TAG,"pendingPublishReauthorization:false");
	    	}
	    	if(state.equals(SessionState.OPENED_TOKEN_UPDATED)){
	    		Log.i(TAG,"SessionState.OPENED_TOKEN_UPDATED:true");
	    	}else{
	    		Log.i(TAG,"SessionState.OPENED_TOKEN_UPDATED:false");
	    	}
	    	
	    	if (pendingPublishReauthorization && 
	    	        state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
	    	    pendingPublishReauthorization = false;
	    	    //publishStory();
	    	    publishFeedDialog();
	    	}
	    	
	    	System.out.println("log in");
	        Log.i(TAG, "Logged in...");
	    } else if (state.isClosed()) {
	    	System.out.println("log out");
	        Log.i(TAG, "Logged out...");
	    }
	}
	private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
        	Session session1 = Session.getActiveSession();
        	onSessionStateChange(session, state, exception);
            if (session1.isOpened()) {
                Log.i("open","session is open");
                //publishFeedDialog();
                //publishStory();
            } else {
                Log.i("close","session is close");
               // publishFeedDialog();
            }
        }
    }
	
	private void onClickLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
        	Log.i("openforread","openforread");
        	
            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
        } else {
        	Log.i("openactivesession","openactivesession");
            Session.openActiveSession(this, true, statusCallback);
            //publishFeedDialog();
        }
    }
	private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
    }
	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	            return false;
	        }
	    }
	    return true;
	}
	private void publishStory() {
	    Session session = Session.getActiveSession();

	    if (session != null){

	        // Check for publish permissions    
	        List<String> permissions = session.getPermissions();
	        if (!isSubsetOf(PERMISSIONS, permissions)) {
	            pendingPublishReauthorization = true;
	            Session.NewPermissionsRequest newPermissionsRequest = new Session
	                    .NewPermissionsRequest(this, PERMISSIONS);
	        session.requestNewPublishPermissions(newPermissionsRequest);
	            return;
	        }

	        Bundle postParams = new Bundle();
	        postParams.putString("name", "Facebook SDK for Android");
	        postParams.putString("caption", "Build great social apps and get more installs.");
	        postParams.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
	        postParams.putString("link", "https://developers.facebook.com/android");
	        postParams.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

	        Request.Callback callback= new Request.Callback() {
	            public void onCompleted(Response response) {
	                JSONObject graphResponse = response
	                                           .getGraphObject()
	                                           .getInnerJSONObject();
	                String postId = null;
	                try {
	                    postId = graphResponse.getString("id");
	                } catch (JSONException e) {
	                    Log.i(TAG,
	                        "JSON error "+ e.getMessage());
	                }
	                FacebookRequestError error = response.getError();
	                if (error != null) {
	                    Toast.makeText(getApplicationContext(),
	                         error.getErrorMessage(),
	                         Toast.LENGTH_SHORT).show();
	                    } else {
	                        Toast.makeText(getApplicationContext(), 
	                             postId,
	                             Toast.LENGTH_LONG).show();
	                }
	            }
	        };

	        Request request = new Request(session, "me/feed", postParams, 
	                              HttpMethod.POST, callback);

	        RequestAsyncTask task = new RequestAsyncTask(request);
	        task.execute();
	    }

	}
	private void publishFeedDialog() {
		Log.i("publishFeedDialog","publishFeedDialog");
	    Bundle params = new Bundle();
	    try {
			params.putString("name", json.getJSONObject("result").getString("Name"));
			params.putString("caption", "Stock information of "+json.getJSONObject("result").getString("Name")
					+"("+json.getJSONObject("result").getString("Symbol")+")");
		    params.putString("description", "Last Trade Price: "+json.getJSONObject("result").getJSONObject("Quote").getString("LastTradePriceOnly")
		    		+" Change:"+json.getJSONObject("result").getJSONObject("Quote").getString("Change")
		    		+"("+json.getJSONObject("result").getJSONObject("Quote").getString("ChangeInPercent")+")");
		    
		    params.putString("link", "http://finance.yahoo.com/q?s="+textView.getText().toString());
		    params.putString("picture", json.getJSONObject("result").getString("StockChartImageURL"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    

	    
	    WebDialog feedDialog = (
	        new WebDialog.FeedDialogBuilder(MainActivity.this,
	            Session.getActiveSession(),
	            params))
	        .setOnCompleteListener(new OnCompleteListener() {

				@Override
				public void onComplete(Bundle values, FacebookException error) {
					if (error == null) {
	                    // When the story is posted, echo the success
	                    // and the post Id.
	                    final String postId = values.getString("post_id");
	                    if (postId != null) {
	                        Toast.makeText(getApplicationContext(),
	                            "Posted Successful: "+postId,
	                            Toast.LENGTH_SHORT).show();
	                    } else {
	                        // User clicked the Cancel button
	                        Toast.makeText(getApplicationContext(), 
	                            "Publish cancelled", 
	                            Toast.LENGTH_SHORT).show();
	                    }
	                } else if (error instanceof FacebookOperationCanceledException) {
	                    // User clicked the "x" button
	                    Toast.makeText(getApplicationContext(), 
	                        "Publish cancelled", 
	                        Toast.LENGTH_SHORT).show();
	                } else {
	                    // Generic, ex: network error
	                    Toast.makeText(getApplicationContext(), 
	                        "Error posting story", 
	                        Toast.LENGTH_SHORT).show();
	                }
					
				}

	        })
	        .build();
	    feedDialog.show();
	}
	@Override
    public void onStart() {
		Log.i("start","start");
        super.onStart();
        Session.getActiveSession().addCallback(statusCallback);
    }

    @Override
    public void onStop() {
    	Log.i("stop","stop");
        super.onStop();
        Session.getActiveSession().removeCallback(statusCallback);
    }
	TextWatcher tw1=null;
	public void autoComplete(){
		textView=(AutoCompleteTextView) findViewById(R.id.symbol_input);
		
		textView.addTextChangedListener(tw1=new TextWatcher(){
			String symbol = null;
			int flag=0;
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				Log.i("before","before");

			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				Log.i("flag",flag+"");
				if(textView.isPerformingCompletion()){
					
					return;
				}
				autoStr=getAutoCompleteData();
				ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.textview,autoStr);
				textView.setAdapter(adapter);
				textView.setTextColor(Color.BLACK);
				textView.setThreshold(1);

				textView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						
						symbol=autoStr[position].substring(0, autoStr[position].indexOf(","));
						textView.setText(symbol);
						InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);			
						getStockInfo(symbol);
						
						
					}
				});
				
			}
			@Override
			public void afterTextChanged(Editable s) {
				Log.i("after","after");
				//textView.dismissDropDown();						
			}				
		});
	}
	public void getStockInfo(String symbol){
		//http://cs-server.usc.edu:36294/examples/servlet/finance_servlet?symbol=GOOG
		String urlString="http://cs-server.usc.edu:36294/examples/servlet/finance_servlet?symbol="+symbol;
		try {
			URL url=new URL(urlString);
			URLConnection urlConnection=url.openConnection();
			urlConnection.setAllowUserInteraction(false);
			InputStream inStream=url.openStream();
			ByteArrayOutputStream outStream=new ByteArrayOutputStream();
			byte[] buffer=new byte[1024];
			int len=0;
			while((len=inStream.read(buffer))!=-1){
				outStream.write(buffer, 0, len);
			}
			inStream.close();
			json_stock_info_str=outStream.toString();
			json=new JSONObject(json_stock_info_str);
			JSONObject Quote=json.getJSONObject("result").getJSONObject("Quote");	
			
			textView1.setVisibility(View.VISIBLE);
			//"Symbol":"GOOG","Name":"Google Inc."
			
			if(Quote.getString("Change")==""){
				textView1.setText("Stock Information Not Available");
				return;
			}else if(json.getJSONObject("result").getString("Name")!=""&&json.getJSONObject("result").getString("Symbol")!=""){
				textView1.setText(json.getJSONObject("result").getString("Name")+"("+json.getJSONObject("result").getString("Symbol")+")");
			}
			
			
			formatData(textView2,Quote,"LastTradePriceOnly");	
			
			if(Quote.getString("ChangeType")=="-"){
				textView3.setTextColor(Color.RED);
			}else{
				textView3.setTextColor(Color.GREEN);
			}
			textView3.setVisibility(View.VISIBLE);
			if(Quote.getString("Change")!=""&&Quote.getString("ChangeInPercent")!=""){
				textView3.setText(Quote.getString("Change")+"("+Quote.getString("ChangeInPercent")+")");
			}else{
				textView3.setText("0.00"+"("+"0.00"+")");
			}
		
			textView4.setVisibility(View.VISIBLE);
			textView5.setVisibility(View.VISIBLE);
			textView6.setVisibility(View.VISIBLE);
			textView7.setVisibility(View.VISIBLE);
			textView8.setVisibility(View.VISIBLE);
			textView9.setVisibility(View.VISIBLE);
			textView10.setVisibility(View.VISIBLE);
			textView11.setVisibility(View.VISIBLE);
			textView12.setVisibility(View.VISIBLE);
			textView13.setVisibility(View.VISIBLE);
			
			formatData(textView14,Quote,"PreviousClose");
			formatData(textView15,Quote,"Open");
			formatData(textView16,Quote,"Bid");
			formatData(textView17,Quote,"Ask");
			formatData(textView18,Quote,"OneYearTargetPrice");
			formatData(textView19,Quote,"DaysLow","DaysHigh");
			formatData(textView20,Quote,"YearLow","YearHigh");
			formatData(textView21,Quote,"Volume");
			formatData(textView22,Quote,"AverageDailyVolume");
			textView23.setVisibility(View.VISIBLE);
			textView23.setText(Quote.getString("MarketCapitalization"));
			getImage(Quote.getString("ChangeType"));
			
			news_btn.setVisibility(View.VISIBLE);
			facebook_btn.setVisibility(View.VISIBLE);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
	
	public void getImage(String changeType) throws JSONException{
		//<img src='http://www-scf.usc.edu/~csci571/2014Spring/hw6/up_g.gif' class='arrow_img'>
		//<img src='http://www-scf.usc.edu/~csci571/2014Spring/hw6/down_r.gif' class='arrow_img'>
		//Drawable drawable=new Html.ImageGetter.getDrawable("http://www-scf.usc.edu/~csci571/2014Spring/hw6/down_r.gif");
		if(changeType=="-"){
			stringWithHtml = "&nbsp;&nbsp;<img src=\"http://www-scf.usc.edu/~csci571/2014Spring/hw6/down_r.gif\"/>&nbsp;&nbsp;";
			
		}else{
			stringWithHtml = "<img src=\"http://www-scf.usc.edu/~csci571/2014Spring/hw6/up_g.gif\"/>";
		}
		spannedValue = Html.fromHtml(stringWithHtml,getImageHTML("arrow_image"),null);
		textView24.setVisibility(View.VISIBLE);
		textView24.setText(spannedValue);
		
		//"StockChartImageURL":"http:\/\/chart.finance.yahoo.com\/t?s=a&lang=enUS&amp;width=300&height=180"
		stringWithHtml="<img src=\""+json.getJSONObject("result").getString("StockChartImageURL")+"\">";
		spannedValue = Html.fromHtml(stringWithHtml,getImageHTML("stock_image"),null);
		textView25.setVisibility(View.VISIBLE);
		textView25.setText(spannedValue);
		
		
	}
	private ImageGetter  getImageHTML(String a_image) {
		final String image=a_image;
		ImageGetter imageGetter = new ImageGetter() {
			public Drawable getDrawable(String source) {
				try {
					Drawable drawable = Drawable.createFromStream(new URL(source).openStream(), "src name");
					//drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
					if(image=="arrow_image"){
						drawable.setBounds(0, 0, 30,30);
					}else if(image=="stock_image"){
						Display display=getWindowManager().getDefaultDisplay();
						drawable.setBounds(0, 0,  (int)Math.floor(display.getWidth()*0.8),(int)Math.floor(display.getHeight()*0.3));
					}
					
					return drawable;
				} catch(IOException exception) {
					Log.v("IOException",exception.getMessage());
					return null;
				}
			}
		};
		return imageGetter;
	}
	public void formatData(TextView textView, JSONObject Quote, String string) throws JSONException {
		
		textView.setVisibility(View.VISIBLE);
		
		StringBuffer sb=new StringBuffer(Quote.getString(string));
		Log.i("quote1111",sb.toString());
		while(sb.indexOf(",")!=-1){
			sb.deleteCharAt(sb.indexOf(","));				
		}
		Log.i("quote1111",sb.toString());
		
		if(Float.parseFloat(sb.toString())!=0){
			textView.setText(Quote.getString(string));
		}else{
			textView.setText("0");
		}
		
		
	}
	public void formatData(TextView textView, JSONObject Quote, String string1,String string2) throws JSONException {
		
		textView.setVisibility(View.VISIBLE);
		
		StringBuffer sb1=new StringBuffer(Quote.getString(string1));
		while(sb1.indexOf(",")!=-1){
			sb1.deleteCharAt(sb1.indexOf(","));				
		}
		
		StringBuffer sb2=new StringBuffer(Quote.getString(string2));			
		while(sb2.indexOf(",")!=-1){
			sb2.deleteCharAt(sb1.indexOf(","));				
		}
		
		if(Float.parseFloat(sb1.toString())!=0&&Float.parseFloat(sb2.toString())!=0){
			textView.setText(Quote.getString(string1)+"-"+Quote.getString(string2));
		}else{
			textView.setText("0"+"-"+"0");
		}
			
		
		
	}
	public String[] getAutoCompleteData(){

		symbol_input_edit=(EditText) findViewById(R.id.symbol_input);
		String symbolStr=symbol_input_edit.getText().toString();
		Log.i("symbolStr",symbolStr);		
		String urlString="http://autoc.finance.yahoo.com/autoc?query="+symbolStr+"&callback=YAHOO.Finance.SymbolSuggest.ssCallback";
		List<String> autoStrList=null;
		try {
			URL url=new URL(urlString);
	
			URLConnection urlConnection=url.openConnection();
			urlConnection.setAllowUserInteraction(false);
			InputStream inStream=url.openStream();
			ByteArrayOutputStream outStream=new ByteArrayOutputStream();
			byte[] buffer=new byte[1024];
			int len=0;
			while((len=inStream.read(buffer))!=-1){
				outStream.write(buffer, 0, len);
			}
			inStream.close();
			String autoCompleteStr=outStream.toString();
			String json_str=autoCompleteStr.substring(autoCompleteStr.indexOf("(")+1, autoCompleteStr.length()-1);
			//Log.i("outStream",json_str);
			JSONObject json=new JSONObject(json_str);
			JSONArray result=json.getJSONObject("ResultSet").getJSONArray("Result");
			//String [] oneAutoCompleteStr=new String[]{};
			autoStrList=new ArrayList<String>();
			for(int i=0;i<result.length();i++){
				String OneResult=(String) result.optJSONObject(i).get("symbol")+", "+result.optJSONObject(i).get("name")+"("+result.optJSONObject(i).get("exch")+")";
				//Log.i("outStream",OneResult);
				autoStrList.add(OneResult);
			}
						
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String[] autoStr=autoStrList.toArray(new String[autoStrList.size()]);
		return autoStr;
	}	
}

