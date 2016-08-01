package com.aacfslo.tzli.seek;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;

/**
 * Created by terrence on 5/30/16.
 */
public class ConfirmFragment extends Fragment {
    protected Firebase myFirebaseRef;
    protected FacebookProfile personal;

    protected ArrayList<MeetUp> displayArray;
    protected ArrayList<String> keys;
    protected ArrayList<Card> cards;
    CardArrayRecyclerViewAdapter mCardArrayAdapter;
    CardRecyclerView cardRecyclerView;

    private LinearLayout rlLayout;

    public ConfirmFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rlLayout = (LinearLayout) inflater.inflate(R.layout.fragment_confirm, container, false);

        personal = ((TabActivity) getActivity()).getPersonal();

        cards = new ArrayList<>();
        displayArray = new ArrayList<>();
        keys = new ArrayList<>();

        mCardArrayAdapter = new CardArrayRecyclerViewAdapter(getContext(),cards);

        cardRecyclerView = (CardRecyclerView) rlLayout.findViewById(R.id.myList);
        cardRecyclerView.setHasFixedSize(false);
        cardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (cardRecyclerView !=null){
            cardRecyclerView.setAdapter(mCardArrayAdapter);
        }

        myFirebaseRef = new Firebase(TabActivity.FIREBASE_URL + personal.getId());

        getPairings();

        return rlLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void getPairings() {
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int arraySize = keys.size();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    MeetUp m = postSnapshot.getValue(MeetUp.class);
                    displayArray.clear();
                    keys.clear();

                    if (!m.getMet() && !keys.contains(postSnapshot.getKey())) {
                        keys.add(postSnapshot.getKey());
                        displayArray.add(m);
                    }
                }

                if (arraySize != keys.size()) {
                    initCards();
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
                System.out.println("The read failed: " + error.getMessage());
            }
        });
    }

    public void initCards() {
        for (int i = 0; i < displayArray.size(); i++) {
            MeetUp m = displayArray.get(i);
            //Create a Card
            ConfirmCard card = new ConfirmCard(getContext(), new ConfirmCard.ConfirmCardOnClickListener() {
                @Override
                public void onClicked(ConfirmCard card, boolean deny) {
                    if (deny) {
                        Toast.makeText(getContext(), "No meet up :(", Toast.LENGTH_SHORT).show();

                        Firebase friendBase = new Firebase(TabActivity.FIREBASE_URL + card.getUserId());
                        friendBase.child(card.getUniqueId()).setValue(null);


                        keys.remove(card.getUniqueId());
                        cards.remove(card);

                        myFirebaseRef.child(card.getUniqueId()).setValue(null);
                    }
                    else {
                        Toast.makeText(getContext(), ":)", Toast.LENGTH_SHORT).show();

                        keys.remove(card.getUniqueId());
                        cards.remove(card);

                        Map<String, Object> met = new HashMap<String, Object>();
                        met.put("met", true);
                        myFirebaseRef.child(card.getUniqueId()).updateChildren(met);
                    }

                    mCardArrayAdapter.notifyDataSetChanged();
                }
            });

            card.setUser(m.getName());
            card.setUserId(m.getId());
            card.setUniqueId(keys.get(i));

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