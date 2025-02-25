package Homework.Day1;
import java.time.LocalDate;
/**
 *Day1 Homework;
 * @author 2023215346
 * @version 1.0
 * @date 2025/2/25
 */
public class PersonClass {
    String name;
    byte age;
    String genders;
    LocalDate birthDate;
    public PersonClass(String name, byte age, String genders, LocalDate birthDate) {
        this.name = name;
        this.age = age;
        this.genders = genders;
        this.birthDate = birthDate;
    }
    public PersonClass() {
        this.name = "Unknown";
        this.age = 0;
        this.genders = "Unknown";
        this.birthDate = LocalDate.now();   
    }
}