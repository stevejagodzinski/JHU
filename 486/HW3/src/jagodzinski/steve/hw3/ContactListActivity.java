package jagodzinski.steve.hw3;

import jagodzinski.steve.hw2.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ContactListActivity extends ActionBarActivity {

	private static final int CREATE_CONTACT_REQUEST_CODE = 1;
	private static final int DISPLAY_CONTACT_REQUEST_CODE = 3;

	private List<Contact> contacts = new ArrayList<Contact>();
	private long nextId = contacts.size();

	private ContactListAdapter contactListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);

		contactListAdapter = new ContactListAdapter(contacts, getLayoutInflater());
		ListView contactListView = (ListView) findViewById(R.id.contacts_list_view);
		contactListView.setAdapter(contactListAdapter);

		contactListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setType("application/vnd.jagodzinski.steve.hw2.Contact");
				intent.putExtra("contact", contactListAdapter.getItem(position));
				startActivityForResult(intent, DISPLAY_CONTACT_REQUEST_CODE);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean success;

		switch(item.getItemId()) {
			case  R.id.action_create:
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
		intent.putExtra("contact", new Contact(nextId++));
		startActivityForResult(intent, CREATE_CONTACT_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CREATE_CONTACT_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				handleAddNewContact(data);
			}
		} else if (requestCode == DISPLAY_CONTACT_REQUEST_CODE) {
			handleMergeModifiedContact(data);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void handleAddNewContact(Intent data) {
		Contact newContact = (Contact) data.getParcelableExtra("contact");
		contacts.add(newContact);
		contactListAdapter.notifyDataSetChanged();
	}

	private void handleMergeModifiedContact(Intent data) {
		Contact modifiedContact = (Contact) data.getParcelableExtra("contact");
		for (int i = 0; i < contacts.size(); i++) {
			if (contacts.get(i).getId() == modifiedContact.getId()) {
				contacts.set(i, modifiedContact);
				contactListAdapter.notifyDataSetChanged();
				break;
			}
		}
	}
}
