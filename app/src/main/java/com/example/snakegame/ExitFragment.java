package com.example.snakegame;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

// displays a button that allows the user to exit the application when the user clicks on the button,
// the user is returned to the main activity and the app is killed (in MainActivity)
public class ExitFragment extends Fragment {

    Button exitBtn1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_exit, container, false);

        exitBtn1 = view.findViewById(R.id.exitBtn1);

        exitBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent killApp = new Intent(getActivity(), MainActivity.class);
                killApp.putExtra("kill", true);
                startActivity(killApp);
            }
        });

        return view;
    }// onCreateView
}// ExitFragment