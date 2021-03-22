package com.delpradosergio.orderbox.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.delpradosergio.orderbox.R;

public class Configuracion_Fragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_configuracion, container, false);

        ImageButton botonRetroceder;
        botonRetroceder = vista.findViewById(R.id.imageButtonRetrocederOpciones);
        botonRetroceder.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });



        return vista;
    }
}