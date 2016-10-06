package com.example.aemacc13.picturematchinggame;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class GameFragment extends Fragment {

    //grid layout ids
    private static int ids[]= {R.id.pic00, R.id.pic01, R.id.pic02, R.id.pic03,
                               R.id.pic10, R.id.pic11, R.id.pic12, R.id.pic13,
                               R.id.pic20, R.id.pic21, R.id.pic22, R.id.pic23,
                               R.id.pic30, R.id.pic31, R.id.pic32, R.id.pic33};

    //original grid images
    private static ArrayList<ImageButton> pokeBalls= new ArrayList();


    //lists for the hidden images
    private static ArrayList<Drawable> allAvailableImages= new ArrayList();
    private static ArrayList<Drawable> roundAvailableImages= new ArrayList();
    private static ArrayList<Drawable> gridAvailableImages= new ArrayList();
    private static ArrayList<Drawable> gridCopyImages= new ArrayList();

    //lists for comparing two clicked images
    private static ArrayList<Drawable> clickedImages= new ArrayList();
    private static ArrayList<Integer> clickedIndices= new ArrayList();

    //counts
    private static int count;
    private static int previousCount;
    private static int round= 0;

    //grid views
    GridLayout grid= null;
    ImageButton pokeBall= null;
    LinearLayout count_layout= null;
    TextView counter= null;
    ImageView badge_image= null;

    //hidden views
    LinearLayout win= null;
    LinearLayout previous= null;
    LinearLayout badges= null;
    TextView previous_count= null;
    Button yes= null;
    Button no= null;

    //Sounds
    SoundPool soundPool= null;
    int openMusic= 0;
    int caughtMusic= 0;

    Handler handler= null;
    Drawable ball= null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        this.soundPool= new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        this.openMusic= this.soundPool.load(getContext(), R.raw.pokeball_opening,1);
        this.caughtMusic= this.soundPool.load(getContext(), R.raw.caught,1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_game, container, false);
        initImageSet();
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        previous.setVisibility(View.GONE);
    }

    public void initImageSet(){
        Resources resources= getResources();

        pokeBalls.clear();
        allAvailableImages.clear();
        roundAvailableImages.clear();
        gridAvailableImages.clear();
        gridCopyImages.clear();


        //save pokeball image... for when we reset the images
        ball= resources.getDrawable(resources.getIdentifier("poke_balls", "drawable", getActivity().getPackageName()));

        //fill the array list with all images
        for (int i= 0; i <= 19; i++){
            int id= resources.getIdentifier("poke" + i, "drawable", getActivity().getPackageName());
            allAvailableImages.add(resources.getDrawable(id));
        } Collections.shuffle(allAvailableImages);

        //get eight images to use
        for (int i= 0; i < 8; i++){
            roundAvailableImages.add(allAvailableImages.get(i));
        }

        //get two of each image
        for (int i= 0; i < 2; i++) {
            for (int j = 0; j < roundAvailableImages.size(); j++) {
                gridAvailableImages.add(roundAvailableImages.get(j));
            }Collections.shuffle(roundAvailableImages);
        }Collections.shuffle(gridAvailableImages);

        //create copy for keeping track of unmatched images
        for (int i = 0; i < gridAvailableImages.size(); i++) {
            gridCopyImages.add(gridAvailableImages.get(i));
        }
    }

    public void initViews(final View rootView) {
        count= 0;
        handler= new Handler();

        //hidden views
        win= (LinearLayout)rootView.findViewById(R.id.win);
        previous= (LinearLayout)rootView.findViewById(R.id.previous_count_layout);
        badges= (LinearLayout)rootView.findViewById(R.id.badges);
        previous_count= (TextView)rootView.findViewById(R.id.previous_count);
        yes= (Button)rootView.findViewById(R.id.yes);
        no= (Button)rootView.findViewById(R.id.no);

        //grid views
        grid= (GridLayout)rootView.findViewById(R.id.grid);
        counter= (TextView)rootView.findViewById(R.id.count);
        count_layout= (LinearLayout)rootView.findViewById(R.id.count_layout);
        badge_image= (ImageView)rootView.findViewById(R.id.badge_image);

        for (int i= 0; i < 16; i++) {
            final int index= i;

            pokeBall = (ImageButton)rootView.findViewById(ids[i]);
            pokeBalls.add(pokeBall);
            pokeBall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //this works but there's to much going on for itt to sound good
                    //soundPool.play(openMusic, 1f, 1f, 1, 0, 1);
                    flipAnimation(index);
                    pauseForFlip(index);
                    flip(index);
                    count++;
                    GameFragment.this.counter.setText(Integer.toString(count));

                    if (checkWin()){
                        //save last score
                        previousCount= count;
                        //hide grid
                        grid.setVisibility(View.GONE);
                        count_layout.setVisibility(View.GONE);
                        //reveal win screen
                        win.setVisibility(View.VISIBLE);
                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //show grid
                                grid.setVisibility(View.VISIBLE);
                                count_layout.setVisibility(View.VISIBLE);
                                //hide win screen
                                win.setVisibility(View.GONE);
                                //reset game
                                for(int index= 0; index < 16; index++)
                                    pokeBalls.get(index).setBackground(ball);
                                initImageSet();
                                initViews(getView());
                                //show badges
                                revealBadge(round, count, previousCount);
                                badge_image.setVisibility(View.VISIBLE);
                                //show previous score
                                previous.setVisibility(View.VISIBLE);
                                GameFragment.this.previous_count.setText(Integer.toString(previousCount));
                                GameFragment.this.counter.setText("");
                            }
                        });

                        no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                getActivity().startActivity(intent);
                            }
                        });
                    }
                }
            });
        }
    }

    public void flip(int index){
        //set background in pauseForFlip so that the poke ball flips and not the pokemon
        pokeBalls.get(index).setClickable(false);
        clickedImages.add(gridAvailableImages.get(index));
        clickedIndices.add(index);

        //two images have been clicked
        if (count % 2 == 1){
            ((GameActivity)getActivity()).startThinking();
            if(!compareImages(clickedImages.get(0), clickedImages.get(1))){
                //images don't match... reset images
                thinking();
            } else {
                //images match... don't reset images
                //remove images from list of unmatched images
                gridCopyImages.remove(clickedImages.get(0));
                gridCopyImages.remove(clickedImages.get(1));

                //clear lists for comparing images
                clickedImages.clear();
                clickedIndices.clear();

                soundPool.play(caughtMusic, 1f, 1f, 1, 0, 1);

                ((GameActivity)getActivity()).stopThinking();
            }
        }
    }

    public void flipBack(){
        //reset images to pokeballs
        pokeBalls.get(clickedIndices.get(0)).setBackground(ball);
        pokeBalls.get(clickedIndices.get(1)).setBackground(ball);

        //make images clickable aagain
        pokeBalls.get(clickedIndices.get(0)).setClickable(true);
        pokeBalls.get(clickedIndices.get(1)).setClickable(true);

        //clear lists for comparing images
        clickedImages.clear();
        clickedIndices.clear();
    }

    public void flipAnimation(int index){
        Animator animator= AnimatorInflater.loadAnimator(getContext(), R.animator.animate_flip);
        animator.setTarget(pokeBalls.get(index));
        animator.start();
        pokeBalls.get(index).setBackground(ball);

    }

    public void revealBadge(int round, int count, int previousCount){
        //add round assuming task was successful
        round= ++this.round;

        Resources resources= getResources();
        int id= resources.getIdentifier("badged" + round, "drawable", getActivity().getPackageName());

        //goal for each round
        if (round == 1 && count <= 100){
            badge_image.setBackground(resources.getDrawable(id));
            Toast.makeText(getContext(), "Find all the matches in less than 75 clicks to earn the next gym badge", Toast.LENGTH_LONG).show();
        } else if (round == 2 && count <= 75){
            badge_image.setBackground(resources.getDrawable(id));
            Toast.makeText(getContext(), "Find all the matches in less than 50 clicks to earn the next gym badge", Toast.LENGTH_LONG).show();
        } else if (round == 3 && count <= 50){
            badge_image.setBackground(resources.getDrawable(id));
            Toast.makeText(getContext(), "Find all the matches in less than 40 clicks to earn the next gym badge", Toast.LENGTH_LONG).show();
        } else if (round == 4 && count <= 40){
            badge_image.setBackground(resources.getDrawable(id));
            Toast.makeText(getContext(), "Find all the matches in less than 30 clicks to earn the next gym badge", Toast.LENGTH_LONG).show();
        } else if (round == 5 && count <= 30){
            badge_image.setBackground(resources.getDrawable(id));
            Toast.makeText(getContext(), "Find all the matches in less than 25 clicks to earn the next gym badge", Toast.LENGTH_LONG).show();
        } else if (round == 6 && count <= 25){
            badge_image.setBackground(resources.getDrawable(id));
            Toast.makeText(getContext(), "Find all the matches in less than 20 clicks to earn the next gym badge", Toast.LENGTH_LONG).show();
        } else if (round == 7 && count <= 20){
            badge_image.setBackground(resources.getDrawable(id));
            Toast.makeText(getContext(), "Find all the matches in exactly 16 clicks to earn the final gym badge", Toast.LENGTH_LONG).show();
        } else if (round == 8 && count == 16){
            badge_image.setBackground(resources.getDrawable(id));
            Toast.makeText(getContext(), "Perfect Score!" + count, Toast.LENGTH_LONG).show();
            ((GameActivity)getActivity()).stoptBattleMusic();
            ((GameActivity)getActivity()).startWinMusic();
        } else {
            this.round--;
        }
    }

    public boolean compareImages(Drawable one, Drawable two){
        //did we pick matching images?
        return one.getConstantState().equals(two.getConstantState());
    }

    public boolean checkWin(){
        //did we match all of the images?
        return gridCopyImages.isEmpty();
    }

    public void thinking() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null) return;
                flipBack();
                ((GameActivity)getActivity()).stopThinking();
            }
        },1000);
    }

    public void pauseForFlip(int i) {
        final int index= i;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null) return;
                pokeBalls.get(index).setBackground(gridAvailableImages.get(index));
            }
        },200);
    }

}