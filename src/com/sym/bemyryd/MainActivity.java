package com.sym.bemyryd;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.Person.Image;
import com.sym.bemyryd.betherydr.BetherydrActivity;
import com.sym.bemyryd.rydowner.RydOwnerActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	  
	  Button bemyryd;
	  Button bedryd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final Context cont = this;
		bemyryd = (Button) findViewById(R.id.button1);
		bemyryd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(cont, BetherydrActivity.class);
				startActivity(i);
			}
		});
		
		bedryd = (Button) findViewById(R.id.button2);
		bedryd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(cont, RydOwnerActivity.class);
				startActivity(i);
			}
		});
		
	}
	
	
	
	protected void onStart() {
	    super.onStart();
	  }

	  protected void onStop() {
	    super.onStop();
	  }


}
