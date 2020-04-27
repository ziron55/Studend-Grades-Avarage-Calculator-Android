package com.example.myapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

public class InteractiveArrayAdapter extends ArrayAdapter<GradeModel> {

    //przechowujemy referencję do listy ocen
    private List<GradeModel> listaOcen;
    private Activity context;
    public InteractiveArrayAdapter(Activity kontekst, List<GradeModel> listaOcen)
    {
        super(kontekst, R.layout.grades_group, listaOcen);
        this.context=kontekst;
        this.listaOcen=listaOcen;
    }
    //tworzenie nowego wiersza
    @Override
    public View getView(int numerWiersza, View widokDoRecyklingu, ViewGroup parent)
    {
        View widok = null;
        //tworzenie nowego wiersza
        if (widokDoRecyklingu == null)
        {
            //utworzenie layout na podstawie pliku XML
            LayoutInflater pompka = context.getLayoutInflater();
            widok = pompka.inflate(R.layout.grades_group, null);
            widok.setVisibility(View.VISIBLE);

            //wybranie konkretnego przycisku radiowego musi zmieniać dane w modelu
            RadioGroup grupaOceny = (RadioGroup) widok.findViewById(R.id.gradesRadioGroup);
            final View finalWidok = widok;
            grupaOceny.setOnCheckedChangeListener(
                    new RadioGroup.OnCheckedChangeListener()
                    {
                        @Override
                        public void onCheckedChanged(RadioGroup group, //referencja do grupy
                                                     //przycisków
                                                     int checkedId) //identyfikator wybranego
                        //przycisku
                        {
                            // 1) odczytanie z etykiety, który obiekt modelu przechowuje dane o
                            //zmienionej ocenie
                            GradeModel model = (GradeModel) group.getTag();
                            // 2) zapisanie zmienionej oceny

                            RadioButton checked = (RadioButton) finalWidok.findViewById(group.getCheckedRadioButtonId());
                            model.setValue(Integer.valueOf(checked.getText().toString()));
                        }
                    }
            );
            //powiązanie grupy przycisków z obiektem w modelu
            grupaOceny.setTag(listaOcen.get(numerWiersza));
        }
        //aktualizacja istniejącego wiersza
        else
        {
            widok = widokDoRecyklingu;
            RadioGroup grupaOceny = (RadioGroup) widok.findViewById(R.id.gradesRadioGroup);
            //powiązanie grupy przycisków z obiektem w modelu
            grupaOceny.setTag(listaOcen.get(numerWiersza));
            GradeModel model = (GradeModel) grupaOceny.getTag();
        }

        TextView etykieta = (TextView) widok.findViewById(R.id.label);
        //ustawienie tekstu etykiety na podstawie modelu
        etykieta.setText(listaOcen.get(numerWiersza).getName());

        RadioGroup grupaOceny = (RadioGroup) widok.findViewById(R.id.gradesRadioGroup);
        //zaznaczenie odpowiedniego przycisku na podtawie modelu
        switch (listaOcen.get(numerWiersza).getValue()) {
            case 2:
                grupaOceny.check(R.id.grade2);
                break;

            case 3:
                grupaOceny.check(R.id.grade3);
                break;

            case 4:
                grupaOceny.check(R.id.grade4);
                break;

            case 5:
                grupaOceny.check(R.id.grade5);
                break;
        }
        //zwrócenie nowego lub zaktualizowanego wiersza listy
        return widok;
    }

}
