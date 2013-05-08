package in.nammaapp.itskannada;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	EditText name,username,password,email,ed;
	Spinner region;
	Button submit;
	HttpClient hc;
	HttpPost hp;
	HttpResponse r;
	BufferedReader br;
	String checkStr;
	SharedPreferences pref;
	String selectedRegion;
	HttpGet hg;
	String html,s="";
	ArrayList<String> usernameList;
	
	public class OnRegionSelectedListener implements OnItemSelectedListener {
    	@Override
    	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
    			long arg3) {
    		selectedRegion = arg0.getItemAtPosition(arg2).toString();
    		
    	}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
    }
	//TODO network check!!
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		hc = new DefaultHttpClient();
		pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.appPrefFIle), Context.MODE_PRIVATE);
		name = (EditText) findViewById(R.id.edtName);
		username  = (EditText) findViewById(R.id.edtUsername);
		password = (EditText) findViewById(R.id.edtPassword);
		email = (EditText) findViewById(R.id.edtEmail);
		region = (Spinner) findViewById(R.id.spnRegion);
		region.setOnItemSelectedListener(new OnRegionSelectedListener());
		submit = (Button) findViewById(R.id.btnSubmit);
		new AsyncTask<String, Void, Void>() {
			@Override
			protected Void doInBackground(String... params) {
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
											Toast.makeText(getApplicationContext(), "Waiting for Internet Connection to resume",Toast.LENGTH_LONG).show();
										}
									});
							        if(count <= 5) {
							        	doInBackground();
							        	count++;
							        }
								}
							}, 1000);
						}
					}
				}.execute();
				String xmlname;
				usernameList = new ArrayList<String>();
				hg = new HttpGet("http://nammaapp.in/scripts/usernamecheck.php?token=xtt68kjf90hs2a");
				try {
					r = hc.execute(hg);
					br = new BufferedReader(new InputStreamReader(r.getEntity().getContent(), "UTF-8"));
					while((html = br.readLine()) != null)
						s += html;
					XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			        factory.setNamespaceAware(true);
			        XmlPullParser xpp = factory.newPullParser();
			        xpp.setInput(new StringReader(s));
			        int eventType = xpp.getEventType();
			        while (eventType != XmlPullParser.END_DOCUMENT) {
			              switch (eventType) {
		                    case XmlPullParser.START_DOCUMENT:
		                    case XmlPullParser.END_DOCUMENT:
		                        break;
		                    case XmlPullParser.START_TAG:
		                        xmlname = xpp.getName();
		                        if (xmlname.equalsIgnoreCase("INFO"))
		                            usernameList.add(xpp.nextText()); 
		                        break;
			                }
			                eventType = xpp.next();
			         }
				} catch (Exception e) {
					Log.e("check for errors", "here", e);
				}
				return null;
			}
		}.execute();
		username.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					ed = (EditText) v;
					checkStr = ed.getText().toString();
					if(checkStr.length() != 0) {
						if(usernameList.contains(checkStr)) {
							Toast.makeText(getApplicationContext(), "Username Already Taken",Toast.LENGTH_LONG).show();
							if(ed.requestFocus()) {
							    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
							}
					}
				} else {
					return;
				}
			} 
		}});
		password.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				ed = (EditText) v;
				if(!hasFocus) {
					if(ed.getText().toString().length() < 6) {
						Toast toast = Toast.makeText(getApplicationContext(), "Password Should be greater than 6 characters",Toast.LENGTH_LONG);
				        toast.show();
				        new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								if(ed.requestFocus()) {
						            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
						        }
							}
						}, 1000);
					}
				} else 
					return;
			}
		});
		name.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				ed = (EditText) v;
				if(!hasFocus) {
					if(ed.getText().toString().length() == 0) {
						Toast toast = Toast.makeText(getApplicationContext(), "Please enter a name",Toast.LENGTH_LONG);
				        toast.show();
				        new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								if(ed.requestFocus()) {
						            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
						        }
							}
						}, 1000);
					}
				} else 
					return;
			}
		});
		email.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				ed = (EditText) v;
				if(!hasFocus) {
					if(ed.getText().toString().length() == 0) {
						Toast toast = Toast.makeText(getApplicationContext(), "Please Enter an Email",Toast.LENGTH_LONG);
				        toast.show();
				        new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								if(ed.requestFocus()) {
						            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
						        }
							}
						}, 500);
					}
				} else 
					return;
			}
		});
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AsyncTask<String, String, String>() {
					String resultinfo = null,xmlname,token=null,errormsg=null,userID=null;
					Exception e1;
					@Override
					protected String doInBackground(String... params) {
						hp = new HttpPost(getResources().getString(R.string.homeURL) + "/scripts/signup.php");
						ArrayList<NameValuePair> postparameters = new ArrayList<NameValuePair>();
						postparameters.add(new BasicNameValuePair("starttoken", "f45e8v23jk5x3p917q106npuwlh94v3zpige9d80"));
						postparameters.add(new BasicNameValuePair("name", name.getText().toString()));
						postparameters.add(new BasicNameValuePair("username", username.getText().toString()));
						postparameters.add(new BasicNameValuePair("password", password.getText().toString()));
						postparameters.add(new BasicNameValuePair("mail", email.getText().toString()));
						postparameters.add(new BasicNameValuePair("regionID", Integer.toString(regionIDcalc(selectedRegion))));
						int kn = 0;
						if(pref.getString("kannada", "notSet") == "true")
							kn = 1;
						else if(pref.getString("kannada", "notSet") == "false")	
							kn = 0;
						postparameters.add(new BasicNameValuePair("kannada", Integer.toString(kn)));
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
							submit.requestFocus();
						}
						return null;
					}
					protected void onPostExecute(String result) {
						if(resultinfo == "Success") {
							pref.edit().putString("name", name.getText().toString());
							pref.edit().putString("username", username.getText().toString());
							pref.edit().putString("password", password.getText().toString());
							pref.edit().putString("region", selectedRegion);
							pref.edit().putString("email", email.getText().toString());
							pref.edit().putString("token", token);
							pref.edit().putString("userID", userID);
							pref.edit().commit();
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(getApplicationContext(), "Registration Success!",Toast.LENGTH_LONG).show();
								}
							});
							
					        if(pref.getString("kannada", "notSet") == "true") {
								Intent startIntent = new Intent(RegisterActivity.this, KannadaHomeActivity.class);
								startActivity(startIntent);
							} else {
								Intent startIntent = new Intent(RegisterActivity.this, EnglishHomeActivity.class);
								startActivity(startIntent);
							}
						} else if(errormsg != null) {
							runOnUiThread(new Runnable() {
								public void run() {
									Toast toast = Toast.makeText(getApplicationContext(), "Something's Wrong! Click Submit again",Toast.LENGTH_LONG);
									toast.show();
								}
							});
					        submit.requestFocus();
						}
					};
				}.execute();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
    @Override
    public void onBackPressed() {
    	String name = pref.getString("name", "notSet");
		String username = pref.getString("username", "notSet");
		String password = pref.getString("password", "notSet");
		String email = pref.getString("email", "notSet");
		String region = pref.getString("region", "notSet");
		if(name == "notSet" || username == "notSet" || password == "notSet" || email == "notSet" || region == "notSet")		
			super.onBackPressed();
		else {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
		}
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
    
} 
