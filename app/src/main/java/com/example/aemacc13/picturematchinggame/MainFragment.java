package com.example.aemacc13.picturematchinggame;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class MainFragment extends Fragment {

    TextView poke_pic= null;
    Button new_button= null, continue_button= null, about_button= null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_main, container, false);

        this.poke_pic= (TextView)rootView.findViewById(R.id.poke_pic);
        this.new_button= (Button)rootView.findViewById(R.id.new_button);
        //this.continue_button= (Button)rootView.findViewById(R.id.continue_button);
        this.about_button= (Button)rootView.findViewById(R.id.about_button);

        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GameActivity.class);
                getActivity().startActivity(intent);
            }
        });

        about_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).showAbout();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }
}
