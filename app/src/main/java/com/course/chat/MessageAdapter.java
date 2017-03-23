package com.course.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by vhernest on 19/11/15.
 */
public class MessageAdapter extends BaseAdapter {

    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return (messages != null) ? messages.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolderItem = new ViewHolderItem();

        if(convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            convertView = inflater.inflate(R.layout.list_item_message, null);

            viewHolderItem.tvTimestamp = (TextView) convertView.findViewById(R.id.tv_timestamp);
            viewHolderItem.tvMessage = (TextView) convertView.findViewById(R.id.tv_message);
            viewHolderItem.tvUsername = (TextView) convertView.findViewById(R.id.tv_username);

            convertView.setTag(viewHolderItem);
        } else {
            viewHolderItem = (ViewHolderItem) convertView.getTag();
        }


        Message message = (Message) getItem(position);

        String timestamp = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss").format(new Date(message.getWhen()));
        viewHolderItem.tvTimestamp.setText(timestamp);
        viewHolderItem.tvUsername.setText(message.getUsername() + ": ");
        viewHolderItem.tvMessage.setText(message.getText());

        return convertView;
    }

    static class ViewHolderItem {
        TextView tvTimestamp;
        TextView tvMessage;
        TextView tvUsername;
    }
}
