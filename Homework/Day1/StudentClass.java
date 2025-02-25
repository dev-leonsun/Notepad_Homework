package Homework.Day1;

import java.time.LocalDate;
/**
 *Day1 Homework;
 * @author 2023215346
 * @version 1.0
 * @date 2025/2/25
 */

public class StudentClass extends PersonClass {
    private short gaokao_grade;
    private String specialty;

    // 构造方法
    public StudentClass(String name, byte age, String genders, LocalDate birthDate, short gaokao_grade, String specialty) {
        super(name, age, genders, birthDate);
        this.gaokao_grade = gaokao_grade;
        this.specialty = specialty;
    }

    // Getter 和 Setter 方法
    public short getGaokao_grade() {
        return gaokao_grade;
    }

    public void setGaokao_grade(short gaokao_grade) {
        this.gaokao_grade = gaokao_grade;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public void StudentMethod() {
        System.out.println("This Student is " + this.name + " Grade " + this.gaokao_grade);
    }
}