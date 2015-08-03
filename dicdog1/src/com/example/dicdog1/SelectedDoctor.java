package com.example.dicdog1;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class SelectedDoctor extends ActionBarActivity {

	private static ImageButton icon;
	private static Button call;
	//private static Button Schedule;
	private static TextView main;
	private static TextView name;
	private static TextView spec;
	private static TextView gender;
	private static TextView hospital;
	private static TextView timings;
	private static TextView experience;
	private static EditText edit1;
	private static Button button1;
	private static ImageView callimage;
	private static Doctor doc;
	private static String schedule;
	private static String mainSchedule;
	private static Button buttonMaps;
	private static Button close;
	private static String nameOfDoctor;
	
	
	private static double longitude;
	private static double latitude;
	private float distance;	
	private static CheckBox check;
	private static boolean manual;
	AppLocationService appLocationService;	
	//List<Doctor> doctorList = new ArrayList<Doctor>();
	private static List LatLngList;     
	private static List<String> nameList;
	private static List<String> hospitalList;	
   private static GoogleMap googleMap; 
   private static SupportMapFragment mMapFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar=getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_selected_doctor);	
		
		/*Button home=(Button) findViewById(R.id.homebitton);
		home=(Button)findViewById(R.id.button2);
		home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(SelectedDoctor.this,DashboardActivity.class);
				startActivity(i);
			}
		});*/
		//call = (Button)findViewById(R.id.button2);
		mMapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapFrag));
		LatLngList =new ArrayList<LatLng>();
		nameList=new ArrayList<String>();
	    hospitalList=new ArrayList<String>();
		nameOfDoctor="";		
		callimage=(ImageView)findViewById(R.id.imageView2);
		callimage.setClickable(true);
		callimage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String number="tel:";
				if(hospital.getText().equals("MAROOF International Hospital"))
				{
					number += "051 111 644 911";				
				}
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
			    startActivity(intent);
			}
		});
		
		name=(TextView)findViewById(R.id.textView1);
		spec=(TextView)findViewById(R.id.textView3);		
		gender=(TextView)findViewById(R.id.TextView02);
		hospital=(TextView)findViewById(R.id.textView8);		
		experience=(TextView)findViewById(R.id.textView9);
		icon = (ImageButton)findViewById(R.id.ImageButton);
		icon.setClickable(false);		
		/*Schedule.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			/*	String sched="";
				schedule=mainSchedule;				
				//Toast.makeText(getApplicationContext(),"name: "+name.getText()+"\nSched: "+schedule, Toast.LENGTH_LONG).show();
				while(true)
				{				
					if(schedule==null || schedule.equals("end"))
						break;									
					if(schedule.contains("/"))
					{
						int index=schedule.indexOf("/");
						String temp="",end="";
						temp+="\n"+schedule.substring(0, 3);
						end+=schedule.substring(4, index-1);
						sched+=temp;
						sched+="            "+end;
						//Toast.makeText(getApplicationContext(),"\nSched: "+sched, Toast.LENGTH_LONG).show();
						if((schedule=schedule.substring(index+1)).equals("/"))
							break;							
						//Toast.makeText(getApplicationContext(),"\nSched: "+schedule, Toast.LENGTH_LONG).show();
					}
					else
						break;
				}
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectedDoctor.this);								
				alertDialog.setIcon(R.drawable.schedule).setTitle("Schedule")
	            .setMessage("\n"+sched+"\n\n")	            
	            .setPositiveButton("Ok", null).show();
			}
		});*/
		button1=(Button)findViewById(R.id.homebitton);
		button1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(SelectedDoctor.this,DashboardActivity.class);
				startActivity(i);
			}
		});
		Intent intent=getIntent();
		String selectedValue=intent.getExtras().getString("doctor");
		nameOfDoctor=selectedValue;
		//Toast.makeText(getApplicationContext(), "Intent Name :  "+selectedValue, Toast.LENGTH_LONG).show();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");		
		query.whereEqualTo("Name", selectedValue);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> la,
					com.parse.ParseException e) {
				// TODO Auto-generated method stub
				 if(la!=null){
					 //Toast.makeText(getApplicationContext(),"Name get: "+la.get(0).getString("Name"), Toast.LENGTH_LONG).show();
					 nameOfDoctor=la.get(0).getString("Name");
					 name.setText("Dr. " + nameOfDoctor);
						spec.setText(la.get(0).getString("Job"));
						gender.setText(la.get(0).getString("Gender"));
						hospital.setText(la.get(0).getString("Hospital"));
						mainSchedule=la.get(0).getString("Scehdule");
						//Toast.makeText(getApplicationContext(),"name: "+la.get(0).getString("Name")+"\nSched: "+la.get(0).getString("Scehdule"), Toast.LENGTH_LONG).show();
						//timings.setText("9-5");
						experience.setMovementMethod(new ScrollingMovementMethod());
						experience.setText(la.get(0).getString("Qualifications"));
						ParseFile fileObject = (ParseFile) la.get(0).get("Images");
						fileObject.getDataInBackground(new GetDataCallback() {									
									@Override
									public void done(byte[] arg0,
											ParseException arg1) {
										// TODO Auto-generated method stub
										if (arg1 == null) {
											//Log.d("test","We've got data in data.");
											// Decode the Byte[] into
											// Bitmap
											Bitmap bmp = BitmapFactory.decodeByteArray(arg0, 0,arg0.length);
                                            BitmapDrawable b=new BitmapDrawable(getResources(),bmp);
											icon.setBackgroundDrawable(b);
    										//icon.setImageDrawable(b);
											//icon.setImageBitmap(bmp);
											
										}
										
									}
						
					
                         });						
						TextView time1=(TextView) findViewById(R.id.time1); 
						TextView time2=(TextView) findViewById(R.id.time2);
						TextView time3=(TextView) findViewById(R.id.time3);
						TextView time4=(TextView) findViewById(R.id.time4);
						TextView time5=(TextView) findViewById(R.id.time5);
						TextView time6=(TextView) findViewById(R.id.time6);
						TextView time7=(TextView) findViewById(R.id.time7);
						
						//Setting up schedule table
						String sched="";
						schedule=mainSchedule;				
						//Toast.makeText(getApplicationContext(),"name: "+name.getText()+"\nSched: "+schedule, Toast.LENGTH_LONG).show();
						while(true)
						{				
							if(schedule==null || schedule.equals("end"))
								break;									
							if(schedule.contains("/"))
							{
								int index=schedule.indexOf("/");
								String temp="",end="";
								temp+=schedule.substring(0, 3);
								end+=schedule.substring(4, index-1);
								if(temp.equals("Mon"))
								{
									time1.setText(end);
								}
								else if(temp.equals("Tue"))
								{
									time2.setText(end);
								}
								else if(temp.equals("Wed"))
								{
									time3.setText(end);
								}
								else if(temp.equals("Thu"))
								{
									time4.setText(end);
								}
								else if(temp.equals("Fri"))
								{
									time5.setText(end);
								}
								else if(temp.equals("Sat"))
								{
									time6.setText(end);
								}
								else if(temp.equals("Sun"))
								{
									time7.setText(end);
								}
								sched+=temp;
								sched+="            "+end;
								//Toast.makeText(getApplicationContext(),"\nSched: "+sched, Toast.LENGTH_LONG).show();
								if((schedule=schedule.substring(index+1)).equals("/"))
									break;							
								//Toast.makeText(getApplicationContext(),"\nSched: "+schedule, Toast.LENGTH_LONG).show();
							}
							else
								break;
						}						
						
				 }
                  else{//handle the error}
                	  //Toast.makeText(getApplicationContext(),"hello valuesdf", Toast.LENGTH_LONG).show();                	  
                            }
				
			}
		});
		appLocationService = new AppLocationService(SelectedDoctor.this);      
	      distance=5;
	      close=(Button) findViewById(R.id.close);
	      mMapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapFrag));		      					 					 	
		  close.setVisibility(View.GONE);
	      getSupportFragmentManager().beginTransaction().hide(mMapFragment).commit();
	     // mMapFragment.getView().setVisibility(View.INVISIBLE);
		//while(done==false);
		Intent getintent=getIntent();
		//Toast.makeText(getApplicationContext(), "Name of doctor:   "+ nameOfDoctor ,Toast.LENGTH_LONG).show();
		String speciality=(String) spec.getText();
		String gend= (String) gender.getText();
		query = ParseQuery.getQuery("DoctorsTable");
//		query.whereEqualTo("Gender", gend);
//  	query.whereEqualTo("Job", speciality);		
		query.whereEqualTo("Name", nameOfDoctor);
		query.findInBackground(new FindCallback<ParseObject>() {
		@Override
		public void done(List<ParseObject> la,
				com.parse.ParseException e) {
			
			// TODO Auto-generated method stub
			 if(la!=null)
			 {					 
				 for(int i=0;i<la.size()+1;i++)
					{
						 if(i==la.size())
						 {							 													    													
								break;							
						 }
						 else
						 {
					 //Toast.makeText(getApplicationContext(),la.get(i).getString("Job"), Toast.LENGTH_LONG).show();
						 String s=la.get(i).getString("Job");
						 String s1=la.get(i).getString("Name");
						 String s2=la.get(i).getString("Gender");
						 String hosp=la.get(i).getString("Hospital");
						 ParseGeoPoint userLocation = (ParseGeoPoint) la.get(i).get("Location");						 						
						 LatLng lat=new LatLng(userLocation.getLatitude(),userLocation.getLongitude());
						 Toast.makeText(getApplicationContext(), "Long adding:   "+userLocation.getLongitude()+"\nLat:  "+userLocation.getLatitude() ,Toast.LENGTH_LONG).show();
			        	 LatLngList.add(lat);        	 
			        	 nameList.add(s1);
			        	 hospitalList.add(hosp);
						// check(s1,s,s2);						 						 
						 //value+="\nName :  " +s1+"   Job :   "+s+"   Hosp :   "+hosp;
					// Toast.makeText(getApplicationContext(),"Hello " + s, Toast.LENGTH_LONG).show();
			//		     genderList1.add(s);		
						 }
					 };
					 //TextView text=(TextView) findViewById(R.id.textView1);												
					// text.setText(value+"\n\n End");
					 try {				         				       				 	 
				       	 //googleMap = mMapFragment.getMap();
				    	  
				    	  googleMap = mMapFragment.getMap();
				    	  //googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapFrag)).getMap();
				          googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				         //googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				         boolean verify=true;            
				         Location gpsLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);
				         if (gpsLocation != null) 
							{
								latitude = gpsLocation.getLatitude();
								longitude = gpsLocation.getLongitude();
								/*Toast.makeText(getApplicationContext(),
								"Mobile Location (GPS): \nLatitude: " + latitude 
								+ "\nLongitude: " + longitude ,
								Toast.LENGTH_LONG).show();		*/		
							}				
							else {
								Location gpsLoc = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
								if (gpsLoc != null) 
								{
									latitude = gpsLoc.getLatitude();
									longitude = gpsLoc.getLongitude();
								    /*Toast.makeText(getApplicationContext(),
									"Mobile Location (GPS): \nLatitude: " + latitude
									+ "\nLongitude: " + longitude,
									Toast.LENGTH_LONG).show();*/	
								}	
								else
								{
									verify=false;			
								}
							}
								//if(verify==false)
									//showSettingsAlert("GPS");  
								
					 		/*Toast.makeText(getApplicationContext(),
									"Your Location (GPS): \nLatitude: " + latitude
									+ "\nLongitude: " + longitude,
									Toast.LENGTH_LONG).show();*/	
				 		        
				     
				         //Toast.makeText(this, "The doctor nearby is " + doctorList.get(0).getname() +"   ", Toast.LENGTH_LONG).show();
						UiSettings sets=googleMap.getUiSettings();
						sets.setCompassEnabled(true);
						sets.setMyLocationButtonEnabled(true);
						sets.setZoomControlsEnabled(true);
						
						//googleMap.setMyLocationEnabled(true);
				         //LatLng myLoc=new LatLng(latitude,longitude);         
				         /*Marker currLoc = googleMap.addMarker(new MarkerOptions().
				    	         position(myLoc).title("My Location").
				    	         icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
				    	  */
						LatLng point=(com.google.android.gms.maps.model.LatLng) LatLngList.get(0);
				         CameraUpdate center=
				        	        CameraUpdateFactory.newLatLng(new LatLng(point.latitude,
				        	                                                 point.longitude));
				        	    CameraUpdate zoom=CameraUpdateFactory.zoomTo(12);
				        	    googleMap.moveCamera(center);
				        	    googleMap.animateCamera(zoom);                           
				         
				         Toast.makeText(getApplicationContext(), "OutLoop \nLong:   "+point.longitude+"\nLat:  "+point.latitude ,Toast.LENGTH_LONG).show();         
				         Toast.makeText(getApplicationContext(), "Size of array"+LatLngList.size() ,Toast.LENGTH_LONG).show();
				         for(int i=0;i<LatLngList.size();i++)
					      {
				        	 
				        	 Toast.makeText(getApplicationContext(), "Inside loop \nLong:   "+point.longitude+"\nLat:  "+point.latitude ,Toast.LENGTH_LONG).show();         
				        	// BitmapDescriptor bit;        	
				          	 //float color= (jobList.indexOf(doctorList.get(i).getjob())+1)*30;
				          	 //bit=BitmapDescriptorFactory.defaultMarker(color);   
				        	 //String snippet="Phone: "+ doctorList.get(i).getphone()+" \nGender: "+"\n "+        			 
				    		 //doctorList.get(i).getgender();
				        	 point=(com.google.android.gms.maps.model.LatLng) LatLngList.get(i);
				        	 Marker TP = googleMap.addMarker(new MarkerOptions().
				        	         position(point).title(nameList.get(i)).snippet(hospitalList.get(i)));
				        	        		 //.icon(BitmapDescriptorFactory.fromResource(R.drawable.dentist)  ));        	 
					      }	        		                      
				         //Marker TP2 = googleMap.addMarker(new MarkerOptions().
				         //position(TutorialsPoint2).title("Ali"));
				      }
				      catch (Exception exc) {
				         exc.printStackTrace();
				      }
			 }
	            else{//handle the error}
	          	  Toast.makeText(getApplicationContext(), "No doctors of these characteristics found", Toast.LENGTH_LONG).show();
	                      }
				 
				
			}
		});

		
	      
	      buttonMaps=(Button) findViewById(R.id.buttonMaps);
	 	  buttonMaps.setOnClickListener(new View.OnClickListener() {			
	 			@Override
	 			public void onClick(View v) {
	 				// TODO Auto-generated method stub
	 				//mMapFragment.getView().setVisibility(View.VISIBLE); 
	 				close.setVisibility(View.VISIBLE);
	 				getSupportFragmentManager().beginTransaction().show(mMapFragment).commit();
	 				//googleMap=mMapFragment.getMap();
	 			}
	 		});
	 	   	
	 	   
	 	   close.setOnClickListener(new View.OnClickListener() {			
	 			@Override
	 			public void onClick(View v) {
	 				// TODO Auto-generated method stub
	 				
	 				mMapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapFrag));	 				
	 				//mMapFragment.getView().setVisibility(View.INVISIBLE);	 					 			
	 				close.setVisibility(View.GONE);
	 				getSupportFragmentManager().beginTransaction().hide(mMapFragment).commit();
	 				/*FragmentManager fm = getSupportFragmentManager();
	 				android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
	 				ft.hide(googleMap).commit();*/
	 			}
	 		});   				      
	      
	}
	  public void showSettingsAlert(String provider) {
	 		AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectedDoctor.this);
	 		alertDialog.setTitle(provider + " SETTINGS");
	 		alertDialog.setMessage(provider + " is not enabled! Want to go to settings menu?");
	 		alertDialog.setPositiveButton("Settings",
	 		new DialogInterface.OnClickListener() {
	 			public void onClick(DialogInterface dialog, int which) {
	 				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	 				SelectedDoctor.this.startActivity(intent);
	 			}});

	 		alertDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
	 			public void onClick(DialogInterface dialog, int which) {
	 				dialog.cancel();
	 			}});
	 		alertDialog.show();
	 	}
		
		/*if(doc.gender.equals("male"))
		{
			icon.setBackgroundResource(R.drawable.doctor);
		}
		else if(doc.gender.equals("female"))
		{
			icon.setBackgroundResource(R.drawable.doctor);
		}
		icon.setClickable(false);
		/*call.setBackgroundResource(R.drawable.doctor);
		call.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {				
				// TODO Auto-generated method stub
				String num="";
		        num=doc.getphone();
		        String number = "tel:" + num.trim();				
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
			    startActivity(intent);
			}
		});*/
		
		//Toast.makeText(this, doc.getname(), Toast.LENGTH_SHORT).show();
	

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.selected_doctor, menu);
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
	}*/
	
	public void onBackPressed() {
      //  Intent intent = new Intent(this, DashboardActivity.class);
        //startActivity(intent);
        finish();

	} 
}
