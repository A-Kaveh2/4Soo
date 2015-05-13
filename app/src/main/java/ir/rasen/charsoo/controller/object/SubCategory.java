package ir.rasen.charsoo.controller.object;

import java.util.ArrayList;

/**
 * Created by android on 2/2/2015.
 */
public class SubCategory {

    public int id;
    public String name;

    public SubCategory(int id, String name){
        this.id = id;
        this.name = name;
    }

    public static ArrayList<String> getSubCategoryListString(ArrayList<SubCategory> subCategories){
        ArrayList<String> subCategoriesStr = new ArrayList<>();
        for (SubCategory subCategory: subCategories)
            subCategoriesStr.add(subCategory.name);

        return subCategoriesStr;
    }
}
