public class Faculty{
    //Attributes
    private String name;
    private String id;
    private boolean tenure;
    private double salary;
    private double hours;
    private double hourlyRate; 

    //Constructor
    public Faculty(String name, String id, boolean tenure, double hours, double hourlyRate){
        this.name = name;
        this.id = id;
        this.tenure = tenure;
        this.hours = hours;
        this.hourlyRate = hourlyRate;
    }

    //Getters
    public String getName(){
        return name;
    }
    public String getID(){
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

    //Setters
    public void setName(String name){
        this.name = name;
    }
    public void setTenure(boolean tenure){
        this.tenure = tenure;
    }
    public void setSalary(double salary){
        this.salary = salary;
    }
    public void setSalary(){
        salary = hourlyRate * hours;
    }

    
}