package technource.greasecrowd.model;

/**
 * Created by technource on 7/9/17.
 */

public class List_items {

  public String id;
  public String name;
  int popular;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPopular() {
    return popular;
  }

  public void setPopular(int popular) {
    this.popular = popular;
  }
}
