package edu.calpoly.tzli.seek;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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


public class HistoryFragment extends Fragment {
    protected Firebase myFirebaseRef;
    protected static String personalName = "";
    protected static String personalId = "";

    protected ArrayList<MeetUp> displayArray;
    protected ArrayList<Card> cards;
    CardArrayAdapter mCardArrayAdapter;
    CardListView listView;

    private LinearLayout rlLayout;
    private FragmentActivity faActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        faActivity = (FragmentActivity) super.getActivity();
        rlLayout = (LinearLayout) inflater.inflate(R.layout.fragment_history, container, false);

        LineChart lineChart = (LineChart) rlLayout.findViewById(R.id.chart);
        // creating list of entry
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(2f, 3));
        entries.add(new Entry(18f, 4));
        entries.add(new Entry(9f, 5));

        LineDataSet dataset = new LineDataSet(entries, "# of MOI's");
        dataset.setDrawFilled(true);

        // creating labels
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        labels.add("July");
        labels.add("August");
        labels.add("September");
        labels.add("October");
        labels.add("November");
        labels.add("December");

        LineData data = new LineData(labels, dataset);
        lineChart.setData(data); // set the data and list of lables into chart


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

        if (personalId.length() > 0) {
            myFirebaseRef = new Firebase("https://boiling-heat-1137.firebaseIO.com/" + personalId);
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