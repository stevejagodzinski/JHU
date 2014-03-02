package jagodzinski.steve.hw3.contact;

import jagodzinski.steve.hw3.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class DisplayActivity extends ActionBarActivity implements DisplayFragmentListener, ContactListFragmentListener {

	private static final int EDIT_CONTACT_REQUEST_CODE = 2;

	private ContactListFragment contactListFragment;
	private DisplayFragment displayFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_display);
		initializeDisplayFragment();
		initializeContactlistFragment();
	}

	private void initializeContactlistFragment() {
		contactListFragment = (ContactListFragment) getSupportFragmentManager().findFragmentById(
				R.id.contact_list_fragment);

		if (contactListFragment != null) {
			contactListFragment.setFragmentListner(this);
		}
	}

	private void initializeDisplayFragment() {
		displayFragment = (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.display_fragment);
		displayFragment.setFragmentListener(this);
		populateDisplayFragment(getIntent());
	}

	private void populateDisplayFragment(final Intent intent) {
		displayFragment.setContactId(getIntent().getExtras().getLong("contactId"));
	}

	private void showEditContactActivity(final long contactId) {
		Intent intent = new Intent(this, EditActivity.class);
		intent.putExtra("contactId", contactId);
		startActivityForResult(intent, EDIT_CONTACT_REQUEST_CODE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EDIT_CONTACT_REQUEST_CODE && resultCode == RESULT_OK) {
			populateDisplayFragment(data);
		}
	}

	@Override
	public void onEdit(final long contactId) {
		showEditContactActivity(contactId);
	}

	@Override
	public void onView(long contactId) {
		showSelectedContactInDisplayFragment(contactId);
	}

	private void showSelectedContactInDisplayFragment(final long selectedContactId) {
		displayFragment.setContactId(selectedContactId);
	}

	@Override
	public void onCreate() {
		showCreateContactActivity();
	}

	private void showCreateContactActivity() {
		Intent intent = new Intent(this, EditActivity.class);
		intent.putExtra("contactId", -1l);
		startActivity(intent);
	}
}
