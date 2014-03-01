package jagodzinski.steve.hw3.contact;

import jagodzinski.steve.hw3.R;

import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class EditActivity extends ActionBarActivity {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	private Contact contact;

	private EditText displayName;
	private EditText firstName;
	private EditText lastName;
	private EditText birthday;
	private EditText homePhone;
	private EditText workPhone;
	private EditText mobilePhone;
	private EditText email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		cacheEditTextFields();
		addBirthdayFormatHint();
		populateFromIntent();
	}

	private void addBirthdayFormatHint() {
		birthday.setHint(dateFormat.toPattern());
	}

	private void cacheEditTextFields() {
		displayName = (EditText) findViewById(R.id.display_name);
		firstName = (EditText) findViewById(R.id.first_name);
		lastName = (EditText) findViewById(R.id.last_name);
		birthday = (EditText) findViewById(R.id.birthday);
		homePhone = (EditText) findViewById(R.id.home_phone);
		workPhone = (EditText) findViewById(R.id.work_phone);
		mobilePhone = (EditText) findViewById(R.id.mobile_phone);
		email = (EditText) findViewById(R.id.email_address);
	}

	private void populateFromIntent() {
		long contactId = getIntent().getExtras().getLong("contactId");

		if (contactId == -1) {
			contact = Contact.newInstance();
		} else {
			contact = ContactContentProvider.findContact(this, contactId);
		}

		populateUIFromContact();
	}

	private void populateUIFromContact() {
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
		getMenuInflater().inflate(R.menu.edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean success;

		switch (item.getItemId()) {
			case R.id.action_done:
				bindContactFields();
				ContactContentProvider.updateContact(this, contact);
				getIntent().putExtra("contactId", this.contact.getId());
				setResult(RESULT_OK, getIntent());
				success = true;
				break;
			case R.id.action_canel:
				setResult(RESULT_CANCELED, getIntent());
				success = true;
				break;
			default:
				success = super.onOptionsItemSelected(item);
		}

		finish();

		return success;
	}

	private void bindContactFields() {
		contact.setDisplayName(displayName.getText().toString());
		contact.setEmailAddress(email.getText().toString());
		contact.setFirstName(firstName.getText().toString());
		contact.setLastName(lastName.getText().toString());
		contact.setHomePhone(homePhone.getText().toString());
		contact.setMobilePhone(mobilePhone.getText().toString());
		contact.setWorkPhone(workPhone.getText().toString());
		contact.setBirthday(birthday.getText().toString());
	}
}
