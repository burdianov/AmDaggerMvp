package com.testography.am_mvp.ui.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.testography.am_mvp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.add_address_btn)
    Button mAddAddress;

    private Callbacks mCallbacks;

    public interface Callbacks {
        void callAddressFragment(AddressFragment addressFragment);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onClick(View view) {
        mCallbacks.callAddressFragment(new AddressFragment());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, view);

        mAddAddress.setOnClickListener(this);
        return view;
    }


}
