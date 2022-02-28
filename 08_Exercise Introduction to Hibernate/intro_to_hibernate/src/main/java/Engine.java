import com.google.protobuf.EmptyProto;
import entities.Address;
import entities.Department;
import entities.Employee;
import entities.Project;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Engine implements Runnable {

    private final EntityManager entityManager;
    private final BufferedReader bufferedReader;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        System.out.println("Enter exercise number: ");

        try {
            int exNum = Integer.parseInt(bufferedReader.readLine());

            switch (exNum) {
                case 2 -> exercise2ChangeCasing();
                case 3 -> exercise3ContainsEmployee();
                case 4 -> exercise4EmployeesWithSalaryOver50000();
                case 5 -> exercise5EmployeesFromDepartment();
                case 6 -> exercise6AddingANewAddressAndUpdatingEmployee();
                case 7 -> exercise7AddressesWithEmployeeCount();
                case 8 -> exercise8GetEmployeeWithProject();
                case 9 -> exercise9FindLatest10Projects();
                case 10 -> exercise10IncreaseSalaries();
                case 11 -> exercise11FindEmployeesByFirstName();
                case 12 -> exercise12EmployeesMaximumSalaries();
                case 13 -> exercise13RemoveTowns();
                default -> System.out.println("Invalid exercise number");
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            entityManager.close();
        }
    }

    private void exercise13RemoveTowns() throws IOException {
        System.out.println("Enter town name: ");
        String townName = bufferedReader.readLine();

    }

    private void exercise12EmployeesMaximumSalaries() {
//        List<Employee> employees = entityManager
//                .createQuery("SELECT e FROM Employee e " +
//                        "WHERE e.salary NOT BETWEEN 30000 AND 70000", Employee.class)
//                .getResultList();
//
//        Set<Department> departments = employees.stream()
//                .map(Employee::getDepartment).collect(Collectors.toSet());
//
//        for (Department d : departments) {
//            BigDecimal max = BigDecimal.valueOf(0);
//
//            for (Employee e : employees) {
//                if (e.getDepartment().getId().equals(d.getId())) {
//                    if (max.compareTo(e.getSalary()) < 1) {
//                        max = e.getSalary();
//                    }
//                }
//            }
//            System.out.printf("%s %.2f%n", d.getName(), max);
//        }

        List<Object[]> rows = entityManager.createNativeQuery("SELECT d.name, MAX(e.salary) FROM employees AS e " +
                        "JOIN departments d on d.department_id = e.department_id " +
                        "GROUP BY e.department_id " +
                        "HAVING MAX(e.salary) NOT BETWEEN 30000 AND 70000 ")
                .getResultList();

        rows.forEach(r -> System.out.printf("%s %.2f%n", r[0], r[1]));

    }

    private void exercise11FindEmployeesByFirstName() {
        entityManager.getTransaction().begin();

        int affectedRows = entityManager.createQuery("UPDATE Employee e " +
                        "SET e.salary = e.salary * 1.12 " +
                        "WHERE e.department.id IN :ids")
                .setParameter("ids", Set.of(1, 2, 4, 11))
                .executeUpdate();

        entityManager.getTransaction().commit();
        System.out.println(affectedRows);

        List<Employee> employees = entityManager.createQuery("SELECT e FROM Employee e " +
                        "WHERE e.department.id IN :ids", Employee.class)
                .setParameter("ids", Set.of(1, 2, 4, 11))
                .getResultList();

        employees
                .forEach(e -> {
                    System.out.printf("%s %s ($%.2f)%n",
                            e.getFirstName(), e.getLastName(), e.getSalary());
                });

    }

    private void exercise10IncreaseSalaries() {
        entityManager.getTransaction().begin();

        int affectedRows = entityManager.createQuery("UPDATE Employee e " +
                        "SET e.salary = e.salary * 1.2 " +
                        "WHERE e.department.id IN :ids")
                .setParameter("ids", Set.of(1, 2, 4, 11))
                .executeUpdate();

        entityManager.getTransaction().commit();

        System.out.println(affectedRows);
    }

    private void exercise9FindLatest10Projects() {

        List<Project> projects = entityManager.createQuery("SELECT p FROM Project p " +
                        "ORDER BY p.startDate DESC ", Project.class)
                .setMaxResults(10)
                .getResultList();

        projects.stream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(p -> System.out.printf("Project name: %s%n\tProject Description: %s ...%n" +
                                "\tProject Start Date: %s%n\tProject End Date: %s%n",
                        p.getName(), p.getDescription().substring(0, 35), p.getStartDate(), p.getEndDate()));
    }

    private void exercise8GetEmployeeWithProject() throws IOException {
        System.out.println("Enter employee id: ");
        int employeeId = Integer.parseInt(bufferedReader.readLine());

        try {
            Employee employee = entityManager.find(Employee.class, employeeId);

            System.out.printf("%s %s - %s%n",
                    employee.getFirstName(), employee.getLastName(), employee.getJobTitle());

            employee.getProjects().stream()
                    .sorted(Comparator.comparing(Project::getName))
                    .forEach(p -> System.out.println(p.getName()));

        } catch (Exception e) {
            System.out.println("Invalid employee id");
        }
    }

    private void exercise7AddressesWithEmployeeCount() {

        List<Address> addresses = entityManager.createQuery("SELECT a FROM Address a " +
                        "ORDER BY a.employees.size DESC ", Address.class)
                .setMaxResults(10)
                .getResultList();

        addresses
                .forEach(a -> System.out.printf("%s, %s - %d employees%n",
                        a.getText(),
                        a.getTown() == null ? "Unknown" : a.getTown().getName(),
                        a.getEmployees().size()));
    }

    private void exercise6AddingANewAddressAndUpdatingEmployee() throws IOException {
        System.out.println("Enter employee last name: ");
        String lastName = bufferedReader.readLine();

        Employee employee = entityManager
                .createQuery("SELECT e FROM Employee e " +
                        "WHERE e.lastName = :l_name", Employee.class)
                .setParameter("l_name", lastName)
                .getSingleResult();

        Address address = createAddress("Vitoshka 15");

        entityManager.getTransaction().begin();
        employee.setAddress(address);
        entityManager.getTransaction().commit();

    }

    private Address createAddress(String addressText) {
        Address address = new Address();
        address.setText(addressText);

        entityManager.getTransaction().begin();
        entityManager.persist(address);
        entityManager.getTransaction().commit();

        return address;
    }

    private void exercise5EmployeesFromDepartment() {
        entityManager
                .createQuery("SELECT e FROM Employee e " +
                        "WHERE e.department.name = :department_name " +
                        "ORDER BY e.salary, e.id", Employee.class)
                .setParameter("department_name", "Research and Development")
                .getResultStream()
                .forEach(e -> {
                    System.out.printf("%s %s from %s - $%.2f%n", e.getFirstName(),
                            e.getLastName(), e.getDepartment().getName(), e.getSalary());
                });
    }

    private void exercise4EmployeesWithSalaryOver50000() {

//        List<Employee> employees = entityManager
//                .createQuery("SELECT e FROM Employee e " +
//                        "WHERE e.salary > :min_salary", Employee.class)
//                .setParameter("min_salary", BigDecimal.valueOf(50000L))
//                .getResultList();

        entityManager
                .createQuery("SELECT e FROM Employee e " +
                        "WHERE e.salary > :min_salary", Employee.class)
                .setParameter("min_salary", BigDecimal.valueOf(50000L))
                .getResultStream()
                .map(Employee::getFirstName)
                .forEach(System.out::println);

    }

    private void exercise3ContainsEmployee() throws IOException {
        System.out.println("Enter employee full name: ");
        String[] fullName = bufferedReader.readLine().split("\\s+");
        String firstName = fullName[0];
        String lastName = fullName[1];

//            Employee employee = entityManager
//                    .createQuery("SELECT e FROM Employee e " +
//                            "WHERE e.firstName = :f_name AND e.lastName = :l_name", Employee.class)
//                    .setParameter("f_name", firstName)
//                    .setParameter("l_name", lastName)
//                    .getSingleResult();

        Long employee = entityManager
                .createQuery("SELECT COUNT(e) FROM Employee e " +
                        "WHERE e.firstName = :f_name AND e.lastName = :l_name", Long.class)
                .setParameter("f_name", firstName)
                .setParameter("l_name", lastName)
                .getSingleResult();

        System.out.println(employee == 0 ? "No" : "Yes");
    }

    private void exercise2ChangeCasing() {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("UPDATE Town t " +
                "SET t.name = UPPER(t.name) " +
                "WHERE LENGTH(t.name) < 5 ");

        int affectedRows = query.executeUpdate();
        System.out.println(affectedRows);

        entityManager.getTransaction().commit();
    }
}
