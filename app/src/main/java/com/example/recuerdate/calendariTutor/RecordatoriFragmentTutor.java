package com.example.recuerdate.calendariTutor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.recuerdate.ChatTutorFragment;
import com.example.recuerdate.R;

public class RecordatoriFragmentTutor extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recordatori_tutor, container, false);
    }
}
