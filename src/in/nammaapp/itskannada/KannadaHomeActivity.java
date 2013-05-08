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

public class KannadaHomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kannada_home_activity);
		
		Button btnResPhrase = (Button) findViewById(R.id.btnResPhrase);
		Button btnPrnWord = (Button) findViewById(R.id.btnPrnWord);
		Button btnResWord = (Button) findViewById(R.id.btnResWord);
		Button btnAnsQuestion = (Button) findViewById(R.id.btnAnsQuestion);
		Button btnEval = (Button) findViewById(R.id.btnEval);
		Button btnKanQuestion = (Button) findViewById(R.id.btnKanQuestion);
		Button btnkanForum = (Button) findViewById(R.id.btnkanForum);
		Button btnKanNotif = (Button) findViewById(R.id.btnKanNotif);
		Button btnKanBadge = (Button) findViewById(R.id.btnKanBadge);
		
		btnResPhrase.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(KannadaHomeActivity.this, PhraseActivity.class);
				startActivity(i);
			}
		});
		
		btnPrnWord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(KannadaHomeActivity.this, PronunciationActivity.class);
				startActivity(i);
			}
		});
		
		btnResWord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(KannadaHomeActivity.this, WordContextResActivity.class);
				startActivity(i);
			}
		});
		
		btnAnsQuestion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(KannadaHomeActivity.this, ForumActivity.class);
				Bundle b = new Bundle();
				b.putString("ACTION", "ANSWER");
				i.putExtras(b);
				startActivity(i);
			}
		});
		SharedPreferences pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.appPrefFIle), Context.MODE_PRIVATE);
		int xp = Integer.parseInt(pref.getString("xp", "-1"));
		if(xp == -1 || xp < 200) {
			btnEval.setEnabled(false);
			btnEval.setText(btnEval.getText().toString() + " (Needs 200 XP)");
		} else {
			btnEval.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(KannadaHomeActivity.this, EvalActivity.class);
					startActivity(i);
				}
			});
		}
		
		btnKanQuestion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(KannadaHomeActivity.this, QuestionActivity.class);
				startActivity(i);
			}
		});
		
		btnkanForum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(KannadaHomeActivity.this, ForumActivity.class);
				startActivity(i);
			}
		});
		
		btnKanNotif.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(KannadaHomeActivity.this, NotificationsActivity.class);
				startActivity(i);
			}
		});
		
		btnKanBadge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(KannadaHomeActivity.this, PointsActivity.class);
				startActivity(i);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kannada_home, menu);
		return true;
	}

}
