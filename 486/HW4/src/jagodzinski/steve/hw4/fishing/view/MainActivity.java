package jagodzinski.steve.hw4.fishing.view;

import jagodzinski.steve.hw4.fishing.R;
import jagodzinski.steve.hw4.fishing.controller.LineLengthObserver;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends Activity {

	private LineLengthObserver fishingViewCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		fishingViewCanvas = (LineLengthObserver) findViewById(R.id.fishingViewCanvas);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean success;

		switch (item.getItemId()) {
			case R.id.action_set_line_length:
				promptForLineLength();
				success = true;
				break;
			default:
				success = super.onOptionsItemSelected(item);
		}

		return success;
	}

	private void promptForLineLength() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		String titleAndMessage = getResources().getString(R.string.set_line_length);
		alert.setTitle(titleAndMessage);
		alert.setMessage(titleAndMessage);

		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		alert.setView(input);

		alert.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				fishingViewCanvas.onLineLengthChange(Integer.valueOf(value));
			}
		});

		alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});

		alert.show();
	}
}
