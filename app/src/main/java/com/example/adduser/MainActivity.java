package com.example.adduser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 10;
    EditText edtUserName,edtAddress,edtYear;
    Button btnAddUser;
    TextView tvDeleteAll;
    EditText edtSearch;
    RecyclerView rcv_user;
    UserAdapter userAdapter;
    List<User> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtUserName = findViewById(R.id.edtUserName);
        edtAddress = findViewById(R.id.edtAddress);
        edtYear = findViewById(R.id.edtYear);
        btnAddUser = findViewById(R.id.btnAddUser);
        tvDeleteAll = findViewById(R.id.tv_delete_all);
        edtSearch = findViewById(R.id.edtSearch);

        rcv_user = findViewById(R.id.rcv_user);
        list = new ArrayList<>();
        userAdapter = new UserAdapter(new UserAdapter.ClickItemUser() {
            @Override
            public void updateUser(User user) {
                ClickUpdateUser(user);
            }

            @Override
            public void deleteUser(User user) {
                ClickDeleteUser(user);
            }
        });
        userAdapter.setData(list);
        rcv_user.setAdapter(userAdapter);

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i== EditorInfo.IME_ACTION_SEARCH){
                    handleSearchUser();
                }
                return false;
            }
        });


        btnAddUser.setOnClickListener(view -> {
            String username = edtUserName.getText().toString();
            String address = edtAddress.getText().toString();
            String year = edtYear.getText().toString();

            User user = new User(username,address,year);
            if (isUserExist(user)){
                Toast.makeText(this, "User đã tồn tại", Toast.LENGTH_SHORT).show();
                return;
            }
            UserDatabase.getInstance(this).userDAO().insert(user);
            Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            edtUserName.setText("");
            edtAddress.setText("");
            edtYear.setText("");
            anBanPhim();
            loadData();
        });
        loadData();
        
        tvDeleteAll.setOnClickListener(view -> {
            ClickDeleteAllUser();
        });
    }

    private void handleSearchUser() {
        String strKeyword = edtSearch.getText().toString();
        list = new ArrayList<>();//làm mới list
        list = UserDatabase.getInstance(this).userDAO().searchUser(strKeyword);
        //truyền dữ liệu vào trong list tùy thuộc vào tìm kiếm của người dùng
        userAdapter.setData(list);//truyền list vào trong adapter
        anBanPhim();
    }

    private void ClickDeleteAllUser() {
        new AlertDialog.Builder(this)
                .setTitle("Xóa tất cả user")
                .setMessage("bạn có muốn xóa tất cả user ?")
                .setPositiveButton("Có", (dialogInterface, i) -> {
                    UserDatabase.getInstance(this).userDAO().deleteAll();
                    Toast.makeText(this, "Xóa thành công tất cả user", Toast.LENGTH_SHORT).show();
                    loadData();
                })
                .setNegativeButton("Không",null)
                .show();
    }

    private void ClickDeleteUser(User user) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa user : "+user.getUsername())
                .setMessage("bạn có muốn xóa user này")
                .setPositiveButton("Có", (dialogInterface, i) -> {
                    UserDatabase.getInstance(this).userDAO().delete(user);
                    Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    loadData();
                })
                .setNegativeButton("Không",null)
                .show();
    }

    private void ClickUpdateUser(User user) {

        Intent intent = new Intent(MainActivity.this,UpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("objUser",user);
        intent.putExtras(bundle);
        startActivityForResult(intent,MY_REQUEST_CODE);
    }

    private void loadData() {
        list = UserDatabase.getInstance(this).userDAO().getListUser();
        userAdapter.setData(list);
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


    private Boolean isUserExist(User user){
        List<User> list = UserDatabase.getInstance(this).userDAO().checkUser(user.getUsername());
        return list!=null &&!list.isEmpty();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==MY_REQUEST_CODE&&resultCode==Activity.RESULT_OK){
            loadData();
        }
    }
}