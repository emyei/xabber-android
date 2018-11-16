package com.xabber.android.ui.adapter.chat;

import android.util.TypedValue;
import android.view.View;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.xabber.android.R;
import com.xabber.android.data.database.messagerealm.MessageItem;
import com.xabber.android.data.entity.UserJid;
import com.xabber.android.data.roster.RosterManager;
import com.xabber.android.ui.color.ColorManager;

public class ForwardedVH extends FileMessageVH {

    public ForwardedVH(View itemView, MessageClickListener messageListener,
                       MessageLongClickListener longClickListener, FileListener listener,
                       int appearance) {
        super(itemView, messageListener, longClickListener, listener, appearance);
    }

    public void bind(MessageItem messageItem, MessagesAdapter.MessageExtraData extraData, String accountJid) {
        super.bind(messageItem, extraData);

        // hide STATUS ICONS
        statusIcon.setVisibility(View.GONE);

        // setup MESSAGE AUTHOR
        UserJid jid = null;
        try {
            jid = UserJid.from(messageItem.getOriginalFrom());
        } catch (UserJid.UserJidCreateException e) {
            e.printStackTrace();
        }

        String author = null;
        if (jid != null) {
            if (messageItem.isFromMUC()) author = jid.getJid().getResourceOrEmpty().toString();
            else author = RosterManager.getInstance().getNameOrBareJid(messageItem.getAccount(), jid);
        }

        if (author != null && !author.isEmpty()) {
            messageHeader.setText(author);
            messageHeader.setTextColor(ColorManager.changeColor(
                    ColorGenerator.MATERIAL.getColor(author), 0.8f));
            messageHeader.setVisibility(View.VISIBLE);
        } else messageHeader.setVisibility(View.GONE);

        // setup BACKGROUND COLOR
        if (jid != null && !accountJid.equals(jid.getBareJid().toString()))
            setUpMessageBalloonBackground(messageBalloon, extraData.getColorStateList());
        else {
            TypedValue typedValue = new TypedValue();
            extraData.getContext().getTheme().resolveAttribute(R.attr.message_background,
                    typedValue, true);
            setUpMessageBalloonBackground(messageBalloon,
                    extraData.getContext().getResources().getColorStateList(typedValue.resourceId));
        }

    }
}
