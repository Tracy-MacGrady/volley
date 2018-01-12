package com.android.volley.toolbox;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

/**
 * @email houyunfang@gexing.com
 * @name houyunfang
 * @version 2014-8-8 下午2:52:58
 */
public class MultipartRequest extends Request<String> {

	private MultipartEntity entity = new MultipartEntity();

	private  final String filekey ;
	private static final String STRING_PART_NAME = "text";

	private final Response.Listener<String> mListener;
	private final File mFilePart;
	private final HashMap<String, String> params;

	public MultipartRequest(String url, HashMap<String, String> map,String fileKey,File file, Response.ErrorListener errorListener,
			Response.Listener<String> listener) {
		super(Method.POST, url, errorListener);

		mListener = listener;
		filekey = fileKey;
		mFilePart = file;
		params = map;
		buildMultipartEntity();
	}

	private void buildMultipartEntity() {
		entity.addPart(filekey, new FileBody(mFilePart));
		try {
			  if (params != null && !params.isEmpty()) {  
		            for (Map.Entry<String, String> entry : params.entrySet()) {  
		  
		                StringBody par = new StringBody(entry.getValue());  
		                entity.addPart(entry.getKey(), par);  
		            }  
		        }
		} catch (UnsupportedEncodingException e) {
			VolleyLog.e("UnsupportedEncodingException");
		}
	}

	@Override
	public String getBodyContentType() {
		return entity.getContentType().getValue();
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			entity.writeTo(bos);
		} catch (IOException e) {
			VolleyLog.e("IOException writing to ByteArrayOutputStream");
		}
		return bos.toByteArray();
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		return Response.success("Uploaded", getCacheEntry());
	}

	@Override
	protected void deliverResponse(String response) {
		mListener.onResponse(response);
	}
}
