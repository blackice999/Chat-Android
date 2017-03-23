package app.src.main.java.com.course.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.course.chat.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by vhernest on 19/11/15.
 */
public class MessageAdapter extends BaseAdapter {

    private List<com.course.chat.Message> messages;

    public List<com.course.chat.Message> getMessages() {
        return messages;
    }

    public void setMessages(List<com.course.chat.Message> messages) {
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.list_item_message, null);

        TextView tvTimestamp = (TextView) view.findViewById(R.id.tv_timestamp);
        TextView tvMessage = (TextView) view.findViewById(R.id.tv_message);
        TextView tvUsername = (TextView) view.findViewById(R.id.tv_username);

        com.course.chat.Message message = (com.course.chat.Message) getItem(position);

        String timestamp = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss").format(new Date(message.getWhen()));
        tvTimestamp.setText(timestamp);
        tvUsername.setText(message.getUsername());
        tvMessage.setText(message.getText());

        return view;
    }
}
