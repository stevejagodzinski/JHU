package jagodzinski.steve.hw3.contact;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class ContactContentProvider extends ContentProvider {

	public static final int DB_VERSION = 1;
	
	// Table Constants
	private static final String DATABASE_FILE_NAME = "contacts_database";
	private static final String CONTACTS_TABLE = "contacts";
	
	public static final String ID = "_id";
	public static final String DISPLAY_NAME = "display_name";
	public static final String FIRST_NAME = "first_name";
	public static final String LAST_NAME = "last_name";
	public static final String BIRTHDAY = "birthday";
	public static final String HOME_PHONE = "home_phone";
	public static final String WORK_PHONE = "work_phone";
	public static final String MOBILE_PHONE = "mobile_phone";
	public static final String EMAIL_ADDRESS = "email_address";
	
	// URI Constants
	public static final int CONTACTS = 1;
	public static final int CONTACT = 2;
	public static final String AUTHORITY = "jagodzinski.steve.hw3";
	public static final String BASE_PATH = "contacts";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.jagodzinski.steve.contact";
	public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.jagodzinski.steve.contact";	
	private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
		// content:jagodzinski.steve.hw3/contacts -> 1
		URI_MATCHER.addURI(AUTHORITY, BASE_PATH, CONTACTS);

		// content:jagodzinski.steve.hw3/contact -> 2
		URI_MATCHER.addURI(AUTHORITY, BASE_PATH + "/#", CONTACT);
	}
	
	private SQLiteDatabase database;
	
	@Override
	public boolean onCreate() {
		database = new OpenHelper(getContext()).getWritableDatabase();
		return true;
	}	
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, 
							String sortOrder) {
		Cursor cursor;
		
		switch (URI_MATCHER.match(uri)) {
			case CONTACT:
				String id = uri.getLastPathSegment();
				selection = ID + "=?";
				selectionArgs = new String[] {id};
				// fall through intended
			case CONTACTS:
				cursor = database.query(CONTACTS_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
				cursor.setNotificationUri(getContext().getContentResolver(), uri);
				break;
			default:
				throw new IllegalArgumentException(String.format("Unsupported URI: %s", uri.toString()));
		}
		
		return cursor;
	}
	
	@Override
	public String getType(Uri uri) {
		String type;
		
		switch (URI_MATCHER.match(uri)) {
			case CONTACTS:
				type = CONTENT_DIR_TYPE;
				break;
			case CONTACT:
				type = CONTENT_ITEM_TYPE;
				break;
			default:
				throw new IllegalArgumentException(String.format("Unsupported URI: %s", uri.toString()));
		}
		
		return type;
	}
	
	@Override 
	public Uri insert(Uri uri, ContentValues values) {
		long id = database.insert(CONTACTS_TABLE, null, values);
		Uri insertedUri = Uri.withAppendedPath(CONTENT_URI, Long.toString(id));
		getContext().getContentResolver().notifyChange(insertedUri, null);
		return insertedUri;
	}

	@Override 
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int numDeleted = database.delete(CONTACTS_TABLE, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return numDeleted;
	}

	@Override 
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int numUpdated = database.update(CONTACTS_TABLE, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return numUpdated;
	}
	
	public static Contact findContact(Context context, long id) {
		Contact returnValue = null;
		
		Uri uri = Uri.withAppendedPath(CONTENT_URI,Long.toString(id));
		
		String[] fullObjectProjection = new String[]{ID, DISPLAY_NAME, FIRST_NAME, LAST_NAME, BIRTHDAY, 
				HOME_PHONE, WORK_PHONE, MOBILE_PHONE, EMAIL_ADDRESS};
		
		Cursor cursor = null;
		
		try {
			cursor = context.getContentResolver().query(uri, fullObjectProjection, ID + "=" + id, null, null);
			
			if (cursor != null && cursor.moveToFirst()) {
				returnValue = Contact.newInstance(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
						cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
						cursor.getString(7), cursor.getString(8));
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		
		return returnValue;
	}
	
	public static void updateContact(Context context, Contact contact) {
		Uri uri = Uri.withAppendedPath(CONTENT_URI, Long.toString(contact.getId()));
		
		ContentValues values = new ContentValues();
		values.put(DISPLAY_NAME, contact.getDisplayName());
		values.put(FIRST_NAME, contact.getFirstName());
		values.put(LAST_NAME, contact.getLastName());
		values.put(BIRTHDAY, contact.getBirthday());
		values.put(HOME_PHONE, contact.getHomePhone());
		values.put(WORK_PHONE, contact.getWorkPhone());
		values.put(MOBILE_PHONE, contact.getMobilePhone());
		values.put(EMAIL_ADDRESS, contact.getEmailAddress());
		
		if (contact.getId() == -1) {
			Uri insertedUri = context.getContentResolver().insert(uri, values);
			String insertedId = insertedUri.getLastPathSegment();
			contact.setId(Long.parseLong(insertedId));
		} else {
			context.getContentResolver().update(uri, values, ID + "=" + contact.getId(), null);
		}
	}
	
	private static class OpenHelper extends SQLiteOpenHelper {
		
		public OpenHelper(Context context) {
			super(context, DATABASE_FILE_NAME, null, DB_VERSION);
		}
		
		@Override 
		public void onCreate(SQLiteDatabase db) {
			try {
				db.beginTransaction();

				String sql = String.format(
						"create table %s " + "(%s integer primary key autoincrement, " + // id
								"%s text," + // display_name
								"%s text," + // first_name
								"%s text," + // last_name
								"%s text," + // birthday
								"%s text," + // home_phone
								"%s text," + // work_phone
								"%s text," + // mobile_phone
								"%s text)", // email_address

						CONTACTS_TABLE, ID, DISPLAY_NAME, FIRST_NAME, LAST_NAME, BIRTHDAY, HOME_PHONE, WORK_PHONE,
							MOBILE_PHONE, EMAIL_ADDRESS);
				
				db.execSQL(sql);
				
				onUpgrade(db, 1, DB_VERSION);
				
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
		}
		
		@Override 
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}
	}
}
