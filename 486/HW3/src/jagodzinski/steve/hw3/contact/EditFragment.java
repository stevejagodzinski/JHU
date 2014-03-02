package jagodzinski.steve.hw3.contact;

import jagodzinski.steve.hw3.R;
import jagodzinski.steve.hw3.contact.DatePickerDialogFragment.OnDatePickerDialogFragmentDateSetListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditFragment extends Fragment implements OnDatePickerDialogFragmentDateSetListener {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	private static final int DATE_ID_BIRTHDAY = 1;

	private EditFragmentListener fragmentListener;

	private Contact contact;

	private Calendar birthdayCalendar;

	private EditText displayNameEditText;
	private EditText firstNameEditText;
	private EditText lastNameEditText;
	private Button birthdayButton;
	private EditText homePhoneEditText;
	private EditText workPhoneEditText;
	private EditText mobilePhoneEditText;
	private EditText emailEditText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_edit, container, false);

		setHasOptionsMenu(true);

		cacheEditTextFields(view);
		showDatePickerDialogOnBirthdayButtonClick();

		return view;
	}

	private void showDatePickerDialogOnBirthdayButtonClick() {
		birthdayButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDatePickerDialog();
			}
		});
	}

	private void showDatePickerDialog() {
		DatePickerDialogFragment fragment = DatePickerDialogFragment.create(EditFragment.this, DATE_ID_BIRTHDAY,
				birthdayCalendar == null ? Calendar.getInstance() : birthdayCalendar);
		fragment.show(getActivity().getSupportFragmentManager(), "setting birthday");
	}

	private void cacheEditTextFields(final View view) {
		displayNameEditText = (EditText) view.findViewById(R.id.display_name);
		firstNameEditText = (EditText) view.findViewById(R.id.first_name);
		lastNameEditText = (EditText) view.findViewById(R.id.last_name);
		birthdayButton = (Button) view.findViewById(R.id.birthday);
		homePhoneEditText = (EditText) view.findViewById(R.id.home_phone);
		workPhoneEditText = (EditText) view.findViewById(R.id.work_phone);
		mobilePhoneEditText = (EditText) view.findViewById(R.id.mobile_phone);
		emailEditText = (EditText) view.findViewById(R.id.email_address);
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
		displayNameEditText.setText(contact.getDisplayName());
		firstNameEditText.setText(contact.getFirstName());
		lastNameEditText.setText(contact.getLastName());
		homePhoneEditText.setText(contact.getHomePhone());
		workPhoneEditText.setText(contact.getWorkPhone());
		mobilePhoneEditText.setText(contact.getMobilePhone());
		emailEditText.setText(contact.getEmailAddress());
		birthdayButton.setText(contact.getBirthday());
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
		contact.setDisplayName(displayNameEditText.getText().toString());
		contact.setEmailAddress(emailEditText.getText().toString());
		contact.setFirstName(firstNameEditText.getText().toString());
		contact.setLastName(lastNameEditText.getText().toString());
		contact.setHomePhone(homePhoneEditText.getText().toString());
		contact.setMobilePhone(mobilePhoneEditText.getText().toString());
		contact.setWorkPhone(workPhoneEditText.getText().toString());
		contact.setBirthday(birthdayButton.getText().toString());
	}

	public void setFragmentListener(final EditFragmentListener fragmentListener) {
		this.fragmentListener = fragmentListener;
	}

	@Override
	public void onDateSet(int dateId, int year, int month, int day) {
		switch (dateId) {
			case DATE_ID_BIRTHDAY:
				updateBirthdayCalendar(year, month, day);
				updateDateButtonText("Birthday", birthdayButton, birthdayCalendar);
				break;
			default:
				throw new IllegalArgumentException(String.format("Unexpected dateId: %d", dateId));
		}
	}

	private void updateBirthdayCalendar(int year, int month, int day) {
		if (birthdayCalendar == null) {
			birthdayCalendar = Calendar.getInstance();
		}

		birthdayCalendar.set(year, month, day);
	}

	private void updateDateButtonText(String header, TextView button, Calendar calendar) {
		String text = dateFormat.format(calendar.getTime());
		button.setText(text);
	}
}
