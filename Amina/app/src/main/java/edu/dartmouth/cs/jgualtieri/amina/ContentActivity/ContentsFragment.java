package edu.dartmouth.cs.jgualtieri.amina.ContentActivity;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.dartmouth.cs.jgualtieri.amina.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContentsFragment extends Fragment {

    private View view;
    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ContentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContentsFragment newInstance(String param1, String param2) {
        ContentsFragment fragment = new ContentsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contents, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        String cardTitle1 = getString(R.string.card_view_one_title);
        String cardDescription1 = getString(R.string.card_view_one_description);
        String cardImage1 = "thumbnail";
        String cardVid1 = "android.resource://" +this.getContext().getPackageName() + "/raw/card_one";
        CardObject cardObj1 = new CardObject(cardTitle1,cardDescription1,cardImage1,cardVid1);

        String cardTitle2 = getString(R.string.card_view_two_title);
        String cardDescription2 = getString(R.string.card_view_two_description);
        String cardImage2 = "thumbnail2";
        String cardVid2 = "android.resource://" +this.getContext().getPackageName() + "/raw/card_two";
        CardObject cardObj2 = new CardObject(cardTitle2,cardDescription2,cardImage2,cardVid2);

        String cardTitle3 = getString(R.string.card_view_three_title);
        String cardDescription3 = getString(R.string.card_view_three_description);
        String cardImage3 = "thumbnail3";
        String cardVid3 = "android.resource://" +this.getContext().getPackageName() + "/raw/card_three";
        CardObject cardObj3 = new CardObject(cardTitle3,cardDescription3,cardImage3,cardVid3);

        String cardTitle4 = getString(R.string.card_view_four_title);
        String cardDescription4 = getString(R.string.card_view_four_description);
        String cardImage4 = "thumbnail4";
        String cardVid4 = "android.resource://" +this.getContext().getPackageName() + "/raw/card_four";
        CardObject cardObj4 = new CardObject(cardTitle4,cardDescription4,cardImage4,cardVid4);

        CardObject[] cardArray = {cardObj1,cardObj2,cardObj3,cardObj4};


        // specify an adapter (see also next example)
        mAdapter = new RecyclerViewAdapter(cardArray);
        mRecyclerView.setAdapter(mAdapter);

        return view;
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
