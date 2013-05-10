package in.nammaapp.itskannada;

import in.nammaapp.itskannada.RegisterActivity.OnRegionSelectedListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class QuestionActivity extends Activity {

	private EditText questxt;
	private Spinner regionspin;
	private Button sbtques;
	private EditText kanword;
	private EditText engword;
	private String selectedRegion;
	SharedPreferences pref;
	HttpClient hc;
	HttpPost hp;
	HttpResponse r;
	HttpGet hg;
	String uid="",rid="",token="",qid="",ques="",eng="",kan="";
	BufferedReader br;
	
	public class OnRegionSelectedListener implements OnItemSelectedListener {
    	@Override
    	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
    			long arg3) {
    		selectedRegion = arg0.getItemAtPosition(arg2).toString();
    		
    	}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_activity);
		pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.appPrefFIle), Context.MODE_PRIVATE);
		questxt = (EditText) findViewById(R.id.questxt);
		kanword = (EditText) findViewById(R.id.kan);
		engword = (EditText) findViewById(R.id.eng);
		regionspin=(Spinner)findViewById(R.id.spnRegion);
		sbtques = (Button) findViewById(R.id.sbtques);
		regionspin.setOnItemSelectedListener(new OnRegionSelectedListener());
		uid=pref.getString(uid, "testuid");
		hc = new DefaultHttpClient();
		token=pref.getString(token, "jhdcgdvcayugseah87s5sa");
		sbtques.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(questxt.getText().toString().length()==0)
					Toast.makeText(getApplicationContext(), "Please enter a question",Toast.LENGTH_LONG).show();
				else
				{
					ques=questxt.getText().toString();
					eng=engword.getText().toString();
					kan=kanword.getText().toString();
					rid=Integer.toString(regionIDcalc(selectedRegion));
					new AsyncTask<String, String, String>() {
						String resultinfo = null,xmlname,token=null,errormsg=null,userID=null;
						Exception e1;
						@Override
						protected String doInBackground(String... params) {
							hp = new HttpPost(getResources().getString(R.string.homeURL) + "/scripts/addquestion.php");
							ArrayList<NameValuePair> postparameters = new ArrayList<NameValuePair>();
							//postparameters.add(new BasicNameValuePair("starttoken", "f45e8v23jk5x3p917q106npuwlh94v3zpige9d80"));
							postparameters.add(new BasicNameValuePair("question", ques));
							postparameters.add(new BasicNameValuePair("U_ID",uid) );
							postparameters.add(new BasicNameValuePair("R_ID",rid));
							postparameters.add(new BasicNameValuePair("engword",eng));
							postparameters.add(new BasicNameValuePair("kanword",kan));
							try {
								hp.setEntity(new UrlEncodedFormEntity(postparameters));
								new AsyncTask<String,String,Boolean>() {
									int count = 0;
									@Override
									protected Boolean doInBackground(String... params) {
										return hasActiveInternetConnection();
									}
									
									@Override
									protected void onPostExecute(Boolean result) {
										super.onPostExecute(result);
										if(result == false) {
											new Handler().postDelayed(new Runnable() {
												
												@Override
												public void run() {
													runOnUiThread(new Runnable() {
														public void run() {
															Toast toast = Toast.makeText(getApplicationContext(), "Waiting for Internet Connection to resume",Toast.LENGTH_LONG);
													        toast.show();
														}
													});
													if(count <= 10) {
											        	doInBackground();
											        	count++;
											        }
												}
											}, 1000);
										}
									}
								}.execute();
								r = hc.execute(hp);
								br = new BufferedReader(new InputStreamReader(r.getEntity().getContent(),"UTF-8"));
								String s = "";
								String html;
								while((html = br.readLine())!= null)
									s += html;
								XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
						        factory.setNamespaceAware(true);
						        XmlPullParser xpp = factory.newPullParser();
						        xpp.setInput(new StringReader(s));
						        int eventType = xpp.getEventType();
						        while (eventType != XmlPullParser.END_DOCUMENT) {
						        	 resultinfo = null;
						              switch (eventType) {
					                    case XmlPullParser.START_DOCUMENT:
					                    case XmlPullParser.END_DOCUMENT:
					                        break;
					                    case XmlPullParser.START_TAG:
					                        xmlname = xpp.getName();
					                        if (xmlname.equalsIgnoreCase("INFO"))
					                            resultinfo = xpp.nextText();
					                        else if(xmlname.equalsIgnoreCase("TOKEN"))
					                        	token = xpp.nextText();
					                        else if(xmlname.equalsIgnoreCase("MESSAGE"))
					                        	errormsg = xpp.nextText();
					                        else if(xmlname.equalsIgnoreCase("USERID"))
					                        	userID = xpp.nextText();
					                        break;
						                }
						                eventType = xpp.next();
						        }
						        
							} catch(Exception e) {
								e1 = e;
								runOnUiThread(new Runnable() {
									public void run() {
										Toast toast = Toast.makeText(getApplicationContext(), "Swalpa Error, Adjust Maadi!",Toast.LENGTH_LONG);
										toast.show();
										Log.e("warning", "checkhere", e1);
									}
								});
								sbtques.requestFocus();
							}
							return null;
						}
						protected void onPostExecute(String result) {
							if(resultinfo == "Success") {
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast.makeText(getApplicationContext(), "Question Posted Successfully!",Toast.LENGTH_LONG).show();
									}
								});
							
						       
							}
						};
					}.execute();
				
				}
			}
		});
		
		
	}
	 private int regionIDcalc(String region) {
	    	ArrayList<String> ar = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.region_arrays)));
	    	return ar.lastIndexOf(selectedRegion);
	    }
	private boolean hasActiveInternetConnection()
	  {
			            try
			            {
			                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
			                urlc.setRequestProperty("User-Agent", "Test");
			                urlc.setRequestProperty("Connection", "close");
			                urlc.setConnectTimeout(4000);
			                urlc.setReadTimeout(4000);
			                urlc.connect();
			                return (urlc.getResponseCode() == 200);
			            } catch (IOException e)
			            {
			                Log.e("warning", "Error checking internet connection", e);
			                return false;
			            }

	  } 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question, menu);
		return true;
	}

}
