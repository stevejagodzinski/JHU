package jagodzinski.steve.hw6.ufo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UFOPosition implements Parcelable {
	private int shipNumber;
	private double lat;
	private double lon;

	public UFOPosition() {

	}

	public int getShipNumber() {
		return shipNumber;
	}

	public void setShipNumber(int shipNumber) {
		this.shipNumber = shipNumber;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	// Parcelable
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(shipNumber);
		dest.writeDouble(lat);
		dest.writeDouble(lon);
	}

	public static final Parcelable.Creator<UFOPosition> CREATOR = new Parcelable.Creator<UFOPosition>() {
		public UFOPosition createFromParcel(Parcel in) {
			return new UFOPosition(in);
		}

		public UFOPosition[] newArray(int size) {
			return new UFOPosition[size];
		}
	};

	private UFOPosition(Parcel in) {
		shipNumber = in.readInt();
		lat = in.readDouble();
		lon = in.readDouble();
	}
}
