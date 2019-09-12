import entities.Department;
import entities.Employee;
import entities.Vacation;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class VacationGenerator {

    public static void createVocs (Session session){
        ArrayList<Vacation> vacList = new ArrayList<Vacation>();
        List<Employee> employees = (List<Employee>) session.createQuery("FROM Employee").list();
        for (Employee employee : employees){
//            System.out.println(employee.getName());
            Department dep = employee.getDepartment();

            List <Vacation> vacInDeps = dep.getVacations();
            List <Vacation> vacInEmps = employee.getVacations();

            LocalDate primeStartDate =  employee.getHireDate();

            int primeYear = primeStartDate.getYear();
            if(primeStartDate.getMonthValue()>6)primeYear++;

            for(int year = primeYear;year<=2020;year++){
                int numOfVac = (int) Math.round(Math.random()+2);
//                System.out.println("Количество отпусков: "+numOfVac+" в "+year);
                int startMonth = 1;
                for (int i =0;i<numOfVac;i++){
                    int step = 12/numOfVac;
                    LocalDate startDate = getRanDate(year,year+1,startMonth,startMonth+step-1);
//                    System.out.println(startDate);

                    int numOfDays = (int) Math.round(Math.random()*7+21);
                    LocalDate endDate = startDate.plusDays(numOfDays);
//                    System.out.println(endDate);
                    startMonth += step;

                    Vacation vac = new Vacation(startDate,endDate,year);

                    vac.setDepartment(dep);
                    vac.setEmployee(employee);

                    vacInDeps.add(vac);
                    vacInEmps.add(vac);
                    vacList.add(vac);

                    session.save(vac);
                }
            }
        }
    }
    public static LocalDate getRanDate(int startYear, int endYear, int startMonth, int endMonth){
        LocalDate startDate = LocalDate.of(startYear, startMonth, 1);
        long start = startDate.toEpochDay();

        LocalDate endDate = LocalDate.of(endYear, endMonth, 1);
        long end = endDate.toEpochDay();

        long randomEpochDay = ThreadLocalRandom.current().longs(start, end).findAny().getAsLong();
        LocalDate date = LocalDate.ofEpochDay(randomEpochDay);
        return date;
    }
}
