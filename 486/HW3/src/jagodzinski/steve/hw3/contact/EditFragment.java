package jagodzinski.steve.hw3.contact;

import jagodzinski.steve.hw3.R;

import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class EditFragment extends Fragment {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	private EditFragmentListener fragmentListener;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_edit, container, false);

		setHasOptionsMenu(true);

		cacheEditTextFields(view);
		addBirthdayFormatHint();

		return view;
	}

	private void addBirthdayFormatHint() {
		birthday.setHint(dateFormat.toPattern());
	}

	private void cacheEditTextFields(final View view) {
		displayName = (EditText) view.findViewById(R.id.display_name);
		firstName = (EditText) view.findViewById(R.id.first_name);
		lastName = (EditText) view.findViewById(R.id.last_name);
		birthday = (EditText) view.findViewById(R.id.birthday);
		homePhone = (EditText) view.findViewById(R.id.home_phone);
		workPhone = (EditText) view.findViewById(R.id.work_phone);
		mobilePhone = (EditText) view.findViewById(R.id.mobile_phone);
		email = (EditText) view.findViewById(R.id.email_address);
	}

	public void setContactId(final long contactId) {
		if (contactId == -1) {
			contact = Contact.newInstance();
		} else {
			contact = ContactContentProvider.findContact(getActivity(), contactId);
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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.edit, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean success;

		switch (item.getItemId()) {
			case R.id.action_done:
				bindContactFields();
				ContactContentProvider.updateContact(getActivity(), contact);
				fragmentListener.onDone(contact.getId());
				success = true;
				break;
			case R.id.action_canel:
				fragmentListener.onCancel();
				success = true;
				break;
			default:
				success = super.onOptionsItemSelected(item);
		}

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

	public void setFragmentListener(final EditFragmentListener fragmentListener) {
		this.fragmentListener = fragmentListener;
	}
}
