package Homework.Day1;

import java.time.LocalDate;
/**
 *Day1 Homework;
 * @author 2023215346
 * @version 1.0
 * @date 2025/2/25
 */
public class Main {
    public static void main(String[] args) {
        // 创建一个 PersonClass 对象
        PersonClass person = new PersonClass("张三", (byte)30, "男", LocalDate.of(1994, 5, 15));
        
        // 创建一个 StudentClass 对象
        StudentClass student = new StudentClass("李四", (byte)18, "女", LocalDate.of(2006, 8, 22), (short)650, "计算机科学");
        
        // 创建一个 TeacherClass 对象
        TeacherClass teacher = new TeacherClass("王五", (byte)45, "男", LocalDate.of(1979, 3, 10), "计算机系", "科技楼A栋305", 12000);
        
        // 使用无参构造函数创建对象
        PersonClass defaultPerson = new PersonClass();
        TeacherClass defaultTeacher = new TeacherClass();
        
        // 展示继承中的方法覆盖 - StudentClass 中的 StudentMethod 方法
        student.StudentMethod();
        
        // 展示继承的属性和方法
        System.out.println("\n===== 继承特性演示 =====");
        System.out.println("1. 子类继承父类的属性：");
        System.out.println("   学生姓名 (从父类继承): " + student.name);  // 访问从 PersonClass 继承的属性
        System.out.println("   学生专业 (子类自己的属性): " + student.getSpecialty());
        
        System.out.println("\n2. 子类继承父类的无参构造：");
        System.out.println("   默认教师姓名: " + defaultTeacher.name);  // 从 PersonClass.java 的无参构造函数设置
        System.out.println("   默认教师部门: " + defaultTeacher.getSectoral());  // 从 TeacherClass.java 的无参构造函数设置
        
        System.out.println("\n3. 多态性演示：");
        // 父类引用可以指向子类对象（向上转型）
        PersonClass p1 = new StudentClass("赵六", (byte)19, "男", LocalDate.of(2005, 9, 18), (short)620, "物理学");
        PersonClass p2 = new TeacherClass("钱七", (byte)38, "女", LocalDate.of(1986, 12, 5), "物理系", "科技楼B栋205", 10000);
        
        System.out.println("   p1 是 PersonClass 类型: " + (p1 instanceof PersonClass));
        System.out.println("   p1 是 StudentClass 类型: " + (p1 instanceof StudentClass));
        System.out.println("   p2 是 TeacherClass 类型: " + (p2 instanceof TeacherClass));
        
        // 使用向下转型访问子类特有的方法
        if (p1 instanceof StudentClass) {
            StudentClass s = (StudentClass) p1;
            System.out.println("   学生高考成绩: " + s.getGaokao_grade());
        }
        
        if (p2 instanceof TeacherClass) {
            TeacherClass t = (TeacherClass) p2;
            System.out.println("   教师收入: " + t.getIncomes() + "元");
        }
    }
}
