package com.example.snakegame;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GitFragment extends Fragment {

    ImageView gitIV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_git, container, false);

        gitIV = view.findViewById(R.id.gitIV);

        gitIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gitHub = new Intent();
                gitHub.setAction(Intent.ACTION_VIEW);
                gitHub.addCategory(Intent.CATEGORY_BROWSABLE);
                gitHub.setData(Uri.parse("https://github.com/Crocodilu/CalculMobil_Laboratoare"));
                startActivity(gitHub);
            }
        });

        return view;
    }
}