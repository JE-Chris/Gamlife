package com.chris.gamelife.util;

import android.util.Log;

import com.chris.gamelife.Activity.WishCenter;
import com.chris.gamelife.Model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class userUtil {
    private static String path = new WishCenter().getPath() + "/user";
    private static User user;

    static {
        try {
            User u = new User(10);
            File f = new File(path);
            ObjectOutputStream oos;
            if (!f.exists()) {
                f.createNewFile();
                oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(u);
                oos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 存储 user 的数据
    public void storageUser(User user) {
        try {
            File file = new File(path);
            if (!file.exists())
                file.createNewFile();

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file, false));
            oos.writeObject(user);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 获取 user 信息
    public User getUser() {
        if (user != null) {
            return user;
        }

        try {
            File file = new File(path);
            Log.i("==FILE==", "file = " + file);
            if (!file.exists()) {
                file.createNewFile();
                User u = new User(10);
                storageUser(u);
                return u;
            }

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            Log.i("==OIS==", "oos = " + ois);
            user = (User) ois.readObject();
            Log.i("==USER==", "user = " + user);

            if (user == null) {
                user = new User(0);
                storageUser(user);
            }

            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }
}
