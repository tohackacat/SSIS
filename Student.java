public class Student {
  private String id;
  private String name;
  private double gpa;
  private int credReqs;

  public Student(String id, String name, double gpa, int credReqs) {
    this.id = id;
    this.name = name;
    this.gpa = gpa;
    this.credReqs = credReqs;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public double getGpa() {
    return gpa;
  }

  public int getCredReqs() {
    return credReqs;
  }
}
