package net.sharksystem.key_administration.fragments.certifications;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.R;
import net.sharksystem.storage.SharedPreferencesHandler;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CertificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CertificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CertificationFragment extends Fragment implements CertificationRecyclerAdapter.OnCertificationClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private LinearLayoutManager layoutManager;

    SharedPreferencesHandler sharedPreferencesHandler;

    ArrayList<ReceiveCertificationPojo> certList;

    public CertificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CertificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CertificationFragment newInstance(String param1, String param2) {
        CertificationFragment fragment = new CertificationFragment();
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

        View view = inflater.inflate(R.layout.fragment_public_key_tab, container, false);

        // recycler view
        recyclerView = view.findViewById(R.id.fragment_public_key_tab_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // specify an adapter (see also next example)
        initRecyclerViewAdapter();

        return view;
    }

    private void initRecyclerViewAdapter() {
        if (this.certList != null) {
            recyclerViewAdapter = new CertificationRecyclerAdapter(this.certList, this);
            recyclerView.setAdapter(recyclerViewAdapter);
        }else {
            // todo empty recycler view layout
            ArrayList<ReceiveCertificationPojo> pojos = new ArrayList<>();

            pojos.add(new ReceiveCertificationPojo("DummyAlias1", "Dummy UUID", "",
                    new Signer("Dummy Signer Alias1", "Dummy uuid", "")));

            pojos.add(new ReceiveCertificationPojo("DummyAlias2", "Dummy UUID", "",
                    new Signer("Dummy Signer Alias2", "Dummy uuid", "")));

            recyclerViewAdapter = new CertificationRecyclerAdapter(pojos, this);
            recyclerView.setAdapter(recyclerViewAdapter);
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onKeyClick(int position) {
        Intent intent = new Intent(getActivity(), DetailViewCertificationActivity.class);
        int itemPos = position;
        intent.putExtra("ITEM_POS", itemPos);
        startActivity(intent);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
