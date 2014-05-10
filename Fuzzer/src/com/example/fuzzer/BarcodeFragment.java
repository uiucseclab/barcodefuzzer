package com.example.fuzzer;

import java.util.EnumMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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


public class BarcodeFragment extends Fragment {

	public static BarcodeFragment newInstance(String input) {
		BarcodeFragment f = new BarcodeFragment();
		
		Bundle args = new Bundle();
		args.putString("input", input);
		f.setArguments(args);
		
		return f;
	}
	
	public String getInput() {
		return getArguments().getString("input", "Example Barcode");
	}
	
	public BarcodeFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LinearLayout l = new LinearLayout(getActivity());
	    l.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	    l.setOrientation(LinearLayout.VERTICAL);
	    
	    String input = getInput();

	    // barcode image
	    Bitmap bitmap = null;
	    ImageView iv = new ImageView(getActivity());
	    
	    try {
	    bitmap = encodeAsBitmap(input, BarcodeFormat.QR_CODE, 600, 300);
	    iv.setImageBitmap(bitmap);

	    } catch (WriterException e) {
	        e.printStackTrace();
	    }

	    l.addView(iv);

	    //barcode text
	    TextView tv = new TextView(getActivity());
	    tv.setGravity(Gravity.CENTER_HORIZONTAL);
	    tv.setText(input);

	    l.addView(tv);
		return l;
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


}
