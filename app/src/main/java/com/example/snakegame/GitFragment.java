package com.example.snakegame;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

// displays an image of the GitHub logo and when the user clicks on the image,
// the app opens a web browser and loads the GitHub repository of this project
public class GitFragment extends Fragment {

    ImageView gitIV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_git, container, false);

        gitIV = view.findViewById(R.id.gitIV);

        gitIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // creates an intent to view a web page and sets the data of the intent to the URL of the GitHub repository
                Intent gitHub = new Intent();
                gitHub.setAction(Intent.ACTION_VIEW);
                gitHub.addCategory(Intent.CATEGORY_BROWSABLE);
                gitHub.setData(Uri.parse("https://github.com/Crocodilu/SnakeGame"));
                startActivity(gitHub);
            }
        });

        return view;
    }// onCreateView
}// GitFragment