package com.example.stockpartone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class NewsActivity extends Activity {
	ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.news);
		lv=(ListView) findViewById(R.id.news);
		Intent intent=getIntent();
		String json_stock_info_str=intent.getStringExtra("result");
		Log.i("result",json_stock_info_str);
		try {
			JSONObject json=new JSONObject(json_stock_info_str);
			final JSONArray items=json.getJSONObject("result").getJSONObject("News").getJSONArray("Item");
			List<HashMap<String,String>> item_list=new ArrayList<HashMap<String,String>>();
			for(int i=0;i<items.length();i++){
				JSONObject item=items.getJSONObject(i);
				HashMap<String,String> item_hashmap=new HashMap<String,String>();
				item_hashmap.put("title", (String) item.get("Title"));
				item_list.add(item_hashmap);				
			}
			
			SimpleAdapter adapter=new SimpleAdapter(this, item_list, R.layout.onenews,new String[]{"title"}, new int[]{R.id.oneNews});
			lv.setAdapter(adapter);
			
			Toast.makeText(this, "Showing "+adapter.getCount()+" headlines", 1).show();
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String link = null;
					try {
						link = items.getJSONObject(position).getString("Link");
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
						startActivity(intent);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
				
			});
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	

}
