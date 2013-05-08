package in.nammaapp.itskannada;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SignUpActivity extends Activity {
	Button btnReg,btnDict,btnPhrase;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up_activity);
		//TODO sync tables here
		btnReg = (Button) findViewById(R.id.btnSignUp);
		btnDict = (Button) findViewById(R.id.btnDict);
		btnPhrase = (Button) findViewById(R.id.btnPhrase);
		btnReg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SignUpActivity.this, RegisterActivity.class);
				startActivity(i);
			}
		});
		btnDict.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SignUpActivity.this, DictionarySearchActivity.class);
				startActivity(i);
			}
		});
		btnPhrase.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SignUpActivity.this, PhraseSearchActivity.class);
				startActivity(i);
			}
		});		
	}
	
	@Override
	public void onBackPressed() {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.appPrefFIle), Context.MODE_PRIVATE);
		String prefcheck = pref.getString("kannada", "notSet");
		if(prefcheck != "notSet") {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

}
