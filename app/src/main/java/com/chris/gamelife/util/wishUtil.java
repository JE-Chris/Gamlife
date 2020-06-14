package com.chris.gamelife.util;

import android.util.Log;

import com.chris.gamelife.Activity.WishCenter;
import com.chris.gamelife.Model.Wish;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

// 用于获取和存储 wishes;
public class wishUtil {
    private static ArrayList<Wish> wishes;
    private static String path = new WishCenter().getPath() + "/wishes";
    private File file;
    private FileInputStream fis;
    private ObjectInputStream ois;
    private FileOutputStream fos;
    private ObjectOutputStream oos;

    static {
        try {
            ArrayList<Wish> ws = new ArrayList<>();
            File f = new File(path);
            ObjectOutputStream oos;
            if (!f.exists()) {
                f.createNewFile();
                oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(ws);
                oos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public wishUtil() {
        wishes = new ArrayList<>();
        wishes = getWishes();
        if (wishes.isEmpty()) {
            wishes = new ArrayList<>();
        }
    }

    public void addWish(Wish wish) {
        Log.i("ISNULL", "wishes = " + wishes);
        wishes.add(wish);
        storageWishes(wishes);
    }

    public void addWish(String wish, int goal) {
        Log.i("ISNULL", "wishes = " + wishes);
        addWish(new Wish(wish, goal));
    }

    public void storageWishes(ArrayList<Wish> wishes2) {
        wishes = wishes2;
        file = new File(path);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);

            oos.writeObject(wishes2);

            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Wish> getWishes() {
        if (!wishes.isEmpty() && wishes != null) {
            Log.i("GETTER", "Get null pointer");
            return wishes;
        } else {
            try {
                Log.i("GETTER", "Get wishes from file.");
                file = new File(path);
                if (!file.exists())
                    file.createNewFile();
                Log.i("FILE", "file = " + file + ", isExists: " + file.exists());
                fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);

                wishes = (ArrayList<Wish>) ois.readObject();

                ois.close();
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return wishes;
    }

    public void deleteWish(int position) {
        wishes.remove(position);
        storageWishes(wishes);
    }

    public void replaceWish (String wish, int goal, int position) {
        replaceWish(new Wish(wish, goal), position);
    }


    public void replaceWish(Wish wish, int position) {
        if (wishes.isEmpty()) {
            getWishes();
        }
        wishes.remove(position);
        wishes.add(position, wish);
        storageWishes(wishes);
    }
}
