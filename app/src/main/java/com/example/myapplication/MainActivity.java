package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //metoda wywoływana po uruchomieniu aplikacji
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //wywołanie metod walidujących
        validateTextPolls();
        validateGradesNumber();
        final EditText gradeNumber = (EditText) findViewById(R.id.gradeNumber_input);
        //podłączenie obsługi zdarzeń dla przycisku
        findViewById(R.id.grades_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //wywołanie aktywności Grades
                        Intent grades = new Intent(MainActivity.this,Grades.class);
                        //przekazanie wartości do aktywności grades
                        int numberOfGrades = Integer.valueOf(gradeNumber.getText().toString());
                        grades.putExtra("gradeNumber",numberOfGrades);
                        int returned = 0;
                        startActivityForResult(grades,returned);
                    }
                }
        );
    }
    //zapis danych podczas obrotu telefonu
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //zapis średniej przed obrotem telefonu
        outState.putFloat("avarage",this.result);
    }
    //ponowne wczytanie danych po obroceniu telefonu
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //przypisanie sredniej po obróceniu telefonu
        this.result=savedInstanceState.getFloat("avarage");
        //przypisanie referencji
        TextView EText = (TextView) findViewById(R.id.avg);
        EText.setText("Srednia studenta to:" + this.result);
        EText.setVisibility(View.VISIBLE);
        //ustawienie dostępności pól na niedostępne
        findViewById(R.id.firstName_input).setEnabled(false);
        findViewById(R.id.surname_input).setEnabled(false);
        findViewById(R.id.gradeNumber_input).setEnabled(false);
        //odwołanie do przycisku oceny
        Button button = (Button)findViewById(R.id.grades_button);
        if(result>=3){
            //ustawienie nowego tekstu na przycisku oraz wykonania nowego zdarzenia po jego wciśnięciu
            button.setText("Super :)");
            button.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showToast("Gratulacje! Otrzymujesz zaliczenie!");
                            finish();
                        }
                    }
            );
        }
        else
        {
            button.setText("Tym razem nie poszło");
            button.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showToast("Wysyłam podanie o zaliczenie warunkowe");
                            finish();
                        }
                    }
            );
        }
    }
    //walidacja danych z formularza
    private final boolean dataValidation(){
        //ustawienie odwołań do pól formularza
            EditText gradeNumber = (EditText) findViewById(R.id.gradeNumber_input);
            EditText[] pools = {findViewById(R.id.firstName_input),findViewById(R.id.surname_input)};
            //sprawdzenie czy pole nie jest puste
            if(gradeNumber.getText().toString().isEmpty()) {
                return false;
            }
            //sprawdzenie czy podana wartość mieści się w przedziale od 5 do 15
            else if (Integer.valueOf(gradeNumber.getText().toString())>=5 && Integer.valueOf(gradeNumber.getText().toString())<=15){
                    if(!pools[0].getText().toString().isEmpty() && !pools[1].getText().toString().isEmpty()) {
                        return true;
                    }
                    else return false;
            }
            else {
                return false;
            }



        }
        //walidacja oraz dodanie listenerow pól tekstowych
        private void validateTextPolls(){
        //ustawienie odwołań do pól formularza
            final EditText[] pools = {findViewById(R.id.firstName_input),
                                findViewById(R.id.surname_input)};
            //ustawienie obsługi zdarzeń zmiany tekstu dla formularza
            for (final EditText e:pools) {
                e.addTextChangedListener(
                        new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                //walidacja oraz wyświetlenie komunikatow po wpisaniu tekstu w formularz
                                if(e.getText().toString().isEmpty()){
                                    findViewById(R.id.grades_button).setVisibility(View.INVISIBLE);
                                    showToast("Pola nie mogą być puste!");
                                    e.setError("Pole nie może być puste!");
                            }
                                else
                                {   //ustawienie przycisku button w stan widoczny lub nie widoczny po uprzedniej walidacji
                                    if(dataValidation()){
                                        findViewById(R.id.grades_button).setVisibility(View.VISIBLE);
                                    }
                                    else
                                    {
                                        findViewById(R.id.grades_button).setVisibility(View.INVISIBLE);

                                    }
                                }
                        }
                        }
                );
            }
        }
        //walidacja oraz dodanie listenerów pola z liczbą ocen
        private void validateGradesNumber(){
        //ustawienie odwołania do pola
        final EditText gradeNumber = (EditText)findViewById(R.id.gradeNumber_input);
        //ustawienie obsługi zdarzeń dla zmiany tekstu w formularzu
        gradeNumber.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        //walidacja oraz ustawienie dostępności do przyciusku oceny
                        if(dataValidation()){
                            findViewById(R.id.grades_button).setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            gradeNumber.setError("Podaj liczbe z przedziału[5-15]");
                            findViewById(R.id.grades_button).setVisibility(View.INVISIBLE);
                        }
                    }
                }
        );
    }

    float result; //srednia ocen
    //pobranie danych z Grades Activity oraz wyświetlenie średniej ocen
    protected void onActivityResult(int kodZadania, int kodWyniku, Intent dane) {
        super.onActivityResult(kodZadania, kodWyniku, dane);
        //sprawdzenie stanu zwróconego przez aktywność Grades
        if (kodWyniku == RESULT_OK) {
            //utworzenie obiektu klasy Bundle w celu pobrania wartości z aktywności Grades
            Bundle tobolek = dane.getExtras();
            this.result = tobolek.getFloat("avarage");
            //utworzenie odwołania do pola formularza
            TextView EText = (TextView) findViewById(R.id.avg);
            EText.setText("Srednia studenta to:" + this.result);
            EText.setVisibility(View.VISIBLE);
            //ustawienie pól formularza na niedostępne
            findViewById(R.id.firstName_input).setEnabled(false);
            findViewById(R.id.surname_input).setEnabled(false);
            findViewById(R.id.gradeNumber_input).setEnabled(false);
            //ustawienie odwołania do przycisku oraz zmiana aktywności i wpisanego tekstu
            Button button = (Button)findViewById(R.id.grades_button);
            if(result>=3){
                button.setText("Super :)");
                button.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showToast("Gratulacje! Otrzymujesz zaliczenie!");
                                finish();
                            }
                        }
                );
            }
            else
            {
                button.setText("Tym razem nie poszło");
                button.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showToast("Wysyłam podanie o zaliczenie warunkowe");
                                finish();
                            }
                        }
                );
            }
        }

    }
    //wyświetlenie komunikatu Toast
    void showToast(String massage){
        Toast toast = Toast.makeText(this,massage,Toast.LENGTH_SHORT);
        toast.show();
    }
}
