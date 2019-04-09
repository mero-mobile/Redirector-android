package com.bhagya.mapapplication.activityclass;
import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
        import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
        import android.widget.Toast;

        import com.androidquery.AQuery;
        import com.androidquery.callback.AjaxCallback;
        import com.androidquery.callback.AjaxStatus;
import com.bhagya.mapapplication.R;

import org.json.JSONArray;

import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Map;

public class ReadContactList extends Activity {
    DatabaseHelper databaseHelper;
    ListView listView ;
    ArrayList<String> StoreContacts ;
    ArrayAdapter<String> arrayAdapter ;
    AQuery aQuery;
    Cursor cursor ;
    String name, phonenumber,user_id;
    public  static final int RequestPermissionCode  = 1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_contact_list);
         listView = (ListView)findViewById(R.id.listview1);
         databaseHelper=new DatabaseHelper(this);
         aQuery=new AQuery(this);
//         StoreContacts =databaseHelper.getcontactdata();

        //user_id=profile.getId();
       //  EnableRuntimePermission();
         //GetContactsIntoArrayList();
         arrayAdapter = new ArrayAdapter<String>(
                ReadContactList.this,
                android.R.layout.simple_list_item_1
                , StoreContacts
         );

        //  ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,);
        listView.setAdapter(arrayAdapter);
    }
    public void GetContactsIntoArrayList(){
        SharedPreferences prfs = getSharedPreferences("new_userid",MODE_PRIVATE);
        user_id = prfs.getString("user_id","");
        String uploadurl="http://104.155.153.31/inserContact.php";
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);

        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            ContentValues cv=new ContentValues();
            cv.put("contactNo",phonenumber);
            cv.put("contactName",name);
          //  databaseHelper.Insertcaontactinfo(cv);
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("contactname",name);
            params.put("contactno",phonenumber);
            params.put("user_id",user_id);
            Toast.makeText(ReadContactList.this,"this is the user id"+user_id,Toast.LENGTH_LONG).show();
            aQuery.ajax(uploadurl,params, JSONArray.class, new AjaxCallback<JSONArray>() {
                @Override
                public void callback(String url, JSONArray array, AjaxStatus status) {
                    super.callback(url, array, status);
                    Log.i("response", "REsponser:" + array);
                }
            });


        }

        cursor.close();

    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                ReadContactList.this,
                Manifest.permission.READ_CONTACTS))
        {

            Toast.makeText(ReadContactList.this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(ReadContactList.this,new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(ReadContactList.this,"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ReadContactList.this,"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
}
