package com.delpradosergio.orderbox.Fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.delpradosergio.orderbox.R;


public class MenuPrincipal_Fragment extends Fragment{

    CardView cardBotonAddPedido, cardBotonVerPedidos;
    ImageButton botonConfiguracion;

    public MenuPrincipal_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_menu_principal, container, false);

        cardBotonAddPedido=vista.findViewById(R.id.cardViewAddPedidos);
        cardBotonVerPedidos=vista.findViewById(R.id.cardViewVerPedidos);
        botonConfiguracion=vista.findViewById(R.id.imageButtonConfiguracionMain);


        //Pongo la V en aquellos metodos que son obvios y solo tienen un metodo posible. Así se obvia todo el codigo de void OnClick{.....}
        cardBotonAddPedido.setOnClickListener(v->NavHostFragment.findNavController(this).navigate(R.id.action_mainFragment_to_addPedidos));
        cardBotonVerPedidos.setOnClickListener(v->NavHostFragment.findNavController(this).navigate(R.id.action_mainFragment_to_verPedidos));
        botonConfiguracion.setOnClickListener(v-> NavHostFragment.findNavController(this).navigate(R.id.action_mainFragment_to_opcionesFragment));

        return vista;
    }

}
