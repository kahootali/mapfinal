package com.example.dicdog1;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.support.v7.app.ActionBarActivity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class DoctorsList2 extends ListActivity {

private static List<String> jobList;
private static List<String> nameList;
private static List<String> genderList;
private static List<String> genderList1;
private static String name;
//private static String s1;
private static String selectedValue;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_doctor_list);
		jobList =new ArrayList<String>();
		 nameList =new ArrayList<String>();
		 genderList =new ArrayList<String>(); 
		 nameList=SearchingByTime.namelist;
		 jobList=SearchingByTime.joblist;
		 //Toast.makeText(this, "Hello bye bye bye", Toast.LENGTH_LONG).show();
		 //genderList1 =new ArrayList<String>();
		 List<Doctor> doctorList =new ArrayList<Doctor>();
		Intent intent=getIntent();
		ListView lv = getListView();
		LayoutInflater inflater = getLayoutInflater();
		View header = inflater.inflate(R.layout.listview, lv, false);
		lv.addHeaderView(header, null, false);
		setListAdapter(new DoctorListAdapter(this, nameList,jobList));
 		
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {

		//get selected items
		//String selectedValue = (String) getListAdapter().getItem(position);
		position=position-1;
		selectedValue = (String) getListAdapter().getItem(position);		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");
		query.whereEqualTo("Name", selectedValue);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> la,
					com.parse.ParseException e) {
				// TODO Auto-generated method stub
				 if(la!=null){
					 Intent intent=new Intent(DoctorsList2.this,SelectedDoctor.class);
					 intent.putExtra("doctor", selectedValue);
				     startActivity(intent);
					 
					
                         }
                  else{//handle the error}
                            }
				
			}
		});
		
		
	}

	
	/*public void onBackPressed() {
        //SearchingByTime.namelist.removeAll(nameList);
        //SearchingByTime.joblist.removeAll(jobList);
		Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();

	}*/ 
}
