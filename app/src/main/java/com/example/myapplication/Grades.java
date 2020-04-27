package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Grades extends AppCompatActivity {

    ArrayList<GradeModel> grades = new ArrayList<GradeModel>();
    //metoda wywoływana po wczytaniu aktywności
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);
        //pobranie elementów przesłanych z aktywności MainActivity
        Bundle bundleIn = getIntent().getExtras();
        final int numberOfGrades = bundleIn.getInt("gradeNumber");
        //stworzenie obiektów modelu ocen oraz ustawienie ich wartosci początkowych
        for (int i = 0; i < numberOfGrades; i++) {
            GradeModel grade = new GradeModel("ocena " + (i + 1));
            grade.setValue(2);
            grades.add(grade);
        }


        //Lączenie list i danych za pomoca adaptera
        final InteractiveArrayAdapter adapter = new InteractiveArrayAdapter(this, grades);
        ListView gradeList = (ListView) findViewById(R.id.gradeList);
        gradeList.setAdapter(adapter);
        //ustawienie click listenera dla przycisku powrotu do aktywności main
        findViewById(R.id.buttonOut).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //stworzenie obiektu klasy bundle przechowującej dane oraz dodanie danych
                        Bundle tobolek = new Bundle();
                        tobolek.putFloat("avarage", getAverage());
                        //wywołanie powrotu do aktywności main
                        Intent intent = new Intent();
                        intent.putExtras(tobolek);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
        );
    }
    //metoda zapisująca dane przed przekręceniem telefonu
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //stworzenie tablicy w którą zostaną wpisane wartości z formularza z pól radiobutton
        int[] gradeTab=new int[grades.size()];
        for(int i=0;i<gradeTab.length;i++){
            gradeTab[i]=grades.get(i).getValue();
        }
        //zapis wartosci
        outState.putIntArray("grades",gradeTab);
    }
    //motoda wczytująca dane po obróceniu telefonu
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //przypisanie do tablicy uprzednio zapisanych wartosci
        int[] gradeTab=savedInstanceState.getIntArray("grades");
        //przypisanie wartosci odpowiadajacym im obiektom
        for(int i=0;i<gradeTab.length;i++){
            grades.get(i).setValue(gradeTab[i]);
        }
        //Lączenie list i danych za pomocą adaptera
        final InteractiveArrayAdapter adapter = new InteractiveArrayAdapter(this, grades);
        ListView gradeList = (ListView) findViewById(R.id.gradeList);
        gradeList.setAdapter(adapter);
    }
    //policzenie sredniej ocen
    protected float getAverage() {
        float tmpSum = 0;
        for (int i = 0; i < grades.size(); i++) {
            tmpSum += grades.get(i).getValue();
        }
        return (float)tmpSum / (float)grades.size();
    }
}
