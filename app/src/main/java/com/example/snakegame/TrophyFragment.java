package com.example.snakegame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TrophyFragment extends Fragment {

    ImageView trophyIV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trophy, container, false);

        trophyIV = view.findViewById(R.id.trophyIV);

        final MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.applause);

        trophyIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.start();
            }
        });

        return view;
    }
}