import com.google.protobuf.EmptyProto;
import entities.Address;
import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;

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
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            entityManager.close();
        }
    }

    private void exercise13RemoveTowns() {
        // TO DO
    }

    private void exercise12EmployeesMaximumSalaries() {
        // TO DO
    }

    private void exercise11FindEmployeesByFirstName() {
        // TO DO
    }

    private void exercise10IncreaseSalaries() {

    }

    private void exercise9FindLatest10Projects() {
        // TO DO
    }

    private void exercise8GetEmployeeWithProject() {
        Employee employee = entityManager.find(Employee.class, 147);
        // TO DO
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
