package com.bhagya.mapapplication.activityclass;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bhagya.mapapplication.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class RequestsenderActivityFragment extends Fragment {

    public RequestsenderActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_requestsender, container, false);
    }
}
