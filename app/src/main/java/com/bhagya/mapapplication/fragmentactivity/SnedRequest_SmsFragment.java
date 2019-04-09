package com.bhagya.mapapplication.fragmentactivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bhagya.mapapplication.R;
import com.bhagya.mapapplication.activityclass.read_Incomingsms;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Bhagya on 7/4/2017.
 */
public class SnedRequest_SmsFragment extends Fragment {
Button turnonwifi,ringphone,findmyphone;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.sendrequest_smsfragment,null);
        turnonwifi= (Button) view.findViewById(R.id.tunonwifi_sms);
        ringphone= (Button) view.findViewById(R.id.ringmyphone_sms);
        findmyphone= (Button) view.findViewById(R.id.findmyphone_sms);

        findmyphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences userpref = getActivity().getSharedPreferences("CURRENT_USER_RECORD",MODE_PRIVATE);
                String respondedUserMobileNo = userpref.getString("CurrentUser_mobile_no","");
                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage(respondedUserMobileNo, null,"findmyphone:test", null,null);
            }
        });
        turnonwifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences userpref = getActivity().getSharedPreferences("CURRENT_USER_RECORD",MODE_PRIVATE);
                String respondedUserMobileNo = userpref.getString("CurrentUser_mobile_no","");
                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage(respondedUserMobileNo, null,"turnonwifi:test", null,null);
            }
        });
        ringphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences userpref = getActivity().getSharedPreferences("CURRENT_USER_RECORD",MODE_PRIVATE);
                String respondedUserMobileNo = userpref.getString("CurrentUser_mobile_no","");
                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage(respondedUserMobileNo, null,"playmp3:test", null,null);
            }
        });

        return view;
    }
}
