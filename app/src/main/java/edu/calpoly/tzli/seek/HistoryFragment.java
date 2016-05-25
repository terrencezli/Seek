package edu.calpoly.tzli.seek;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardViewNative;


public class HistoryFragment extends Fragment {
    protected Firebase myFirebaseRef;
    protected FacebookProfile personal;

    protected ArrayList<MeetUp> displayArray;
    protected ArrayList<Card> cards;
    CardArrayAdapter mCardArrayAdapter;
    CardListView listView;

    private LinearLayout rlLayout;

    public HistoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rlLayout = (LinearLayout) inflater.inflate(R.layout.fragment_history, container, false);

        personal = ((TabActivity) getActivity()).getPersonal();

        cards = new ArrayList<>();
        displayArray = new ArrayList<>();

        mCardArrayAdapter = new CardArrayAdapter(getContext(),cards);

        listView = (CardListView) rlLayout.findViewById(R.id.myList);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }

        return rlLayout;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (personal.getId().length() > 0) {
            myFirebaseRef = new Firebase("https://boiling-heat-1137.firebaseIO.com/" + personal.getId());
            getPairings();
        }
    }

    public void getPairings() {
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    MeetUp m = postSnapshot.getValue(MeetUp.class);
                    displayArray.add(m);
                }

                initCards();
            }

            @Override
            public void onCancelled(FirebaseError error) {
                System.out.println("The read failed: " + error.getMessage());
            }
        });
    }

    public void initCards() {
        // add chart first
        Card chart = new ChartCard(getActivity());
        cards.add(chart);

        for (MeetUp m : displayArray) {
            //Create a Card
            Card card = new Card(getContext());
            card.setTitle(m.getName() + "               " + m.getDate());

            //Create thumbnail
            CardThumbnail thumb = new CardThumbnail(getContext());

            //Set URL resource
            thumb.setUrlResource(m.retrievePicture());

            //Add thumbnail to a card
            card.addCardThumbnail(thumb);

            cards.add(card);

        }
        mCardArrayAdapter.notifyDataSetChanged();

    }
}