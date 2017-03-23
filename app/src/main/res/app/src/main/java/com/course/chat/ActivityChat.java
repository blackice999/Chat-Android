package app.src.main.java.com.course.chat;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.course.chat.*;
import com.course.chat.conn.ServerConnection;
import com.course.chat.db.MessageDAO;

import java.util.ArrayList;
import java.util.List;

public class ActivityChat extends Activity implements View.OnClickListener, View.OnKeyListener, ServerConnection.MessageListener {

    private static final int MENU_ITEM_ID_LOGOUT = 1;

    private ListView lvMessages;
    private EditText etInput;
    private Button bSend;
    private List<com.course.chat.Message> messages;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_chat_layout);

        lvMessages = (ListView) findViewById(R.id.lv_messages);

        adapter = new MessageAdapter();
        messages = MessageDAO.list();
        adapter.setMessages(messages);
        lvMessages.setAdapter(adapter);

        etInput = (EditText) findViewById(R.id.et_input_text);
        etInput.setOnKeyListener(this);

        bSend = (Button) findViewById(R.id.btn_send);

        bSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                sendMessage();
                break;
        }
    }

    public void sendMessage() {
        String text = etInput.getText().toString();
        etInput.setText("");
        ServerConnection.getInstance().send(text);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (v.getId()) {
            case R.id.et_input_text:
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    sendMessage();
                }
                break;
        }
        return false;
    }

    @Override
    public void onMessage(final com.course.chat.Message message) {
        MessageDAO.add(message);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messages.add(message);

                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        ServerConnection.getInstance().addMessageListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        ServerConnection.getInstance().removeMessageListener(this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_ITEM_ID_LOGOUT, 0, "logout");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_ID_LOGOUT:
                ServerConnection.getInstance().logout();
                Intent intent = new Intent(this, ActivityLogin.class);
                startActivity(intent);

                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
