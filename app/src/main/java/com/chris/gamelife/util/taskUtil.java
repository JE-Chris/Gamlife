package com.chris.gamelife.util;

import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import com.chris.gamelife.Activity.WishCenter;
import com.chris.gamelife.Model.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class taskUtil {
    private static ArrayList<Task> tasks_daily, tasks_main, tasks_side;
    private static String path = new WishCenter().getPath() + "/tasks";
    private File file;
    private FileInputStream fis;
    private ObjectInputStream ois;
    private FileOutputStream fos;
    private ObjectOutputStream oos;
    private static int open = 0;

    static {
        try {
            ArrayList<Task> ts = new ArrayList<>();
            File f = new File(path);
            ObjectOutputStream oos;
            if (!f.exists()) {
                f.mkdirs();
            }
            f = new File(path + "/tasks_daily");
            if (!f.exists()) {
                f.createNewFile();
                oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(ts);
                oos.close();
            }
            f = new File(path + "/tasks_main");
            if (!f.exists()) {
                f.createNewFile();
                oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(ts);
                oos.close();
            }
            f = new File(path + "/tasks_side");
            if (!f.exists()) {
                f.createNewFile();
                oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(ts);
                oos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public taskUtil() {
        GetTasks();
    }

    public void addTask(String task, int goal, int choice) {
        file = new File(path);
        if (!file.exists())
            file.mkdirs();

        addTask(new Task(task, goal), choice);
    }

    private void addTask(Task task, int choice) {
        switch (choice) {
            case 1:
                tasks_main.add(task);
                storageTasks(tasks_main, choice);
                break;
            case 2:
                tasks_side.add(task);
                storageTasks(tasks_side, choice);
                break;
            default:
                tasks_daily.add(task);
                storageTasks(tasks_daily, choice);
                break;
        }
    }

    public void storageTasks(ArrayList<Task> tasks, int choice) {
        String p = getPath(choice);
        switch (choice) {
            case 0:
                tasks_daily = tasks;
                break;
            case 1:
                tasks_main = tasks;
                break;
            case 2:
                tasks_side = tasks;
            default:
                tasks_daily = tasks;
                break;
        }

        try {
            file = new File(p);
            if (!file.exists())
                file.createNewFile();

            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);

            oos.writeObject(tasks);

            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void GetTasks() {
        tasks_daily = getTasks(0);
        tasks_main = getTasks(1);
        tasks_side = getTasks(2);
    }

    private ArrayList<Task> tasks;
    public ArrayList<Task> getTasks(int model) {
        String p = getPath(model);

        try {
            file = new File(p);
            if (!file.exists())
                file.createNewFile();

            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);

            tasks = (ArrayList<Task>) ois.readObject();
            if (tasks.isEmpty())
                tasks = new ArrayList<>();

            Log.i("===TASKS===", getPath(model) + " tasks = " + tasks);

            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    private String getPath(int model) {
        String p;
        switch (model) {
            case 1:
                p = path + "/tasks_main";
                break;
            case 2:
                p = path + "/tasks_side";
                break;
            default:
                p = path + "/tasks_daily";
                break;
        }
        return p;
    }

    public void replaceTask (String task, int xs, int position, int model) {
        replaceTask(new Task(task, xs), position, model);
    }

    public void replaceTask(Task task, int position, int model) {
        tasks = getTasks(model);
        if (tasks.isEmpty()) {
            getTasks(model);
        }

        tasks.remove(position);
        tasks.add(position, task);

        storageTasks(tasks, model);
    }

    public void deleteTask(int position, int model) {
        switch (model) {
            case 0:
                tasks_daily.remove(position);
                storageTasks(tasks_daily, model);
                break;
            case 1:
                tasks_main.remove(position);
                storageTasks(tasks_main, model);
                break;
            case 2:
                tasks_side.remove(position);
                storageTasks(tasks_side, model);
                break;
            default:
                break;
        }
    }
}
