package jagodzinski.steve.hw3.contact;

import jagodzinski.steve.hw3.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class ContactListActivity extends ActionBarActivity implements ContactListFragmentListener {

	private ContactListFragment contactListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);

		contactListFragment = (ContactListFragment) getSupportFragmentManager().findFragmentById(
				R.id.contact_list_fragment);

		contactListFragment.setFragmentListner(this);
	}

	private void showCreateContactActivity() {
		Intent intent = new Intent(this, EditActivity.class);
		intent.putExtra("contactId", -1l);
		startActivity(intent);
	}

	private void showViewContactActivity(final long contactId) {
		Intent intent = new Intent(this, DisplayActivity.class);
		intent.putExtra("contactId", contactId);
		startActivity(intent);
	}

	@Override
	public void onView(long contactId) {
		showViewContactActivity(contactId);

	}

	@Override
	public void onCreate() {
		showCreateContactActivity();
	}
}
