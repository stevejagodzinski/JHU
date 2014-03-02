package jagodzinski.steve.hw3.contact;

import jagodzinski.steve.hw3.R;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ContactListFragment extends Fragment {

	private static final int CONTACT_LOADER = 1;

	private SimpleCursorAdapter cursorAdapter;

	private ContactListFragmentListener fragmentListner;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

		ListView contactListView = (ListView) view.findViewById(R.id.contacts_list_view);

		cursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.contact_layout, null,
				ContentToViewMapper.databaseColumns, ContentToViewMapper.viewFields, 0);

		contactListView.setAdapter(cursorAdapter);

		contactListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				fragmentListner.onView(id);
			}
		});

		getActivity().getSupportLoaderManager().initLoader(CONTACT_LOADER, null, loaderCallbacks);

		setHasOptionsMenu(true);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.contact_list, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean success;

		switch (item.getItemId()) {
			case R.id.action_create:
				fragmentListner.onCreate();
				success = true;
				break;
			default:
				success = super.onOptionsItemSelected(item);
		}

		return success;
	}

	private LoaderCallbacks<Cursor> loaderCallbacks = new LoaderCallbacks<Cursor>() {
		@Override
		public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
			return new CursorLoader(getActivity(), ContactContentProvider.CONTENT_URI,
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

	public void setFragmentListner(final ContactListFragmentListener fragmentListner) {
		this.fragmentListner = fragmentListner;
	}

	private static class ContentToViewMapper {
		public static String[] databaseColumns = { ContactContentProvider.ID, ContactContentProvider.DISPLAY_NAME,
				ContactContentProvider.HOME_PHONE };
		public static int[] viewFields = { -1, R.id.display_name, R.id.home_phone };
	}
}
