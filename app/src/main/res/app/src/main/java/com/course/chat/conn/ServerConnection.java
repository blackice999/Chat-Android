package app.src.main.java.com.course.chat.conn;

import android.content.Context;
import android.content.SharedPreferences;

import com.course.chat.Message;
import com.course.chat.app.ChatApplication;
import com.course.chat.db.MessageDAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vhernest on 12/11/15.
 */
public class ServerConnection {

    private static final String SP_NAME = "server.connection";
    private static final String KEY_USERNAME = "key.username";
    private static final String KEY_PASSWORD = "key.password";

    public static final String MSG_SIGN_IN_OK = "OK";
    public static final String MSG_SIGN_IN_FAIL = "FAIL";

    private Socket socket;
    private BufferedReader reader;
    private PrintStream stream;
    private List<MessageListener> messageListeners;
    private Receiver receiver;
    private SharedPreferences pref;

    // singleton design pattern
    private static ServerConnection instance;

    private ServerConnection() {
        messageListeners = new ArrayList<>();
        pref = ChatApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized ServerConnection getInstance() {
        if (instance == null) {
            instance = new ServerConnection();
        }

        return instance;
    }

    public boolean isConnected() {
        return socket != null;
    }

    public void connect(String host, int port) throws IOException {
        connect(host, port, false);
    }

    public void connect(String host, int port, boolean autologin) throws IOException {
        socket = new Socket(host, port);

        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        reader = new BufferedReader(new InputStreamReader(in));
        stream = new PrintStream(out);

        if (autologin) {
            login(pref.getString(KEY_USERNAME, null), pref.getString(KEY_PASSWORD, null));
        }
    }

    public String login(String username, String password) throws IOException {
        stream.println(username);
        stream.println(password);

        String result = reader.readLine();
        if(MSG_SIGN_IN_OK.equals(result)) {
            saveUser(username, password);

            receiver = new Receiver();
            receiver.start();
        }

        return result;
    }

    public void saveUser(String username, String password) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    public void send(String message) {
       stream.println(message);
    }

    public void addMessageListener(MessageListener ml) {
        messageListeners.add(ml);
    }

    public void removeMessageListener(MessageListener ml) {
        messageListeners.remove(ml);
    }

    public void logout() {
        receiver.setNotRunning();
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        MessageDAO.clear();
    }

    class Receiver extends Thread {

        private boolean running = true;

        @Override
        public void run() {
            try {
                while (running) {
                    long id = Long.parseLong(reader.readLine());
                    long when = Long.parseLong(reader.readLine());
                    String username = reader.readLine();
                    String message = reader.readLine();

                    Message msg = new Message(id, when, username, message);

                    List<MessageListener> clone = new ArrayList<>(messageListeners);
                    for (MessageListener ml : clone) {
                        ml.onMessage(msg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            reader = null;
            stream = null;
            socket = null;
        }

        public void setNotRunning() {
            this.running = false;
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface MessageListener {

        public void onMessage(Message msg);
    }

}
