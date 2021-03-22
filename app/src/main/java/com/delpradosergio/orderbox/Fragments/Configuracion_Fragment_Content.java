package com.delpradosergio.orderbox.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delpradosergio.orderbox.Database.ViewModelCarnitronic;
import com.delpradosergio.orderbox.R;

import java.util.Locale;

/*
Clase que se encargará de dar utilidad a los botones de la ventana de la configuración
 */

public class Configuracion_Fragment_Content extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_configuracion_content, rootKey);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        ViewModelCarnitronic viewModel = new ViewModelProvider(requireActivity()).get(ViewModelCarnitronic.class);



        //Abrir direccion url
        Preference califica = findPreference("CalificaID");
        califica.setOnPreferenceClickListener(v-> {
            String url = "http://play.google.com/store/apps/details?id=com.delpradosergio.orderbox";
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            //Comprobar si la app esta activa
            if(intent.resolveActivity(getActivity().getPackageManager())!=null){
                startActivity(intent);
            }

            return true;
        });

        Preference borrarTodosLosPedidos = findPreference("BorrarListaPedidosID");
        borrarTodosLosPedidos.setOnPreferenceClickListener(v->{
            NavHostFragment.findNavController(this).navigate(R.id.action_opcionesFragment_to_confirmarBorrarTodosLosPedidos2);
            return true;
        });

        Preference enviarSugerencias = findPreference("ErroresID");
        enviarSugerencias.setOnPreferenceClickListener(v-> {

            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "sdelprado01@gmail.com", null));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Sugerencia o Error en la Aplicacion OrderBox");
                startActivity(intent);
            return true;
                });

        Preference cambiarIdioma = findPreference("IdiomaID");
        cambiarIdioma.setOnPreferenceClickListener(v-> {
            showCambiarIdiomaDialog();
            return true;
        });

        return view;
    }

    //Muestra un dialogo para seleccionar el idioma que quieres
    private void showCambiarIdiomaDialog(){
        String[] listaIdiomas = {"Español", "English"};
        AlertDialog.Builder dialogo= new AlertDialog.Builder(getActivity());
        dialogo.setTitle(R.string.configuracion_Idioma);
        dialogo.setSingleChoiceItems(listaIdiomas, -1, (dialog, which) -> {
            switch (which){
                case 0:
                    Locale idioma_es = new Locale("es", "ES");
                    Locale.setDefault(idioma_es);
                    Configuration config_es = new Configuration();
                    config_es.locale = idioma_es;
                    getContext().getResources().updateConfiguration(config_es, getContext().getResources().getDisplayMetrics());
                    refresh();
                    break;

                case 1:
                    Locale idioma_en = new Locale("en", "EN");
                    Locale.setDefault(idioma_en);
                    Configuration config_en = new Configuration();
                    config_en.locale = idioma_en;
                    getContext().getResources().updateConfiguration(config_en, getContext().getResources().getDisplayMetrics());
                    refresh();
                    break;
            }

        });


    }

    //Refresca el fragment despues de cambiar el idioma
   private void refresh(){
       Fragment fragment = null;
       fragment = getFragmentManager().findFragmentByTag("Configuracion_Fragment_Content");
       final FragmentTransaction ft = getFragmentManager().beginTransaction();
       ft.detach(fragment);
       ft.attach(fragment);
       ft.commit();
   }

}
