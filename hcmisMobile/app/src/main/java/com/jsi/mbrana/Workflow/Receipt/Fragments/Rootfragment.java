package com.jsi.mbrana.Workflow.Receipt.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jsi.mbrana.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Rootfragment extends Fragment {

    public Rootfragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_root_fragment, container, false);

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        /*
         * When this container fragment is created, we fill it with our first
		 * "real" fragment
		 */
        transaction.replace(R.id.root_frame, new AddReceipt());

        transaction.commit();

        return view;
    }

}
