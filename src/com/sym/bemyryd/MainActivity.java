package com.sym.bemyryd;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.Person.Image;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity implements OnConnectionFailedListener, ConnectionCallbacks, com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks, com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener {
	
	/* Request code used to invoke sign in user interactions. */
	  private static final int RC_SIGN_IN = 0;

	  /* Client used to interact with Google APIs. */
	  private GoogleApiClient mGoogleApiClient;

	  /* A flag indicating that a PendingIntent is in progress and prevents
	   * us from starting further intents.
	   */
	  private boolean mIntentInProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try{
		mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(Plus.API)
        .addScope(Plus.SCOPE_PLUS_LOGIN)
        .build();
		}
		catch(Exception ex){
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
	}
	
	protected void onStart() {
	    super.onStart();
	    mGoogleApiClient.connect();
	    try{
	    	if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
			    Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
			    String personName = currentPerson.getDisplayName();
			    
			    AlertDialog alertDialog = new AlertDialog.Builder(
	                    MainActivity.this).create();

	    // Setting Dialog Title
	    alertDialog.setTitle("Alert Dialog");

	    // Setting Dialog Message
	    alertDialog.setMessage(personName);
			    System.out.println(personName);
			    Image personPhoto = currentPerson.getImage();
			    String personGooglePlusProfile = currentPerson.getUrl();
			  }
	    }
	    catch(Exception e){
	    	System.out.println(e.getMessage());
	    }
	    
	  }

	  protected void onStop() {
	    super.onStop();

	    if (mGoogleApiClient.isConnected()) {
	      mGoogleApiClient.disconnect();
	    }
	  }

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
		    Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
		    String personName = currentPerson.getDisplayName();
		    
		    AlertDialog alertDialog = new AlertDialog.Builder(
                    MainActivity.this).create();

    // Setting Dialog Title
    alertDialog.setTitle("Alert Dialog");

    // Setting Dialog Message
    alertDialog.setMessage(personName);
		    System.out.println(personName);
		    Image personPhoto = currentPerson.getImage();
		    String personGooglePlusProfile = currentPerson.getUrl();
		  }
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		
	}
}
