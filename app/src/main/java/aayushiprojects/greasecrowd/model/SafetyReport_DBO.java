package aayushiprojects.greasecrowd.model;

/**
 * Created by technource on 14/2/18.
 */

public class SafetyReport_DBO {

    String name,description;
    String selectedColor;

    public String getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(String selectedColor) {
        this.selectedColor = selectedColor;
    }

    public SafetyReport_DBO(){

    }
    public SafetyReport_DBO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "Product [name=" + name + ", description="
                + description +  "]";
    }
}
