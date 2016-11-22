package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.john.combinebrasil.AdapterList.AdapterListAthletes;
import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.DatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlayersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlayersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ListView listViewPlayers;

    public static ArrayList<Athletes> playersArrayList;

    public PlayersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayersFragment newInstance(String param1, String param2) {
        PlayersFragment fragment = new PlayersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_players, container, false);
    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listViewPlayers = (ListView) view.findViewById(R.id.list_athletes);
        callInflateAthletes();

    }

    private void callInflateAthletes(){
        DatabaseHelper db = new DatabaseHelper(AllActivities.mainActivity);
        db.openDataBase();
        ArrayList<Athletes> athletesList = db.getAthletes();
        if(!(athletesList == null || athletesList.size()==0)){
            String[] values = new String[athletesList.size()];
            for(int i=0; i <=athletesList.size()-1; i++){
                values[i] = athletesList.get(i).getId();
            }
            inflateRecyclerView(athletesList, values);
        }
    }
    private void inflateRecyclerView(ArrayList<Athletes> testsArrayList, String[] values){
        AdapterListAthletes adapterTests = new AdapterListAthletes(AllActivities.mainActivity, values, testsArrayList);
        adapterTests.setActivity(AllActivities.mainActivity);
        listViewPlayers.setVisibility(View.VISIBLE);
        listViewPlayers.setAdapter(adapterTests);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
