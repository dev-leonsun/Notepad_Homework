package Homework.Day1;

import java.time.LocalDate;
/**
 *Day1 Homework;
 * @author 2023215346
 * @version 1.0
 * @date 2025/2/25
 */
public class TeacherClass extends PersonClass {
    String sectoral;
    String office;
    int incomes;
    
    // 构造方法
    public TeacherClass(String name, byte age, String genders, LocalDate birthDate, String sectoral, String office, int incomes) {
        super(name, age, genders, birthDate);
        this.sectoral = sectoral;
        this.office = office;
        this.incomes = incomes;
    }
    
    // 无参构造方法
    public TeacherClass() {
        super();
        this.sectoral = "Unknown";
        this.office = "Unknown";
        this.incomes = 0;
    }
    
    // Getter 和 Setter 方法
    public String getSectoral() {
        return sectoral;
    }
    
    public void setSectoral(String sectoral) {
        this.sectoral = sectoral;
    }
    
    public String getOffice() {
        return office;
    }
    
    public void setOffice(String office) {
        this.office = office;
    }
    
    public int getIncomes() {
        return incomes;
    }
    
    public void setIncomes(int incomes) {
        this.incomes = incomes;
    }
}