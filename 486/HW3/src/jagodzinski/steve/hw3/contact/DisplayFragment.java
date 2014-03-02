package jagodzinski.steve.hw3.contact;

import jagodzinski.steve.hw3.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DisplayFragment extends Fragment {

	private DisplayFragmentListener fragmentListener;

	private Contact contact;

	private TextView displayName;
	private TextView firstName;
	private TextView lastName;
	private TextView birthday;
	private TextView homePhone;
	private TextView workPhone;
	private TextView mobilePhone;
	private TextView email;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_display, container, false);

		cacheTextViewFields(view);
		setHasOptionsMenu(true);

		return view;
	}

	private void cacheTextViewFields(final View view) {
		displayName = (TextView) view.findViewById(R.id.display_name);
		firstName = (TextView) view.findViewById(R.id.first_name);
		lastName = (TextView) view.findViewById(R.id.last_name);
		birthday = (TextView) view.findViewById(R.id.birthday);
		homePhone = (TextView) view.findViewById(R.id.home_phone);
		workPhone = (TextView) view.findViewById(R.id.work_phone);
		mobilePhone = (TextView) view.findViewById(R.id.mobile_phone);
		email = (TextView) view.findViewById(R.id.email_address);
	}

	public void setContactId(final long contactId) {
		contact = ContactContentProvider.findContact(getActivity(), contactId);
		populateTextFields();
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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.display, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean success;

		switch (item.getItemId()) {
			case R.id.action_edit:
				fragmentListener.onEdit(contact.getId());
				success = true;
				break;
			default:
				success = super.onOptionsItemSelected(item);
		}

		return success;
	}

	public void setFragmentListener(final DisplayFragmentListener fragmentlistener) {
		this.fragmentListener = fragmentlistener;
	}
}
