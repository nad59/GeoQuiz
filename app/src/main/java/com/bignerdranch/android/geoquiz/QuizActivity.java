package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX="index";
    private static final String BANK_INDEX="bank_index";
    private static final String ANS_INDEX="ans_index";
    private static final String COR_INDEX="cor_index";
    private static final String CHEAT_INDEX="cheat_index";
    private static final int REQUEST_CODE_CHEAT = 0;
    private int cheats = 3;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private TextView mCheatText;
    private int answeredQuestions=0;
    private int correctAnswers=0;


    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate(Bundle)called");
        setContentView(R.layout.activity_quiz);

        if(savedInstanceState!=null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mQuestionBank= (Question[])savedInstanceState.getParcelableArray(BANK_INDEX);
            cheats = savedInstanceState.getInt(CHEAT_INDEX,0);
        }

        mCheatText=(TextView)findViewById(R.id.cheat_text);
        mCheatText.setText(cheats+"/3 Cheats Remaining");

        mQuestionTextView=(TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCurrentIndex=(mCurrentIndex+1) %mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton=(Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checkAnswer(true);
            }
        });
        mFalseButton=(Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               checkAnswer(false);
            }
        });

        mNextButton=(ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCurrentIndex=(mCurrentIndex+1) %mQuestionBank.length;
                updateQuestion();
            }
        });

        mPrevButton=(ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mCurrentIndex==0){
                    mCurrentIndex=mQuestionBank.length;
                }
                mCurrentIndex=(mCurrentIndex-1) %mQuestionBank.length;
                updateQuestion();
            }
        });

        mCheatButton = (Button)findViewById(R.id.cheat_button);
        if(cheats==0){
            mCheatButton.setEnabled(false);
        }
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        // Start CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        updateQuestion();
    }


    private void updateQuestion(){
        int question =mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        if (!mQuestionBank[mCurrentIndex].isAnswered()){
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
        else {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        }
    }

    private void checkAnswer(boolean userPressedTrue){
        answeredQuestions+=1;
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId=0;
        if (mQuestionBank[mCurrentIndex].isCheated()) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                correctAnswers += 1;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
            mQuestionBank[mCurrentIndex].setAnswered(true);
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
            Log.i(TAG, "Correct Answers: " + correctAnswers);
            Log.i(TAG, "Answered Questions: " + answeredQuestions);
            Log.i(TAG, "Length: " + mQuestionBank.length);
            if (answeredQuestions == mQuestionBank.length) {
                Toast.makeText(this, "You answered " + ((float) correctAnswers / answeredQuestions * 100) + "% correctly", Toast.LENGTH_SHORT).show();
            }
        }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG,"onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
        savedInstanceState.putParcelableArray(BANK_INDEX,mQuestionBank);
        savedInstanceState.putInt(COR_INDEX,correctAnswers);
        savedInstanceState.putInt(ANS_INDEX,answeredQuestions);
        savedInstanceState.putInt(CHEAT_INDEX,cheats);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
        return;
    }
    if (requestCode == REQUEST_CODE_CHEAT) {
        if (data == null) {
            return;
        }
        mQuestionBank[mCurrentIndex].setCheated(CheatActivity.wasAnswerShown(data));
        if(mQuestionBank[mCurrentIndex].isCheated()){
            cheats-=1;
            mCheatText.setText(cheats+"/3 Cheats Remaining");
            if(cheats==0){
                mCheatButton.setEnabled(false);

            }

        }
    }
}

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
