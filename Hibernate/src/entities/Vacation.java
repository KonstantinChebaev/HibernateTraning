package entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vacation")
public class Vacation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "start_vac")
    @Type(type = "java.time.LocalDate")
    private LocalDate startVac;
    @Column(name = "end_vac")
    @Type(type = "java.time.LocalDate")
    private LocalDate endVac;
    @Column(name = "year")
    private Integer year;
    @ManyToOne(optional = false)
    @JoinColumn(name = "department_id")
    private Department department;
    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;


    public Vacation(){
       //DLYA HIBERNAYTA
    }

    public Vacation(LocalDate startDate, LocalDate endDate,int year){
        this.startVac = startDate;
        this.endVac = endDate;
        this.year = year;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public LocalDate getStartVac() {
        return startVac;
    }

    public void setStartVac(LocalDate startVac) {
        this.startVac = startVac;
    }

    public LocalDate getEndVac() {
        return endVac;
    }

    public void setEndVac(LocalDate endVac) {
        this.endVac = endVac;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
