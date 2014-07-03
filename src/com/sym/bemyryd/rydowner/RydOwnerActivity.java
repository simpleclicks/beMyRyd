package com.sym.bemyryd.rydowner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.sym.bemyryd.R;
import com.sym.bemyryd.locationUtils.FilterWithSpaceAdapter;
import com.sym.bemyryd.locationUtils.LocationJsonParser;

import android.app.Activity;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class RydOwnerActivity extends Activity {

	AutoCompleteTextView atvPlaces_src;
	AutoCompleteTextView atvPlaces_dst;
	TextView num_passengers;
    Button button;
	PlacesTask placesTask;
	ParserTask parserTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rydowner_main);

		atvPlaces_src = (AutoCompleteTextView) findViewById(R.id.atv_places_src);
		atvPlaces_src.setThreshold(1);

		atvPlaces_dst = (AutoCompleteTextView) findViewById(R.id.atv_places_dst);
		atvPlaces_dst.setThreshold(1);
		
		num_passengers = (TextView) findViewById(R.id.num_Passengers);

		final Button button = (Button) findViewById(R.id.button);

		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Editable srcLocation = atvPlaces_src.getText();
				Editable dstLocation = atvPlaces_dst.getText();
				CharSequence numPassangers = num_passengers.getText();
				Address srcLatLongRep = null;
				Address dstLatLongRep = null;
				boolean isAddressValid = true;
				if (srcLocation == null
						|| (srcLatLongRep = getLatLongFromAddress(srcLocation
								.toString())) == null) {
					isAddressValid = false;
					atvPlaces_src.setBackgroundColor(Color
							.parseColor("#F75D59"));
				}
				if (dstLocation == null
						|| (dstLatLongRep = getLatLongFromAddress(dstLocation
								.toString())) == null) {
					isAddressValid = false;
					atvPlaces_dst.setBackgroundColor(Color
							.parseColor("#F75D59"));
				}
				if(numPassangers ==null 
						||numPassangers.toString().trim().equals("")
						|| Integer.parseInt(numPassangers.toString()) <=0 
						|| Integer.parseInt(numPassangers.toString()) >6){
					isAddressValid = false;
					num_passengers.setBackgroundColor(Color
							.parseColor("#F75D59"));
				}
				if (isAddressValid == true) {
					String jsonString = "{ " +
							"\"seats\":"+numPassangers.toString()+","
							+ "\"start\": " + "{\"lat\": "
							+ srcLatLongRep.getLatitude() + "," + "\"lon\": "
							+ srcLatLongRep.getLongitude() + "}," + "\"end\": "
							+ "{\"lat\": " + dstLatLongRep.getLatitude() + ","
							+ "\"long\": " + dstLatLongRep.getLongitude() + "}"
							+ "}";
				}

			}
		});

		atvPlaces_src.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				atvPlaces_src.setBackgroundColor(Color.parseColor("white"));
				placesTask = new PlacesTask();
				placesTask.execute(atvPlaces_src.getText().toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		atvPlaces_dst.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				atvPlaces_dst.setBackgroundColor(Color.parseColor("white"));
				placesTask = new PlacesTask();
				placesTask.execute(atvPlaces_dst.getText().toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		
		num_passengers.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				atvPlaces_dst.setBackgroundColor(Color.parseColor("white"));
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public Address getLatLongFromAddress(String addr) {
		Geocoder coder = new Geocoder(this);
		List<Address> address;
		try {
			address = coder.getFromLocationName(addr, 3);
			if (address == null) {
				return null;
			}
			Address location = address.get(0);
			location.getLatitude();
			location.getLongitude();
			return location;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	// Fetches all places from GooglePlaces AutoComplete Web Service
	private class PlacesTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... place) {
			// For storing data from web service
			String data = "";

			String key = "key=AIzaSyA8uYz7B8Ci52FBnkuRd1kM2cmTN1fk1Mk";

			String input = "";

			try {
				input = "input=" + URLEncoder.encode(place[0], "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			// place type to be searched
			String types = "types=geocode";

			// Sensor enabled
			String sensor = "sensor=true";

			// Building the parameters to the web service
			String parameters = input + "&" + types + "&" + sensor + "&" + key;

			// Output format
			String output = "json";

			// Building the url to the web service
			String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
					+ output + "?" + parameters;

			try {
				// Fetching the data from we service
				data = downloadUrl(url);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// Creating ParserTask
			parserTask = new ParserTask();

			// Starting Parsing the JSON string returned by Web Service
			parserTask.execute(result);
		}
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends
			AsyncTask<String, Integer, List<HashMap<String, String>>> {

		JSONObject jObject;

		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {

			List<HashMap<String, String>> places = null;

			LocationJsonParser placeJsonParser = new LocationJsonParser();

			try {
				jObject = new JSONObject(jsonData[0]);

				// Getting the parsed data as a List construct
				places = placeJsonParser.parse(jObject);

			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
			return places;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {

			String[] from = new String[] { "description" };
			int[] to = new int[] { android.R.id.text1 };

			// Creating a SimpleAdapter for the AutoCompleteTextView
			//FilterWithSpaceAdapter<String> adapter = new FilterWithSpaceAdapter<String>(getBaseContext(),
		    //        android.R.layout.simple_list_item_1,from);
			SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result,android.R.layout.simple_list_item_1, from, to);

			// Setting the adapter
			atvPlaces_src.setAdapter(adapter);
			atvPlaces_dst.setAdapter(adapter);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rydowner_main, menu);
		return true;
	}
}
