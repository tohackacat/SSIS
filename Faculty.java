public class Faculty{
    //Attributes
    private String name;
    private String id;
    private boolean tenure;
    private double salary;
    private double hours;
    private double hourlyRate; 

    //Constructor
    public Faculty(String id, String name, boolean tenure, double salary, double hours, double hourlyRate){
        this.id = id;
        this.name = name;
        this.tenure = tenure;
        this.salary = salary;
        this.hours = hours;
        this.hourlyRate = hourlyRate;
    }

    //Getters
    public String getName(){
        return name;
    }
    public String getId(){
        return id;
    }
    public boolean getTenure(){
        return tenure;
    }
    public double getHourse(){
        return hours;
    }
    public double getHourlyRate(){
        return hourlyRate;
    }
    public double getSalary(){
        return salary;
    }



    ////toString method returns object details as a String
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name)
          .append("\nID: ").append(id)
          .append("\nTenure: ").append(tenure)
          .append("\nHours: ").append(hours)
          .append("\nHourly Rate: ").append(hourlyRate)
          .append("\nSalary: ").append(salary);

          return sb.toString();
    }
}