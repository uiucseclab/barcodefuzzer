package com.example.fuzzer;

import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

public class MainActivity extends ActionBarActivity {
	public final static String EXTRA_INPUT = "com.example.fuzzer.INPUT";
	public final static String EXTRA_TYPE = "com.example.fuzzer.TYPE";
	public String barcode_type = "@string/qr_code";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	public void inputToBarcode(View view) {
		Intent intent = new Intent(this, DisplayBarcodeActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_input);
		String input = editText.getText().toString();
		intent.putExtra(EXTRA_INPUT, input);
		intent.putExtra(EXTRA_TYPE, barcode_type);
		startActivity(intent);
	}
	
	//TODO: make length grab length from input widgets
	public void randomToBarcode(View view) {
		Intent intent = new Intent(this, DisplayBarcodeActivity.class);
		EditText editText = (EditText) findViewById(R.id.code_length);
		int length =  Integer.parseInt(editText.getText().toString());
		String lower = "abcdefghijklmnopqurstuvwxyz";
		String upper = "ABCDEFGHIJKLMNOPQURSTUVWXYZ";
		String num = "123456789";
		String special = "!@#$%&^*()-_=+`~[]{};,.?/:\"\'\\";
		String charspace = "";
		charspace += special;
		char[] chars = charspace.toCharArray();
		//int length = 100;
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String input = sb.toString();
		intent.putExtra(EXTRA_INPUT, input);
		intent.putExtra(EXTRA_TYPE, barcode_type);
		startActivity(intent);
	}
	
	public void onTypeSelected(View view) {
		boolean checked = ((RadioButton) view).isChecked();
		
		switch(view.getId()) {
		case R.id.qr_select:
			if (checked)
				barcode_type = "@string/qr_code";
			break;
		case R.id.code128_select:
			if (checked)
				barcode_type = "@string/code_128";
			break;
		case R.id.upc_select:
			if (checked)
				barcode_type = "@string/upc_a";
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
