package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;

import java.io.IOException;
import java.util.ArrayList;

import br.com.john.combinebrasil.AdapterList.AdapterRecyclerTests;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.Services;
import br.com.john.combinebrasil.Services.SyncDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TestsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static RecyclerView mRecyclerView;
    public LinearLayoutManager mLayoutManager;
    public static ArrayList<Tests> testsArrayList;

    private OnFragmentInteractionListener mListener;

    public TestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestsFragment newInstance(String param1, String param2) {
        TestsFragment fragment = new TestsFragment();
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
        return inflater.inflate(R.layout.fragment_tests, container, false);

    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mLayoutManager = new LinearLayoutManager(AllActivities.mainActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int totalItemCount = llm.getItemCount();
                Log.i("totalRecycler", totalItemCount+"");
                //CarAdapter adapter = (CarAdapter) mRecyclerView.getAdapter();
                Log.i("recyclerLastFind", llm.findLastCompletelyVisibleItemPosition()+"");

                if (totalItemCount == llm.findLastCompletelyVisibleItemPosition()+1) {
                    // List<Car> listAux = ((CestasFragment) getActivity()).getSetCarList(10);
                    Log.i("recycler", "last");
                    /*for (int i = 0; i < listAux.size(); i++) {
                        adapter.addListItem(listAux.get(i), mList.size());
                    }*/
                }
            }
        });

        try {
            MainActivity.linearProgress.setVisibility(View.VISIBLE);
            SyncDatabase sync = new SyncDatabase(AllActivities.mainActivity);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(Constants.debug)
            callAllFalseTests();
        else
            callAllTests();
    }

    private void callAllFalseTests(){
        testsArrayList = new ArrayList<Tests>();
        String[] values = new String[6];
        for(int i=0; i<=5; i++){
            Tests test = new Tests();
            test.setId(String.valueOf(i));
            test.setName("Teste "+i);
            test.setType("Tipo "+i);
            test.setDescription("descrição "+i);
            testsArrayList.add(test);
            values[i]="Teste "+i;
        }
        inflateRecyclerView(testsArrayList, values);
    }

    private void callAllTests() {
        if (Services.isOnline(AllActivities.mainActivity)) {
            MainActivity.linearProgress.setVisibility(View.VISIBLE);
            String url = Constants.URL + Constants.login;
            //Connection task = new Connection(url, Request.Method.GET, Constants.CALLED_GET_TESTS, true, AllActivities.mainActivity);
            //task.callByJsonStringRequest();
        }
        else
            Services.messageAlert(AllActivities.mainActivity, "Aviso", "Sem conexão com a internet", "");
    }

    private static void inflateRecyclerView(ArrayList<Tests> testsArrayList, String[] values){
        AdapterRecyclerTests adapterTests = new AdapterRecyclerTests(AllActivities.mainActivity,testsArrayList, values);
        adapterTests.setHomeActivity(AllActivities.mainActivity);
        MainActivity.linearProgress.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setLayoutManager(new GridLayoutManager(AllActivities.mainActivity, 2));//numero de colunas
        mRecyclerView.setAdapter(adapterTests);
    }

    public static void ShowTests(String response, boolean isList){
        MainActivity.linearProgress.setVisibility(View.GONE);
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        Tests tests;

        if (!isList)
            tests = des.getObjectTest();
        else{
            ArrayList<Tests> testsArrayList = des.getListTests();
            String[] values = new String[testsArrayList.size()];
            for(int i=0; i<=testsArrayList.size()-1; i++){
                values[i] = String.valueOf(testsArrayList.get(i).getName());
            }
            inflateRecyclerView(testsArrayList, values);
        }

        if (!response.equals("")) {
            //Guarda em memória do cel
        }
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
