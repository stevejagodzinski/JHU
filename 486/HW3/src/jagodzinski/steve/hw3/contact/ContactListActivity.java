package jagodzinski.steve.hw3.contact;

import jagodzinski.steve.hw3.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

public class ContactListActivity extends ActionBarActivity implements ContactListFragmentListener,
		DisplayFragmentListener, EditFragmentListener {

	private ContactListFragment contactListFragment;
	private DisplayFragment displayFragment;
	private EditFragment editFragment;

	boolean dual_mode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);

		displayFragment = (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.display_fragment);
		editFragment = (EditFragment) getSupportFragmentManager().findFragmentById(R.id.edit_fragment);

		dual_mode = (displayFragment != null && displayFragment.isInLayout()) ||
				(editFragment != null && editFragment.isInLayout());

		initializeContactlistFragment();

		if (dual_mode) {
			displayFragment.setFragmentListener(this);
			editFragment.setFragmentListener(this);

			switchToDisplayFragment();
		}
	}

	private void initializeContactlistFragment() {
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

	private void showSelectedContactInDisplayFragment(final long selectedContactId) {
		displayFragment.setContactId(selectedContactId);
	}

	private void showSelectedContactInEditFragment(final long selectedContactId) {
		editFragment.setContactId(selectedContactId);
	}

	private void switchToEditFragment() {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.hide(displayFragment);
		transaction.show(editFragment);
		transaction.commit();
	}

	private void switchToDisplayFragment() {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.hide(editFragment);
		transaction.show(displayFragment);
		transaction.commit();
	}

	@Override
	public void onView(final long contactId) {
		if (dual_mode) {
			if (displayFragment.isVisible()) {
				showSelectedContactInDisplayFragment(contactId);
			} else if (editFragment.isVisible()) {
				showSelectedContactInEditFragment(contactId);
			}
		} else {
			showViewContactActivity(contactId);
		}
	}

	@Override
	public void onCreate() {
		if (dual_mode) {
			switchToEditFragment();
			showSelectedContactInEditFragment(-1l);
		} else {
			showCreateContactActivity();
		}
	}

	@Override
	public void onDone(long contactId) {
		showSelectedContactInDisplayFragment(contactId);
		switchToDisplayFragment();
	}

	@Override
	public void onCancel() {
		switchToDisplayFragment();
	}

	@Override
	public void onEdit(long contactId) {
		showSelectedContactInEditFragment(contactId);
		switchToEditFragment();
	}
}
