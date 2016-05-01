package com.xmpp.chat.adapter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cool.xmppsample.R;
import com.xmpp.chat.dao.ChatItem;
import com.xmpp.chat.dao.MessageItem;
import com.xmpp.chat.util.EmojiUtil;

public class LiveChatAdapter extends BaseAdapter {
	private Activity context;
	private List<MessageItem> items;
	private String displayName;
	ChatItem chatItem;

	public LiveChatAdapter(Activity paramContext, ChatItem chatItem,
			List<MessageItem> paramList, String displayName) {
		this.items = paramList;
		this.context = paramContext;
		this.displayName = displayName;
		this.chatItem = chatItem;
	}

	private int[] COLORS = new int[] { 0xFFFF0000, 0xFFcc8800, 0xFFFF00FF,
			0xFF00FF00, 0xFF00FFFF, 0xFF0000FF };
	HashMap<String, Integer> colorsFrom = new HashMap<String, Integer>();
	int currentColor = 0;

	public void notifyDataSetChangedOnUI() {
		context.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				notifyDataSetChanged();
			}
		});
	}

	public void add(final MessageItem paramMessageItem) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				items.add(paramMessageItem);
				notifyDataSetChanged();
			}
		});

	}

	public int getCount() {
		return this.items.size();
	}

	public MessageItem getItem(int paramInt) {
		return (MessageItem) this.items.get(paramInt);
	}

	public long getItemId(int paramInt) {
		return 0L;
	}

	public View getView(final int pos, View view, ViewGroup paramViewGroup) {
		if (view == null) {
			final View v = LayoutInflater.from(this.context).inflate(
					R.layout.item_livechat, null);

			TextView textMessage = (TextView) view.findViewById(R.id.text1);
			TextView textWhoSent = (TextView) view
					.findViewById(R.id.textWhoSent);
			TextView textTime = (TextView) view.findViewById(R.id.textTime);
			textMessage.setText(getItem(pos).file != null ? "" : EmojiUtil
					.getInstance(context).processEmoji(getItem(pos).message,
							textMessage.getTextSize()));
			String who = getItem(pos).outMessage ? "You"
					: getItem(pos).opponentDisplay;
			((LinearLayout) view.findViewById(R.id.layoutTop))
					.setGravity(getItem(pos).outMessage ? Gravity.LEFT
							: Gravity.RIGHT);

			textTime.setText(DateFormat.getTimeFormat(this.context).format(
					new Date(getItem(pos).timestamp)));

			int color = 0;
			if (getItem(pos).outMessage) {
				color = 0xff00a0e3;
			} else {
				if (colorsFrom.containsKey(getItem(pos).opponentDisplay)) {
					color = colorsFrom.get(getItem(pos).opponentDisplay);
				} else {
					color = COLORS[currentColor++ % COLORS.length];
					colorsFrom.put(getItem(pos).opponentDisplay, color);
				}
			}

		}
		return view;
	}
}