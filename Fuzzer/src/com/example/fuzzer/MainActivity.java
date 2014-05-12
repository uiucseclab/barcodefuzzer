package com.example.fuzzer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class MainActivity extends ActionBarActivity {
	private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
	public String barcode_type = "@string/qr_code";
	public String current_barcode_string = "";
	
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
		current_barcode_string = input;
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
	        	bitmap = encodeAsBitmap(input, BarcodeFormat.QR_CODE, 550, 550);
	        	break;
	        case "@string/code_128":
	        	bitmap = encodeAsBitmap(input, BarcodeFormat.CODE_128, 550, 550);
	        	break;
	        }

	    } catch (WriterException e) {
	        e.printStackTrace();
	    }
		iv.setImageBitmap(bitmap);
		tv.setText(text);
	}
	
	public void randomToBarcode(View view) {
		//Grab necessary info from the layout
		ImageView iv = (ImageView) findViewById(R.id.barcode_view);
		TextView tv = (TextView) findViewById(R.id.barcode_text);
		EditText editText = (EditText) findViewById(R.id.code_length);
		int length =  Integer.parseInt(editText.getText().toString());
		final CheckBox upperBox = (CheckBox) findViewById(R.id.upper_check);
		final CheckBox lowerBox = (CheckBox) findViewById(R.id.lower_check);
		final CheckBox numBox = (CheckBox) findViewById(R.id.num_check);
		final CheckBox specialBox = (CheckBox) findViewById(R.id.special_check);
		boolean upperBool = false;
		boolean lowerBool = false;
		boolean numBool = false;
		boolean specialBool = false;
		int max = 2953;
		CharSequence toastText = "";

		//Setup character space for random generation
		String lower = "abcdefghijklmnopqurstuvwxyz";
		String upper = "ABCDEFGHIJKLMNOPQURSTUVWXYZ";
		String num = "123456789";
		String special = "!@#$%&^*()-_=+`~[]{};,.?/:\"\'\\";
		String charspace = "";
		
		//Read checkboxes to determine what to add to charspace
		if (upperBox.isChecked()) {
			charspace += upper;
			upperBool = true;
		}
		if (lowerBox.isChecked()) {
			charspace += lower;
			lowerBool = true;
		}
		if (numBox.isChecked()) {
			charspace += num;
			numBool = true;
		}
		if (specialBox.isChecked()) {
			charspace += special;
			specialBool = true;
		}
		if (!upperBox.isChecked() && !lowerBox.isChecked() &&
				!numBox.isChecked() && !specialBox.isChecked()) {
			charspace = lower;
		}
		
		//Update max based on character set for QR codes
		if (!specialBool) {
			max = 4296;
		}
		if (numBool && !upperBool && !lowerBool && !specialBool) {
			max = 7089;
		}
		if (barcode_type == "@string/code_128") {
			max = 80;
		}
		
		//Compare max to length. Toast user if length > max and return.
		if (length > max) {
			if (max == 7089) {
				toastText = "String length is too long: max for numeric is 7089";
			}
			if (max == 4296) {
				toastText = "String length is too long: max for alphanumeric is 4296";
			}
			if (max == 2953) {
				toastText = "String length is too long: max for special is 2953";
			}
			if (max == 80) {
				toastText = "String length is too long: max for Code128 is 80";
			}
			Context context = getApplicationContext();
			int duration = Toast.LENGTH_SHORT;
			
			Toast toast = Toast.makeText(context, toastText, duration);
			toast.show();
			return;
		}
		
		char[] chars = charspace.toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String input = sb.toString();
		current_barcode_string = input;
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
	        	bitmap = encodeAsBitmap(input, BarcodeFormat.QR_CODE, 550, 550);
	        	break;
	        case "@string/code_128":
	        	bitmap = encodeAsBitmap(input, BarcodeFormat.CODE_128, 550, 550);
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
		
		int id = view.getId();
		if (id == R.id.qr_select) {
			if (checked)
				barcode_type = "@string/qr_code";
		} else if (id == R.id.code128_select) {
			if (checked)
				barcode_type = "@string/code_128";
		}
	}
	
	
	//Helper functions for saving barcodes and strings
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}
	
	public boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public void saveBarcode(View view) {
		//Pull image info from ImageView, make byte array from it
		ImageView iv = (ImageView) findViewById(R.id.barcode_view);
		Drawable drawable = iv.getDrawable();
		BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
		Bitmap bitmap = bitmapDrawable.getBitmap();
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
				
		//Grab filename from the text field
		EditText editText = (EditText) findViewById(R.id.file_name);
		String name = editText.getText().toString().replaceAll("[^a-zA-Z0-9]", "");
		
		//Open filestream and write the image
		String root = Environment.getExternalStorageDirectory().toString();
	    File myDir = new File(root + "/BarcodeFuzzer");    
	    myDir.mkdirs();
	    Random generator = new Random();
	    int n = 10000;
	    n = generator.nextInt(n);
	    String fnamePic = name + "-" + n + ".jpg";
	    String fnameText = name + "-" + n + ".txt";
	    File filePic = new File (myDir, fnamePic);
	    if (filePic.exists ()) filePic.delete (); 
	    try {
	           FileOutputStream out = new FileOutputStream(filePic);
	           bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
	           out.flush();
	           out.close();

	    } catch (Exception e) {
	           e.printStackTrace();
	    }
	    File fileText = new File (myDir, fnameText);
	    if (fileText.exists ()) fileText.delete (); 
	    try {
	           FileOutputStream out = new FileOutputStream(fileText);
	           out.write(current_barcode_string.getBytes());
	           out.flush();
	           out.close();

	    } catch (Exception e) {
	           e.printStackTrace();
	    }
		
	    Context context = getApplicationContext();
		int duration = Toast.LENGTH_LONG;
		CharSequence toastText = "Barcode saved to: " + root + "/BarcodeFuzzer/" + fnamePic;
		
		Toast toast = Toast.makeText(context, toastText, duration);
		toast.show();
	    
		//Start media scan
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri contentUri = Uri.fromFile(filePic);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent); 
	}
	
	public Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
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
