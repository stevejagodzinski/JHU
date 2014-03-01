package jagodzinski.steve.hw3.contact;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable {
	private long id = -1;
	private String displayName;
	private String firstName;
	private String lastName;
	private String birthday;
	private String homePhone;
	private String workPhone;
	private String mobilePhone;
	private String emailAddress;
	
	static Contact newInstance(final long id, final String displayName, final String firstName, 
 final String lastName,
			final String birthday, final String homePhone, final String workPhone,
			final String mobilePhone, final String emailAddress) {
		Contact instance = newInstance();
		instance.setId(id);
		instance.setDisplayName(displayName);
		instance.setFirstName(firstName);
		instance.setLastName(lastName);
		instance.setBirthday(birthday);
		instance.setHomePhone(homePhone);
		instance.setWorkPhone(workPhone);
		instance.setMobilePhone(mobilePhone);
		instance.setEmailAddress(emailAddress);
		return instance;
	}
	
	static Contact newInstance() {
		return new Contact();
	}

	private Contact() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	// Parcelable
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(displayName);
		dest.writeString(firstName);
		dest.writeString(homePhone);
		dest.writeString(workPhone);
		dest.writeString(mobilePhone);
		dest.writeString(emailAddress);
		dest.writeString(lastName);
		dest.writeString(birthday);
	}

	public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
		public Contact createFromParcel(Parcel in) {
			return new Contact(in);
		}

		public Contact[] newArray(int size) {
			return new Contact[size];
		}
	};

	private Contact(Parcel in) {
		id = in.readLong();
		displayName = in.readString();
		firstName = in.readString();
		homePhone = in.readString();
		workPhone = in.readString();
		mobilePhone = in.readString();
		emailAddress = in.readString();
		lastName = in.readString();
		birthday = in.readString();
	}
}
