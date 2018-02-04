package hoshin.firebase_chat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NameChoiceActivity extends AppCompatActivity {

    public static final int PERMISSION_ACCOUNT = 2;

    EditText mUserName;
    EditText mUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_choice);

        mUserName = findViewById(R.id.edt_userName);
        mUserEmail = findViewById(R.id.edt_userEmail);
        Button submitButton = findViewById(R.id.btn_registerUser);

        fillDefaultEmail();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(saveUserData()){
                    goToMainActivity();
                }
            }
        });
    }

    private void fillDefaultEmail(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.GET_ACCOUNTS)){
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            else{
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.GET_ACCOUNTS},
                        PERMISSION_ACCOUNT);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        else{
            mUserEmail.setText(Utils.getDefaultUserEmail(this));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch(requestCode){
            case PERMISSION_ACCOUNT:
                // If request is cancelled, the result arrays are empty.
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    fillDefaultEmail();
                }
                else{
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
        }
    }

    private boolean saveUserData() {
        boolean userSaveEffective = true;
        String userName  = mUserName.getText().toString();
        String userEmail = mUserEmail.getText().toString();

        if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(userEmail)){
            userSaveEffective = false;
        }
        else{
            UserStorage.saveUserInfo(this, userName, userEmail);
        }

        return userSaveEffective;
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

}
