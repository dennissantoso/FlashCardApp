package com.example.flashcardapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView flashcardQuestion;
    TextView flashcardAnswer;
    ImageButton flashcardNext;
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int currentCardDisplayedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardQuestion = findViewById(R.id.flashcard_question);
        flashcardAnswer = findViewById(R.id.flashcard_answer);
        flashcardNext = findViewById(R.id.flashcard_next);

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();

        if (allFlashcards != null && allFlashcards.size() > 0) {
            ((TextView) findViewById(R.id.flashcard_question)).setText(allFlashcards.get(0).getQuestion());
            ((TextView) findViewById(R.id.flashcard_answer)).setText(allFlashcards.get(0).getAnswer());
        }

        flashcardQuestion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                flashcardQuestion.setVisibility(View.INVISIBLE);
                flashcardAnswer.setVisibility(View.VISIBLE);
            }
        });

        flashcardAnswer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                flashcardAnswer.setVisibility(View.INVISIBLE);
                flashcardQuestion.setVisibility(View.VISIBLE);
            }
        });

        flashcardNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (allFlashcards.size() == 0)
                    return;

                currentCardDisplayedIndex++;

                if(currentCardDisplayedIndex >= allFlashcards.size()) {
                    currentCardDisplayedIndex = 0;
                }

                allFlashcards = flashcardDatabase.getAllCards();
                Flashcard flashcard = allFlashcards.get(currentCardDisplayedIndex);

                flashcardQuestion.setText(flashcard.getAnswer());
                flashcardAnswer.setText(flashcard.getQuestion());
            }
        });

        ImageButton addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            //get data
            if (data != null) {
                String questionString = data.getExtras().getString("QUESTION_KEY");
                String answerString = data.getExtras().getString("ANSWER_KEY");

                flashcardQuestion.setText(questionString);
                flashcardAnswer.setText(answerString);

                flashcardDatabase.insertCard(new Flashcard(questionString, answerString));
                allFlashcards = flashcardDatabase.getAllCards();
            }
        }
    }

}