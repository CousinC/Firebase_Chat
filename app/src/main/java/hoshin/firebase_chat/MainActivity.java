package hoshin.firebase_chat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ValueEventListener, MessageAdapter.Listener {

    MessageAdapter mMessageAdapter;
    RecyclerView mRecyclerView;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!UserStorage.isUserLoggedIn(this)) {
            Intent intent = new Intent(this, NameChoiceActivity.class);
            this.startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerView);
        final EditText messageContent = findViewById(R.id.edt_messageContent);
        ImageButton sendButton = findViewById(R.id.btn_sendButton);

        User currentUser = UserStorage.getUserInfo(this);

        mMessageAdapter = new MessageAdapter(this, new ArrayList<Message>(), currentUser);
        mRecyclerView.setAdapter(mMessageAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        connectionToFirebase();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitNewMessage(messageContent.getText().toString());
                messageContent.setText("");
            }
        });
    }

    private void connectionToFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference(Constant.FIREBASE_PATH);
        mDatabaseReference.addValueEventListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.item_logout:
                UserStorage.deleteUserInfo(this);
                Intent intent = new Intent(this, NameChoiceActivity.class);
                this.startActivity(intent);
                finish();
        }
        return true;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Message> messagesList = new ArrayList<>();
        for(DataSnapshot messageSnapshot : dataSnapshot.getChildren()){
            Message message = messageSnapshot.getValue(Message.class);
            if (message != null) {
                message.setKey(messageSnapshot.getKey());
                messagesList.add(message);
            }
        }
        mMessageAdapter.setData(messagesList);
        mRecyclerView.scrollToPosition(messagesList.size() - 1);
    }

    private void submitNewMessage(String newMessage) {
        User user = UserStorage.getUserInfo(this);
        if (!newMessage.isEmpty() && user != null) {
            DatabaseReference newData = mDatabaseReference.push();
            newData.setValue(new Message(user.getName(),
                            user.getEmail(),
                            newMessage));
        }
    }

    @Override
    public void onItemLongClick(int position, final Message message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");
        builder.setItems(R.array.messageOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selectedItem) {
                switch(selectedItem){
                    case 0:
                        mDatabaseReference.child(message.getKey()).removeValue();
                        break;
                    case 1:
                        mDatabaseReference.child(message.getKey()).
                                setValue(new Message(message.getUserName(),
                                        message.getUserEmail(),
                                        ":D"));
                }
            }
        });
        builder.show();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(this, "Error: " + databaseError, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mDatabaseReference.removeEventListener(this);
    }

}