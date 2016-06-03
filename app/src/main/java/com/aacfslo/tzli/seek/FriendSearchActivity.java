package com.aacfslo.tzli.seek;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aacfslo.tzli.seek.helper.DividerItemDecoration;
import com.aacfslo.tzli.seek.helper.RecyclerItemClickListener;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FriendSearchActivity extends AppCompatActivity
//        implements
//        DatePickerDialog.OnDateSetListener,
//        View.OnClickListener
{

  FacebookAdapter adapter;
  List<FacebookProfile> names;
  List<FacebookProfile> filteredNames;
  Toolbar toolbar;
  AccessToken accessToken;

  Button back;
  EditText search;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_friends_picker);

    accessToken = AccessToken.getCurrentAccessToken();

    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
    recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);


    search = (EditText) findViewById(R.id.search);
    search.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        filteredNames.clear();
        adapter.notifyDataSetChanged();

        if (s.length() == 0) {
          filteredNames.addAll(names);
          return;
        }
        String search = s.toString().replace(" ", "").toLowerCase();
        for (FacebookProfile name : names) {
          if (name.getName().toLowerCase().contains(search)) {
            filteredNames.add(name);
          }
        }
        adapter.notifyDataSetChanged();
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });


    try {
      names = getFacebookFriends(accessToken);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    filteredNames = new ArrayList<>(names);
    adapter = new FacebookAdapter(filteredNames);
    recyclerView.setAdapter(adapter);

    recyclerView.addOnItemTouchListener(
        new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
          @Override public void onItemClick(View view, int position) throws IOException {
            MeetFragment.friend = new FacebookProfile(filteredNames.get(position).getName(),
                    filteredNames.get(position).getId());
            onBackPressed();
          }
        })
    );
  }

  public static class FacebookAdapter extends RecyclerView.Adapter<FacebookViewHolder> {

    private List<FacebookProfile> mFacebook;

    public FacebookAdapter(List<FacebookProfile> a) {
      this.mFacebook = a;
    }

    @Override
    public int getItemViewType(int position) {
      return R.layout.friend_item_view;
    }

    @Override
    public FacebookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new FacebookViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }


    @Override
    public void onBindViewHolder(FacebookViewHolder holder, int position) {
      holder.bind(mFacebook.get(position));
    }

    @Override
    public int getItemCount() {
      return mFacebook.size();
    }
  }

  public static class FacebookViewHolder extends RecyclerView.ViewHolder {

    private ImageView mIv;
    private TextView mTv;
    public FacebookProfile mFb;

    public FacebookViewHolder(View itemView) {
      super(itemView);

      mIv = (ImageView) itemView.findViewById(R.id.iv);
      mTv = (TextView) itemView.findViewById(R.id.tv);
    }

    public void bind(FacebookProfile a) {
      mFb = a;

      mTv.setText(a.getName());

      Glide.with(mIv.getContext())
              .load(a.getImageUrl())
              .into(mIv);
    }


  }

  public static List<FacebookProfile> getFacebookFriends(AccessToken accessToken) throws InterruptedException, ExecutionException {
    final List<FacebookProfile> friendsMap = new ArrayList<>();

    GraphRequest.Callback gCallback = new GraphRequest.Callback() {
      public void onCompleted(GraphResponse response) {
        JSONObject jGraphObj = response.getJSONObject();
        try {
          JSONArray friendsData = jGraphObj.getJSONArray("data");
          for (int i = 0; i < friendsData.length(); i++) {
            JSONObject friend = friendsData.getJSONObject(i);
            String friendId = friend.getString("id");
            String friendName = friend.getString("name");
            FacebookProfile fb = new FacebookProfile(friendName, friendId);
            friendsMap.add(fb);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };
    final GraphRequest graphRequest = new GraphRequest(accessToken, "/me/friends", null, HttpMethod.GET, gCallback);
    // Run facebook graphRequest.
    Thread t = new Thread(new Runnable() {
      @Override
      public void run() {
        GraphResponse gResponse = graphRequest.executeAndWait();
      }
    });
    t.start();
    t.join();
    return friendsMap;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // handle arrow click here
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }

    return super.onOptionsItemSelected(item);
  }
}
