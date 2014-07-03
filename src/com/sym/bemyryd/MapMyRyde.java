package com.sym.bemyryd;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class MapMyRyde extends Activity {
	static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	  static final LatLng KIEL = new LatLng(53.551, 9.993);
	MapView mv;
	GoogleMap gMap;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_my_ryde);
		initializeMap();
	}
	
	private void initializeMap() {
		//getShaKey();
		if(gMap==null){
			gMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
			gMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(new Double(37.4013970),new Double(-122.0520890)), 17, 0, 0)));
			gMap.addMarker(new MarkerOptions()
	        .position(new LatLng(37.4013970, -122.0520890))
	        .title("Hello world"));
			try {
			    JSONObject json = new JSONObject("{rows:[{'seats':4.0,'_id':'53b4be02cbe6fbab0a019623','startpoint':{'lat':-114.11773681640625,'lon':51.10682735591432},'email':'sheetalparanjpe@gmail.com','endpoint':{'lat':-113.98040771484375,'lon':50.93939251390387}},{'seats':4.0,'_id':'53b4be09cbe6fbab0a019624','startpoint':{'lat':-114.11773681640625,'lon':51.10682735591432},'email':'sheetalparanjpe@gmail.com','endpoint':{'lat':-113.98040771484375,'lon':50.93939251390387}},{'seats':2.0,'_id':'53b4be31cbe6fbab0a019625','startpoint':{'lat':-118.11773681640625,'lon':55.09144802136697},'email':'martin4127@gmail.com','endpoint':{'lat':-114.98040771484375,'lon':51.93939251390387}},{'seats':2.0,'_id':'53b4be44cbe6fbab0a019626','startpoint':{'lat':-128.11773681640625,'lon':52.09144802136697},'email':'nivedita.sancheti@gmail.com','endpoint':{'lat':-114.98040771484375,'lon':51.93939251390387}},{'startpoint':{'lat':-128.11773681640625,'lon':52.09144802136697},'_id':'53b4dc3ab3b1b8269a709ce2','endpoint':{'lat':-114.98040771484375,'lon':51.93939251390387},'email':'nivedita.sanchetiii@gmail.com','seats':2}]}");
			    JSONArray nearby= json.getJSONArray("rows");
			    Log.d("leaders",nearby.toString());
			    for(int i=0;i<nearby.length(); i++){
			        JSONObject jsonas = nearby.getJSONObject(i);
			        //Log.d("First Names",jsonas.getString("email"));
			        JSONObject pos = jsonas.getJSONObject("endpoint");
			        Double lat = pos.getDouble("lat");
			        Double longi = pos.getDouble("lon");
			        gMap.addMarker(new MarkerOptions()
			        .position(new LatLng(lat, longi))
			        .title("Contact : " + jsonas.getString("email") + " only " + jsonas.getString("seats") + " left!"));
			    }
			} catch (JSONException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
		}
		
	}
	
	private void getShaKey() {

		 try {
		 PackageInfo info = getPackageManager().getPackageInfo("com.sym.bemyryd",
		 PackageManager.GET_SIGNATURES);
		 for (Signature signature : info.signatures) {
		 MessageDigest md = MessageDigest.getInstance("SHA");
		 md.update(signature.toByteArray());
		 Log.v("Warning", "KeyHash:" + Base64.encodeToString(md.digest(),
		 Base64.DEFAULT));
		 }
		 } catch (NameNotFoundException e) {
		 e.printStackTrace();

		 } catch (NoSuchAlgorithmException e) {
		 e.printStackTrace();

		 }

		 }
	
}
