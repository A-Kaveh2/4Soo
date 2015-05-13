package ir.rasen.charsoo.classes;

import java.util.ArrayList;

/**
 * Created by android on 2/2/2015.
 */
public class Category {

    public int id;
    public String name;

    public Category(int id,String name){
        this.id = id;
        this.name = name;
    }

    public static ArrayList<String> getCategoryListString(ArrayList<Category> categories){
        ArrayList<String> categoriesStr = new ArrayList<>();
        for (Category category: categories)
            categoriesStr.add(category.name);

        return categoriesStr;
    }
}
