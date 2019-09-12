import entities.Department;
import entities.Employee;
import entities.Vacation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danya on 26.10.2015.
 */
public class Loader {
    private static SessionFactory sessionFactory;

    public static void main(String[] args) {
        setUp();

        Session session = sessionFactory.openSession();
        session.beginTransaction();


        // Начало кода по поиску пересечений в отпусках
        List<Department> departments;
        departments = (List<Department>) session.createQuery("FROM Department").list();
        Employee empOne;
        Employee empTwo;
        List<Vacation> vacsOne;
        List<Vacation> vacsTwo;
        int startYear;
        for(Department department : departments)
        {
            System.out.println(department.getName());
            List<Employee> employees = (List<Employee>) department.getEmployees();
            for(int i = 0; i<employees.size()-1;i++)
            {
                empOne = employees.get(i);
                System.out.println(empOne.getName());
                vacsOne = empOne.getVacations();
                for(int y = i+1;y<employees.size();y++){
                    empTwo = employees.get(y);
                    vacsTwo = empTwo.getVacations();
                    if(empOne.getHireDate().getYear()>empTwo.getHireDate().getYear()){
                        startYear = empOne.getHireDate().getYear();
                        int delete;
                        List <Vacation> blackList = new ArrayList<Vacation>();
                        SEARCH_FOR_DELETE :for(delete = 0; delete<5;delete++){
                            if(vacsTwo.get(delete).getYear()==startYear){
                                break SEARCH_FOR_DELETE;
                            } else blackList.add(vacsTwo.get(delete));
                        }
                        vacsTwo.removeAll(blackList);
                    } else  {
                        startYear = empTwo.getHireDate().getYear();
                        int delete;
                        List <Vacation> blackList = new ArrayList<Vacation>();
                        SEARCH_FOR_DELETE :for(delete = 0; delete<5;delete++){
                            if(vacsOne.get(delete).getYear()==startYear){
                                break SEARCH_FOR_DELETE;
                            } else blackList.add(vacsOne.get(delete));
                        }
                        vacsOne.removeAll(blackList);
                    }
                    for(int x = 0;x<employees.size();x++){
                       Vacation a = vacsOne.get(x);
                       for(int q = -3;q<4;q++){
                           if((x==2&&q<-2)||(x==1&&q<-1)||(x==0&&q<0))continue;
                           if((x+q)>=vacsTwo.size())continue;
                           Vacation b = vacsTwo.get(x+q);
                           if(a.getStartVac().isAfter(b.getStartVac())&&a.getStartVac().isBefore(b.getEndVac())){
                               System.out.println("Пересечение отпусков c "+empTwo.getName()+"\n"+a.getStartVac()+" - "+b.getEndVac());
                           }
                           if(a.getEndVac().isAfter(b.getStartVac())&&a.getEndVac().isBefore(b.getEndVac())){
                               System.out.println("Пересечение отпусков c "+empTwo.getName()+"\n"+b.getStartVac()+" - "+a.getEndVac());
                           }
                       }

                    }
                }
            }
        }

        // Конец кода по поиску пересечений в отпусках

        //        Запуск генератора отпусков
        //VacationGenerator.createVocs(session)

        // Удалить все записи в таблице отпусков
//        List<Vacation> vacs;
//        vacs = (List<Vacation>) session.createQuery("FROM Vacation").list();
//        for(Vacation vac : vacs){
//            session.delete(vac);
//        }


        // вывести ошибочно привязанных сотрудников, которые работают в одних отделах, а руководят другими;
//        List <Object []> employees = (List <Object []>) session.createQuery(
//                 "SELECT d.name, e.name, e.department.name FROM Department d JOIN d.employee e WHERE d.id != e.department.id"
//        ).list();
//        for(Object data [] : employees){
//            System.out.println(data[0]+" "+data[1]+" "+data[2]);
//        }
        //вывести руководителей отделов, зарплата которых составляет менее 115 000 рублей в месяц;
//        List <Object []> employees = (List <Object []>) session.createQuery(
//                 "SELECT d.name, e.name, e.salary FROM Department d JOIN d.employee e WHERE e.salary <115000"
//        ).list();
//        for(Object data [] : employees){
//            System.out.println(data[0]+" "+data[1]+" "+data[2]);
//        }
        //руководители отделов, которые утсроились на работу до марта 2010
//        Calendar cal = Calendar.getInstance();
//        cal.set(2010, 04, 1);
//        Date maxModDate = cal.getTime();
//        List <Object []> employees = (List <Object []>) session.createQuery(
//                "SELECT d.name, e.name FROM Department d JOIN d.employee e WHERE e.hireDate < :maxModDate"
//        ).setParameter("maxModDate", maxModDate).list();
//        for(Object data [] : employees){
//            System.out.println(data[0]+" "+data[1]);
//        }

        session.getTransaction().commit();
        session.close();

        //==================================================================
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    //=====================================================================



    private static void setUp() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure(new File("src/config/hibernate.cfg.xml")) // configures settings from hibernate.config.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy(registry);
            throw e;
        }
    }
}
