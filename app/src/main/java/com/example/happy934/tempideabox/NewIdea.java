package com.example.happy934.tempideabox;


import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewIdea extends Fragment implements View.OnClickListener{

    private boolean isFabOpen;
    FloatingActionButton mainFab, textFab, speechFab;
    Animation fab_open,fab_close,rotate_forward,rotate_backward;

    private final int REQ_CODE_SPEECH_INPUT = 100;

    public NewIdea() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_idea, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
//        View view = getView();

        //Initialize the  floating action buttons
        mainFab = (FloatingActionButton) getView().findViewById(R.id.mainAddFab);
        textFab = (FloatingActionButton) getView().findViewById(R.id.textInputButton);
        speechFab = (FloatingActionButton) getView().findViewById(R.id.speechInputButton);

        //Initialize the Animations
        fab_open = AnimationUtils.loadAnimation(getContext(),R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getContext(),R.anim.fab_rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(),R.anim.fab_rotate_backward);

        //set Listeners
        mainFab.setOnClickListener(this);
        textFab.setOnClickListener(this);
        speechFab.setOnClickListener(this);
    }

    public void onClick(View view){
        Intent i;
        int id = view.getId();
        switch (id){
            case R.id.mainAddFab:
                animateFab();
                break;
            case R.id.textInputButton:
                i = new Intent(getActivity(),KeyBoardInput.class);
                animateFab();
                startActivity(i);
                break;
            case R.id.speechInputButton:
                promptSpeechInput();
        }
    }
    public void animateFab(){
        if (isFabOpen){
            mainFab.startAnimation(rotate_backward);
            textFab.startAnimation(fab_close);
            speechFab.startAnimation(fab_close);

            textFab.setClickable(false);
            speechFab.setClickable(false);

            isFabOpen = false;
        }else{
            mainFab.startAnimation(rotate_forward);
            textFab.startAnimation(fab_open);
            speechFab.startAnimation(fab_open);

            textFab.setClickable(true);
            speechFab.setClickable(true);

            isFabOpen = true;
        }
    }

    private void promptSpeechInput(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,R.string.speechPrompt);

        try{
            startActivityForResult(intent,REQ_CODE_SPEECH_INPUT);
        }catch (Exception e){

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && data != null){
                    List<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    Toast.makeText(getContext(),result.get(0),Toast.LENGTH_SHORT).show();
                    Intent newIdeaIntent = new Intent(getContext(),KeyBoardInput.class);
                    newIdeaIntent.putExtra("description",result.get(0));
                    startActivity(newIdeaIntent);
                }
        }
    }

}
