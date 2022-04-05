package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Apartment;

import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    boolean existsByAreaAndTown_TownName(Double area, String town_townName);

    Optional<Apartment> findApartmentById(Long id);

}
