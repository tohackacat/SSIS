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
    public Course(String id, String name, int credits, String description, String location, int year, String season, int length){
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.description = description;
        this.location = location;
        this.year = year;
        this.season = season;
        this.length = length;
    }

    //Getters
    public String getName(){
        return name;
    }
    public String getId(){
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


    //toString method returns object details as a String
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name)
          .append("\nID: ").append(id)
          .append("\nNum of Credits: ").append(credits)
          .append("\nCourse Description: ").append(description)
          .append("\nLocation: ").append(location)
          .append("\nLength: ").append(length)
          .append("\nSeason: ").append(season)
          .append("\nYear: ").append(year);

          return sb.toString();
    }
}
