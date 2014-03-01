package jagodzinski.steve.hw3.contact;

import jagodzinski.steve.hw3.R;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ContactListActivity extends ActionBarActivity {

	private static final int CONTACT_LOADER = 1;

	private SimpleCursorAdapter cursorAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);

		ListView contactListView = (ListView) findViewById(R.id.contacts_list_view);

		cursorAdapter = new SimpleCursorAdapter(this, R.layout.contact_layout, null,
				ContentToViewMapper.databaseColumns, ContentToViewMapper.viewFields, 0);

		contactListView.setAdapter(cursorAdapter);

		contactListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(ContactListActivity.this, DisplayActivity.class);
				intent.putExtra("contactId", id);
				startActivity(intent);
			}
		});

		getSupportLoaderManager().initLoader(CONTACT_LOADER, null, loaderCallbacks);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.contact_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean success;

		switch (item.getItemId()) {
		case R.id.action_create:
			showCreateContactActivity();
			success = true;
			break;
		default:
			success = super.onOptionsItemSelected(item);
		}

		return success;
	}

	private void showCreateContactActivity() {
		Intent intent = new Intent(this, EditActivity.class);
		intent.putExtra("contactId", -1l);
		startActivity(intent);
	}

	private LoaderCallbacks<Cursor> loaderCallbacks = new LoaderCallbacks<Cursor>() {
		@Override
		public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
			return new CursorLoader(ContactListActivity.this, ContactContentProvider.CONTENT_URI,
					ContentToViewMapper.databaseColumns, null, null, ContactContentProvider.DISPLAY_NAME + " asc");
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			cursorAdapter.swapCursor(cursor); // set the data
		}

		@Override
		public void onLoaderReset(Loader<Cursor> cursor) {
			cursorAdapter.swapCursor(null); // clear the data
		}
	};

	private static class ContentToViewMapper {
		public static String[] databaseColumns = { ContactContentProvider.ID, ContactContentProvider.DISPLAY_NAME,
				ContactContentProvider.HOME_PHONE };
		public static int[] viewFields = { -1, R.id.display_name, R.id.home_phone };
	}
}
