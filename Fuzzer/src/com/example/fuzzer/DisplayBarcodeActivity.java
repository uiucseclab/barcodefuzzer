package com.example.fuzzer;

import java.util.EnumMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class DisplayBarcodeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_display_barcode);

		/**Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.EXTRA_INPUT);
		
		TextView textView = new TextView(this);
		textView.setTextSize(40);
		textView.setText(message);
		
		setContentView(textView);*/
		
		
		Intent intent = getIntent();
		String input = intent.getStringExtra(MainActivity.EXTRA_INPUT);
		String type = intent.getStringExtra(MainActivity.EXTRA_TYPE);
		
		LinearLayout l = new LinearLayout(this);
	    l.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	    l.setOrientation(LinearLayout.VERTICAL);

	    setContentView(l);

	    // barcode image
	    Bitmap bitmap = null;
	    ImageView iv = new ImageView(this);

	    try {

	        switch(type) {
	        case "@string/qr_code": 
	        	bitmap = encodeAsBitmap(input, BarcodeFormat.QR_CODE, 600, 300);
	        	break;
	        case "@string/code_128":
	        	bitmap = encodeAsBitmap(input, BarcodeFormat.CODE_128, 600, 300);
	        	break;
	        case "@string/upc_a":
	        	bitmap = encodeAsBitmap(input, BarcodeFormat.UPC_A, 600, 300);
	        }
	    	//bitmap = encodeAsBitmap(input, BarcodeFormat.QR_CODE, 600, 300);
	        iv.setImageBitmap(bitmap);

	    } catch (WriterException e) {
	        e.printStackTrace();
	    }

	    l.addView(iv);

	    //barcode text
	    TextView tv = new TextView(this);
	    tv.setGravity(Gravity.CENTER_HORIZONTAL);
	    tv.setText(input);

	    l.addView(tv);
		
		
		/**if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
	}
	

	private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

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
			View rootView = inflater.inflate(R.layout.fragment_display_barcode,
					container, false);
			return rootView;
		}
	}

}
