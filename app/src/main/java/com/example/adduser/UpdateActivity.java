package com.example.adduser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {
    EditText edtUserName,edtAddress,edtYear;
    Button btnUpdateUser;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        edtUserName = findViewById(R.id.edtUserName);
        edtAddress = findViewById(R.id.edtAddress);
        edtYear = findViewById(R.id.edtYear);

        btnUpdateUser = findViewById(R.id.btnUpdateUser);

        user = (User) getIntent().getExtras().get("objUser");

        if (user!=null){
            edtUserName.setText(user.getUsername());
            edtAddress.setText(user.getAddress());
            edtYear.setText(user.getYear());
        }

        btnUpdateUser.setOnClickListener(view -> {
            String username = edtUserName.getText().toString();
            String address = edtAddress.getText().toString();
            String year = edtYear.getText().toString();

            user.setUsername(username);
            user.setAddress(address);
            user.setYear(year);
            UserDatabase.getInstance(this).userDAO().update(user);
            Toast.makeText(this, "Sửa thành công", Toast.LENGTH_SHORT).show();
            Intent intentResult = new Intent();
            setResult(Activity.RESULT_OK,intentResult);
            finish();

            anBanPhim();
        });

    }

    private void anBanPhim() {
        try {
            InputMethodManager inputMethodManager
                    = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }
}