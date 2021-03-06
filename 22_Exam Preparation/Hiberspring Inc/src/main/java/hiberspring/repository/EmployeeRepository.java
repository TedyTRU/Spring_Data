package hiberspring.repository;

import hiberspring.domain.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByCard_Number(String card_number);

    @Query("SELECT e FROM Employee e " +
            "WHERE SIZE(e.branch.products) > 0 " +
            "ORDER BY e.firstName, e.lastName, LENGTH(e.position) DESC")
    List<Employee> findAllByBranch_ProductsOrderByFirstNameAndPositionLength();

}
