package app.src.main.java.com.course.chat;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.course.chat.*;
import com.course.chat.conn.ServerConnection;
import com.course.chat.db.ChatOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;


public class ActivityLogin extends Activity implements View.OnClickListener {

    private EditText etUser;
    private EditText etPassword;
    private Button bSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        etUser = (EditText) findViewById(R.id.et_user);
        etPassword = (EditText) findViewById(R.id.et_password);
        bSignIn = (Button) findViewById(R.id.button_sign_in);

        bSignIn.setOnClickListener(this);

        if (ServerConnection.getInstance().getUsername() != null) {
            try {
                if (!ServerConnection.getInstance().isConnected()) {
                    ServerConnection.getInstance().connect("192.168.1.234", 1024, true);
                }

                startChat();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startChat() {
        Intent intent = new Intent(this, com.course.chat.ActivityChat.class);
        startActivity(intent);

        finish();
    }

    public void onClick(View view) {
        if (view == bSignIn) {

            String user = etUser.getText().toString();
            String password = etPassword.getText().toString();

            try {
                ServerConnection.getInstance().connect("192.168.1.234", 1024);

                String response = ServerConnection.getInstance().login(user, password);
                if (ServerConnection.MSG_SIGN_IN_OK.equals(response)) {
                    startChat();
                } else if (ServerConnection.MSG_SIGN_IN_FAIL.equals(response)) {
                    Toast.makeText(this, getString(R.string.message_sign_in_fail), Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
