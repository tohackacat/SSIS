public class Course {
    //Atributes
    private String name;
    private String id;
    private int credits;
    private String description;
    private String location;
    private int length;
    private int year;
    private String season;

    //Constructor
    public Course(String name, String id, int credits){
        this.name = name;
        this.id = id;
        this.credits = credits;
    }

    //Getters
    public String getName(){
        return name;
    }
    public String getID(){
        return id;
    }
    public int getCredits(){
        return credits;
    }
    public String getDescription(){
        return description;
    }
    public String getLocation(){
        return location;
    }
    public int getLength(){
        return length;
    }
    public int getYear(){
        return year;
    }
    public String getSeason(){
        return season;
    }

    //Setters

    //toString method returns object details as a String
    public String toString(){

    }
}
