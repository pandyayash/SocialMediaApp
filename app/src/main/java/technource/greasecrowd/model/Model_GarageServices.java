package technource.greasecrowd.model;

/**
 * Created by technource on 29/1/18.
 */

public class Model_GarageServices {
    String id;
    String garage_master_id;
    String service_id;
    String name;
    String is_checked;
    boolean isSelected=false;

    public String getIs_checked() {
        return is_checked;
    }

    public void setIs_checked(String is_checked) {
        this.is_checked = is_checked;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGarage_master_id() {
        return garage_master_id;
    }

    public void setGarage_master_id(String garage_master_id) {
        this.garage_master_id = garage_master_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
