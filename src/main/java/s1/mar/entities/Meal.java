package s1.mar.entities;

import javax.persistence.*;

@Entity
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String user;
    private String time;
    private String mealname;
    private int caloriesintake;


    public Meal() {}

    public Meal(String user, String time, String mealname, int caloriesintake){
        this.user = user;
        this.time = time;
        this.mealname = mealname;
        this.caloriesintake = caloriesintake;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMealname() {
        return mealname;
    }

    public void setMealname(String mealName) {
        this.mealname = mealName;
    }

    public int getCaloriesintake() {
        return caloriesintake;
    }

    public void setCaloriesintake(int caloriesIntake) {
        this.caloriesintake = caloriesIntake;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
