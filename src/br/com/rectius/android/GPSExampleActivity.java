package br.com.rectius.android;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.SimpleCursorAdapter;

public class GPSExampleActivity extends ListActivity {
	
	private GpsDbAdapter mDbHelper;
	private Cursor cursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.getListView().setDividerHeight(2);
		
		mDbHelper = new GpsDbAdapter(this);
        mDbHelper.open();
        mDbHelper.delete();
        registerForContextMenu(getListView());
        
     // Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				String accurancy = location.getAccuracy() == 1 ? "Bom" : "Ruim";
				Localizacao l = new Localizacao();
				l.setLatitude(location.getLatitude());
				l.setLongitude(location.getLongitude());
				l.setAccurancy(accurancy);
				l.setFonte(location.getProvider());
				l.setTime(new Time());
				
				create(l);
				fill();
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {}

			public void onProviderEnabled(String provider) {}

			public void onProviderDisabled(String provider) {}
		};

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 10, locationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10, locationListener);
        
	}
	
	
	public void fill() {
		cursor = mDbHelper.listAll();
		startManagingCursor(cursor);
		
		String[] from = new String[] { 
				GpsDbAdapter.COL_DATA_INFORMACAO, 
				GpsDbAdapter.COL_LATITUDE, 
				GpsDbAdapter.COL_LONGITUDE,
				GpsDbAdapter.COL_FONTE,
				GpsDbAdapter.COL_QUALIDADE};
		int[] to = new int[] { R.id.label, R.id.lblLatitude, R.id.lblLongitude, R.id.lblFonte, R.id.lblQualidade };
		
		// Now create an array adapter and set it to display using our row
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.gps_row, cursor, from, to);
		setListAdapter(notes);
	}
	
	public void create(Localizacao localizacao) {
		mDbHelper.create("pablo001", 
				localizacao.getLatitude().toString(), 
				localizacao.getLongitude().toString(),
				localizacao.getAccurancy(),
				localizacao.getFonte());
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mDbHelper != null) {
			mDbHelper.close();
		}
	}
	
}

