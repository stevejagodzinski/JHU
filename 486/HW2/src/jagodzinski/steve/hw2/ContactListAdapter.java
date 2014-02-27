package jagodzinski.steve.hw2;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ContactListAdapter extends BaseAdapter {

	private final List<Contact> contacts;
	private final LayoutInflater layoutInflater;

	public ContactListAdapter(final List<Contact> contacts, final LayoutInflater layoutInflater) {
		this.contacts = contacts;
		this.layoutInflater = layoutInflater;
	}

	@Override
	public int getCount() {
		return contacts.size();
	}

	@Override
	public Contact getItem(int position) {
		return contacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = createNewContactLayoutView();
		}

		ViewHolder viewHolder = (ViewHolder) convertView.getTag();

		Contact contact = getItem(position);
		viewHolder.displayName.setText(contact.getDisplayName());
		viewHolder.phoneNumber.setText(contact.getHomePhone());

		return convertView;
	}

	private View createNewContactLayoutView() {
		View contactLayoutView = layoutInflater.inflate(R.layout.contact_layout, null);
		addViewHolder(contactLayoutView);
		return contactLayoutView;
	}

	private void addViewHolder(View contactLayoutView) {
		TextView nameTextField = (TextView) contactLayoutView.findViewById(R.id.display_name);
		TextView phoneNumberTextField = (TextView) contactLayoutView.findViewById(R.id.home_phone);

		ViewHolder viewHolder = new ViewHolder();
		viewHolder.displayName = nameTextField;
		viewHolder.phoneNumber = phoneNumberTextField;

		contactLayoutView.setTag(viewHolder);
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return contacts.isEmpty();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	private static class ViewHolder {
		private TextView displayName;
		private TextView phoneNumber;
	}

}
