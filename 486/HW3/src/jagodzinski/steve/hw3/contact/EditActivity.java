package jagodzinski.steve.hw3.contact;

import jagodzinski.steve.hw3.R;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class EditActivity extends ActionBarActivity implements EditFragmentListener {

	private EditFragment editFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		initializeEditFragment();
	}

	private void initializeEditFragment() {
		editFragment = (EditFragment) getSupportFragmentManager().findFragmentById(R.id.edit_fragment);
		editFragment.setFragmentListener(this);
		populateFromIntent();
	}

	private void populateFromIntent() {
		long contactId = getIntent().getExtras().getLong("contactId");
		editFragment.setContactId(contactId);
	}

	@Override
	public void onDone(long contactId) {
		getIntent().putExtra("contactId", contactId);
		setResult(RESULT_OK, getIntent());
		finish();
	}

	@Override
	public void onCancel() {
		setResult(RESULT_CANCELED, getIntent());
		finish();
	}
}
