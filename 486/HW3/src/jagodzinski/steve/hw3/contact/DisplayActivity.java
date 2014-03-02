package jagodzinski.steve.hw3.contact;

import jagodzinski.steve.hw3.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayActivity extends ActionBarActivity {

	private static final int EDIT_CONTACT_REQUEST_CODE = 2;

	private Contact contact;

	private TextView displayName;
	private TextView firstName;
	private TextView lastName;
	private TextView birthday;
	private TextView homePhone;
	private TextView workPhone;
	private TextView mobilePhone;
	private TextView email;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_display);
		cacheTextViewFields();
		setContact(getIntent());
		populateTextFields();
	}

	private void cacheTextViewFields() {
		displayName = (TextView) findViewById(R.id.display_name);
		firstName = (TextView) findViewById(R.id.first_name);
		lastName = (TextView) findViewById(R.id.last_name);
		birthday = (TextView) findViewById(R.id.birthday);
		homePhone = (TextView) findViewById(R.id.home_phone);
		workPhone = (TextView) findViewById(R.id.work_phone);
		mobilePhone = (TextView) findViewById(R.id.mobile_phone);
		email = (TextView) findViewById(R.id.email_address);
	}

	private void setContact(Intent intent) {
		contact = ContactContentProvider.findContact(this, getIntent().getExtras().getLong("contactId"));
	}

	private void populateTextFields() {
		displayName.setText(contact.getDisplayName());
		firstName.setText(contact.getFirstName());
		lastName.setText(contact.getLastName());
		homePhone.setText(contact.getHomePhone());
		workPhone.setText(contact.getWorkPhone());
		mobilePhone.setText(contact.getMobilePhone());
		email.setText(contact.getEmailAddress());
		birthday.setText(contact.getBirthday());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean success;

		switch (item.getItemId()) {
			case R.id.action_edit:
				showEditContactActivity();
				success = true;
				break;
			default:
				success = super.onOptionsItemSelected(item);
		}

		return success;
	}

	private void showEditContactActivity() {
		Intent intent = new Intent(this, EditActivity.class);
		intent.putExtra("contactId", getIntent().getExtras().getLong("contactId"));
		startActivityForResult(intent, EDIT_CONTACT_REQUEST_CODE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EDIT_CONTACT_REQUEST_CODE && resultCode == RESULT_OK) {
			setContact(data);
			populateTextFields();
		}
	}

}
