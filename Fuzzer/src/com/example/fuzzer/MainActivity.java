package com.example.fuzzer;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class MainActivity extends ActionBarActivity {
	public final static String EXTRA_INPUT = "com.example.fuzzer.INPUT";
	public final static String EXTRA_TYPE = "com.example.fuzzer.TYPE";
	private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
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
		ImageView iv = (ImageView) findViewById(R.id.barcode_view);
		TextView tv = (TextView) findViewById(R.id.barcode_text);
		EditText editText = (EditText) findViewById(R.id.edit_input);
		String input = editText.getText().toString();
		String text = null;
		if (input.length() > 30){
			text = input.substring(0, 30).concat("... (Total length: )").concat(Integer.toString(input.length()));
		} else {
			text = input;
		}
		Bitmap bitmap = null;
		try {

	        switch(barcode_type) {
	        case "@string/qr_code": 
	        	bitmap = encodeAsBitmap(input, BarcodeFormat.QR_CODE, 300, 150);
	        	break;
	        case "@string/code_128":
	        	bitmap = encodeAsBitmap(input, BarcodeFormat.CODE_128, 300, 150);
	        	break;
	        }

	    } catch (WriterException e) {
	        e.printStackTrace();
	    }
		iv.setImageBitmap(bitmap);
		tv.setText(text);
	}
	
	public void randomToBarcode(View view) {
		ImageView iv = (ImageView) findViewById(R.id.barcode_view);
		TextView tv = (TextView) findViewById(R.id.barcode_text);
		EditText editText = (EditText) findViewById(R.id.code_length);
		int length =  Integer.parseInt(editText.getText().toString());
		String lower = "abcdefghijklmnopqurstuvwxyz";
		String upper = "ABCDEFGHIJKLMNOPQURSTUVWXYZ";
		String num = "123456789";
		String special = "!@#$%&^*()-_=+`~[]{};,.?/:\"\'\\";
		String charspace = "";
		charspace += special;
		char[] chars = charspace.toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String input = sb.toString();
		String text = null;
		if (input.length() > 30){
			text = input.substring(0, 30).concat("... \nTotal length: ").concat(Integer.toString(input.length()));
		} else {
			text = input;
		}
		Bitmap bitmap = null;
		try {

	        switch(barcode_type) {
	        case "@string/qr_code": 
	        	bitmap = encodeAsBitmap(input, BarcodeFormat.QR_CODE, 300, 150);
	        	break;
	        case "@string/code_128":
	        	bitmap = encodeAsBitmap(input, BarcodeFormat.CODE_128, 300, 150);
	        	break;
	        }

	    } catch (WriterException e) {
	        e.printStackTrace();
	    }
		iv.setImageBitmap(bitmap);
		tv.setText(text);

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
		}
	}
	
	Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
	    String contentsToEncode = contents;
	    if (contentsToEncode == null) {
	        return null;
	    }
	    Map<EncodeHintType, Object> hints = null;
	    String encoding = guessAppropriateEncoding(contentsToEncode);
	    if (encoding != null) {
	        hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
	        hints.put(EncodeHintType.CHARACTER_SET, encoding);
	    }
	    MultiFormatWriter writer = new MultiFormatWriter();
	    BitMatrix result;
	    try {
	        result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
	    } catch (IllegalArgumentException iae) {
	        // Unsupported format
	        return null;
	    }
	    int width = result.getWidth();
	    int height = result.getHeight();
	    int[] pixels = new int[width * height];
	    for (int y = 0; y < height; y++) {
	        int offset = y * width;
	        for (int x = 0; x < width; x++) {
	        pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
	        }
	    }

	    Bitmap bitmap = Bitmap.createBitmap(width, height,
	        Bitmap.Config.ARGB_8888);
	    bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
	    return bitmap;
	    }

	    private static String guessAppropriateEncoding(CharSequence contents) {
	    // Very crude at the moment
	    for (int i = 0; i < contents.length(); i++) {
	        if (contents.charAt(i) > 0xFF) {
	        return "UTF-8";
	        }
	    }
	    return null;
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
