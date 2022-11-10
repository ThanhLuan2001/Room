package com.example.adduser;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM user")
    List<User> getListUser();

    @Query("SELECT * FROM user where username =:username")
    List<User> checkUser(String username);

    @Update
    void update(User user);
    @Delete
    void delete(User user);

    @Query("DELETE FROM user")
    void deleteAll();

    @Query("SELECT * FROM user WHERE username LIKE '%' ||:name || '%' ")
    List<User> searchUser(String name);

}
