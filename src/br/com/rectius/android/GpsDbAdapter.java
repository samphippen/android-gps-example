package br.com.rectius.android;

import java.text.SimpleDateFormat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GpsDbAdapter {

	public static final String COL_ID 				= "_id";
	public static final String COL_EMISSOR 			= "emissor";
	public static final String COL_DATA_INFORMACAO 	= "data_informacao";
	public static final String COL_LATITUDE 		= "latitude";
	public static final String COL_LONGITUDE 		= "longitude";
	public static final String COL_QUALIDADE 		= "qualidade";
	public static final String COL_FONTE 			= "fonte";
	public static final String COL_TRANSMITIDO 		= "transmitido";

	
	private static final String TAG = "GpsDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;


	private static final String DATABASE_CREATE = 
		"create table gps (_id integer primary key autoincrement, " +
		"emissor text not null, data_informacao string not null," +
		"latitude text not null, longitude text not null," +
		"qualidade text not null, fonte text not null, transmitido text not null);";
	
	private static final String DATABASE_NAME = "applicationdata";
	private static final String DATABASE_TABLE = "gps";
	private static final int DATABASE_VERSION = 1;


	private final Context mCtx;
	
	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Atualizando banco de dados  da versão " + oldVersion + " para a " + newVersion);
			db.execSQL("DROP TABLE IF EXISTS gps");
			onCreate(db);
		}

	}
	
	public GpsDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}
	
	public GpsDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		mDbHelper.close();
	}
	
	/**
	 * 
	 * @param emissor
	 * @param latitude
	 * @param longitude
	 * @return rowId ou -1 se ocorrer algum erro
	 */
	public long create(String emissor, String latitude, String longitude, String qualidade, String fonte) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(COL_EMISSOR, emissor);
		initialValues.put(COL_DATA_INFORMACAO, getCurrentDate());
		initialValues.put(COL_LATITUDE, latitude);
		initialValues.put(COL_LONGITUDE, longitude);
		initialValues.put(COL_QUALIDADE, qualidade);
		initialValues.put(COL_FONTE, fonte);
		initialValues.put(COL_TRANSMITIDO, "N");
		
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}
	
	public boolean delete() {		
		return mDb.delete(DATABASE_TABLE, null, null) > 0;
	}
	
	public boolean delete(long rowId) {		
		return mDb.delete(DATABASE_TABLE, COL_ID + "=" + rowId, null) > 0;
	}
	
	public Cursor listAll() {
		return mDb.query(DATABASE_TABLE, new String[] {COL_ID, COL_EMISSOR, COL_DATA_INFORMACAO, 
				COL_LATITUDE, COL_LONGITUDE, COL_QUALIDADE, COL_TRANSMITIDO, COL_FONTE}, null, null, null, null, null);
	}
	
	public Cursor listById(long rowId) {
		Cursor mCursor = 
			mDb.query(DATABASE_TABLE, new String[] {COL_ID, COL_EMISSOR, COL_DATA_INFORMACAO, 
					COL_LATITUDE, COL_LONGITUDE, COL_QUALIDADE, COL_TRANSMITIDO, COL_FONTE}, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public boolean update(long rowId, String transmitido) {
		ContentValues args = new ContentValues();
		args.put(COL_TRANSMITIDO, transmitido);
		
		return mDb.update(DATABASE_TABLE, args, COL_ID + "=" + rowId, null) > 0;
	}
	
	private String getCurrentDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		java.util.Date date = new java.util.Date();
		return dateFormat.format(date);

	}

}
