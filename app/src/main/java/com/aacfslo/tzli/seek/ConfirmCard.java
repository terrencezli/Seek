package com.aacfslo.tzli.seek;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by terrence on 6/1/16.
 */
public class ConfirmCard extends Card {

    protected TextView username;
    protected ImageView deny;
    protected ImageView confirm;

    protected String user;
    protected String userId;
    protected String userUniqueId;

    protected String uniqueId;

    private ConfirmCardOnClickListener listener;
    /**
     * Constructor with a custom inner layout
     * @param context
     */
    public ConfirmCard(Context context, final ConfirmCardOnClickListener listener) {
        super(context, R.layout.confirm_view);
        this.listener = listener;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserUniqueId() {
        return userUniqueId;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        if (view != null) {
            //Retrieve elements
            username = (TextView) parent.findViewById(R.id.username);
            deny = (ImageView) parent.findViewById(R.id.deny);
            confirm = (ImageView) parent.findViewById(R.id.confirm);

            Drawable clear = getContext().getDrawable( R.drawable.ic_clear_black_18dp );
            ColorFilter redFilter = new LightingColorFilter( Color.RED, Color.RED );
            clear.setColorFilter(redFilter);
            deny.setImageDrawable(clear);

            Drawable myIcon = getContext().getDrawable( R.drawable.ic_done_black_18dp );
            ColorFilter greenFilter = new LightingColorFilter( Color.GREEN, Color.GREEN );
            myIcon.setColorFilter(greenFilter);
            confirm.setImageDrawable(myIcon);

            username.setText(user);

            deny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClicked(ConfirmCard.this, true);
                }
            });

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClicked(ConfirmCard.this, false);
                }
            });
        }
    }

    public interface ConfirmCardOnClickListener {
        void onClicked(ConfirmCard card, boolean deny);
    }
}