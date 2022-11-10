package com.example.adduser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    private List<User> userList;

    public ClickItemUser clickItemUser;

    public UserAdapter(ClickItemUser clickItemUser) {
        this.clickItemUser = clickItemUser;
    }


    public interface ClickItemUser{
        void updateUser(User user);
        void deleteUser(User user);
    }

    public void setData(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        User user = userList.get(position);
        if (user==null){
            return;
        }
        holder.tv_username.setText(user.getUsername());
        holder.tv_address.setText(user.getAddress());
        holder.tv_year.setText(user.getYear());
        holder.btnUpdate.setOnClickListener(view -> {

            clickItemUser.updateUser(user);
        });

        holder.btnDelete.setOnClickListener(view -> {
            clickItemUser.deleteUser(user);
        });
    }

    @Override
    public int getItemCount() {
        if (userList==null){
            return 0;
        }
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tv_username,tv_address,tv_year;
        Button btnUpdate,btnDelete;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_year = itemView.findViewById(R.id.tv_year);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
