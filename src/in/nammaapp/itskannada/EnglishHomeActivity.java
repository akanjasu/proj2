package in.nammaapp.itskannada;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class EnglishHomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.english_home_activity);
		Button btnPhrase = (Button) findViewById(R.id.btnPhrase);
		Button btnSearchForum = (Button) findViewById(R.id.btnSearchForum);
		Button btnWord = (Button) findViewById(R.id.btnWord);
		Button btnDictSearch = (Button) findViewById(R.id.btnDictSearch);
		Button btnQuestion = (Button) findViewById(R.id.btnQuestion);
		Button btnForum = (Button) findViewById(R.id.btnForum);
		Button btnPhraseRequest = (Button) findViewById(R.id.btnPhraseRequest);
		Button btnWordContext = (Button) findViewById(R.id.btnWordContext);
		Button btnEngNotif = (Button) findViewById(R.id.btnEngNotif);
		Button btnEngBadge = (Button) findViewById(R.id.btnEngBadge);
		
		//Onclick listeners for all the buttons
		btnPhrase.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(EnglishHomeActivity.this, RequestPhraseTranslateActivity.class);
				startActivity(i);
			}
		});
		
		btnSearchForum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(EnglishHomeActivity.this, SearchForumActivity.class);
				startActivity(i);
			}
		});
		
		btnWord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(EnglishHomeActivity.this, LearningModuleActivity.class);
				startActivity(i);
			}
		});
		
		btnDictSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(EnglishHomeActivity.this, DictSearchActivity.class);
				startActivity(i);
			}
		});
		
		btnQuestion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(EnglishHomeActivity.this, QuestionActivity.class);
				startActivity(i);
			}
		});
		
		btnForum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(EnglishHomeActivity.this, ForumActivity.class);
				startActivity(i);
			}
		});
		
		btnPhraseRequest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(EnglishHomeActivity.this, PhraseActivity.class);
				startActivity(i);
			}
		});
		
		btnWordContext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(EnglishHomeActivity.this, WordContextResActivity.class);
				startActivity(i);
			}
		});
		
		btnEngNotif.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(EnglishHomeActivity.this, NotificationsActivity.class);
				startActivity(i);
			}
		});
		
		btnEngBadge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(EnglishHomeActivity.this, PointsActivity.class);
				startActivity(i);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.english_home, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}

}
