package com.example.android.quiz4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.Toolbar;

import icepick.Icepick;
import icepick.State;

public class MainActivity extends AppCompatActivity {
    int numQuestions = 6;  //make sure eack check box is counted as one answer
    RadioButton radioButton;
    CheckBox checkBox, q2Chk1, q2Chk2, q2Chk3, q2Chk4, q2Chk5;

    @State
    int numAnswered = 0;    //tally answers
    @State
    double numCorrect = 0;
    @State
    int progress = 0;
    @State
    int q1A, q2A, q4A;
    @State
    String q3A;
    @State
    String msg = "";
    @State
    String results;
    @State
    int questionNumber = 1;
    @State
    int lastQuestion = 0;
    @State
    EditText q3Answer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);  //icepick automatically handles bundling on orientation
        setContentView(R.layout.activity_main);
        ProgressBar status = findViewById(R.id.progress_bar);
        status.setProgress(0);
        status.setMax(numQuestions);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    public void btnSubmit(View view) {

        if (questionNumber >= numQuestions) {
            //grade Q2 checkBox
            //read checkBox states into variables - ?? can I use arrays on check & radios?
            q2Chk1 = findViewById(R.id.c1q2);  //y
            q2Chk2 = findViewById(R.id.c2q2);
            q2Chk3 = findViewById(R.id.c3q2);   //y
            q2Chk4 = findViewById(R.id.c4q2);   //y
            q2Chk5 = findViewById(R.id.c5q2);
            if (q2Chk1.isChecked() && !q2Chk2.isChecked() && q2Chk3.isChecked() && q2Chk4.isChecked() && !q2Chk5.isChecked())
                numCorrect++;

            //grade Q3 EditText
            q3Answer = findViewById(R.id.q3a1); //get ID of Q3 EditText
            String q3txt = q3Answer.getText().toString();
            String q3AnsTxt = getResources().getString(R.string.q3Ans1);
            if (q3txt.equals(q3AnsTxt)) {
                numCorrect++;
                updateProgress(3);
                //Toast.makeText(this, q3txt + " + " + q3AnsTxt, Toast.LENGTH_SHORT).show();
                msg = msg + "\n3 - " + q3txt + " is right!";
            } else {
                msg = msg + "\n3 - " + q3txt + " is wrong, it's Mario!";
                updateProgress(3);
            }

            //calculate & return results
            double grade = numCorrect / numQuestions * 100;
            msg = msg + "\nScore  = " + grade + "% + " + numCorrect + " of " + numQuestions; //using R.string.txtScore returns ID, not text
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            //email();
            reset();
        } else {
            Toast.makeText(this, R.string.answerAll, Toast.LENGTH_LONG).show();
        }
    }

    public void btnReset(View view) {
        reset();
    }


    private void reset() {  //reset variables
        setContentView(R.layout.activity_main);//reload screen
        numCorrect = 0;
        numAnswered = 0;
        progress = 0;
        q1A = 0;
        q2A = 0;
        q4A = 0;
        q3A = "";
        msg = "";
        results = "";
        lastQuestion = 0;
        questionNumber = 0;
        ProgressBar status = findViewById(R.id.progress_bar);
        status.setProgress(0);
    }

    /* capture view.ID of rdo.answers for processing */

    public void onRdoClicked(View view) {    //routine for radio buttons
        q1A = view.getId();
        calcBtnAnswer(q1A);
        updateProgress(questionNumber); // should hard-coded param questionNumber because of BUG in updateProgress
    }

    public void onChkClicked(View view) {    //routine for radio buttons
        q2A = view.getId();
        calcChkAnswer(q2A);
        updateProgress(2); // should hard-coded param questionNumber because of BUG in updateProgress

    }

    private void email() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"jr@robitaille.us"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Qui 3");
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        startActivity(Intent.createChooser(intent, "Title of the chooser dialog"));
    }

    /*  UPDATE PROGRESS BAR - **!! BUG  !!**  - click off and back on question increments progress */
    public void updateProgress(int i) {
        if (i != lastQuestion) {
            progress++;
            ProgressBar status = findViewById(R.id.progress_bar);
            status.setProgress(progress);
            lastQuestion = i;
            questionNumber++;
        }
    }

    private void calcChkAnswer(int i) {  //i is answer's view.ID
        checkBox = findViewById(i);
        String s = checkBox.getHint().toString();
        if (s.equals("y")) {
            //msg = msg + "\nQuestion  " + questionNumber + " * " + " + " + checkBox.getText().toString() + " is Correct!";
            numCorrect++;
        } else {
            msg = msg + "\nQuestion " + questionNumber + " " + checkBox.getText().toString() + " was Incorrect!";
        }
    }//end calc answer

    private void calcBtnAnswer(int i) {  //i is answer's view.ID
        radioButton = findViewById(i);
        String s = radioButton.getHint().toString();
        if (s.equals("y")) {
            msg = msg + "\nQuestion  " + questionNumber + " * " + " + " + radioButton.getText().toString() + " is Correct!";
            numCorrect++;
        } else {
            msg = msg + "\nQuestion " + questionNumber + " " + radioButton.getText().toString() + " was Incorrect!";
        }
        numAnswered++; //update answers
    }//end calc answer
}//end Main Activity

