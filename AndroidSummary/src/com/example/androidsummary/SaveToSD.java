package com.example.androidsummary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SaveToSD extends Activity implements OnClickListener {

	Button savePic;

	Button saveSound;

	EditText filename;

	boolean isSDAvail = false, isSDWriteable = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		savePic = (Button) findViewById(getResources().getIdentifier(
				"savePicture", "id", getPackageName()));

		saveSound = (Button) findViewById(getResources().getIdentifier(
				"saveSound", "id", getPackageName()));

		filename = (EditText) findViewById(getResources().getIdentifier(
				"filename", "id", getPackageName()));

		savePic.setOnClickListener(this);

		saveSound.setOnClickListener(this);

		checkSDStuff();

	}

	private void checkSDStuff() {

		// TODO Auto-generated method stub

		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// write
			isSDAvail = true;
			isSDWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// read only
			isSDAvail = true;
			isSDWriteable = false;

		} else {
			// uh-oh
			isSDAvail = false;
			isSDWriteable = false;

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == getResources().getIdentifier("savePicture", "id",
				getPackageName())) {
			if (isSDAvail && isSDWriteable) {
				File path = Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

				String name = filename.getText().toString();
				File file = new File(path, name + ".png");
				try {
					path.mkdirs();
					InputStream is = getResources().openRawResource(

							getResources().getIdentifier("exit", "id",
									getPackageName()));
					OutputStream os;

					os = new FileOutputStream(file);

					byte[] data = new byte[is.available()];

					is.read(data);

					os.write(data);

					is.close();

					os.close();

				} catch (FileNotFoundException e) {

					// TODO Auto-generated catch block

					e.printStackTrace();

				} catch (IOException e) {

					// TODO Auto-generated catch block

					e.printStackTrace();

				}

			}

		}

		else if (v.getId() == getResources().getIdentifier("saveSound", "id",
				getPackageName())) {

			if (isSDAvail && isSDWriteable) {

				File path2 = Environment

				.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

				String name2 = filename.getText().toString();

				File file2 = new File(path2, name2 + ".m4a");

				try {

					path2.mkdirs();

					InputStream is = getResources().openRawResource(

							getResources().getIdentifier("gamemusic", "raw",
									getPackageName()));

					OutputStream os;

					os = new FileOutputStream(file2);

					byte[] data = new byte[is.available()];

					is.read(data);

					os.write(data);

					is.close();

					os.close();

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {

					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

}
