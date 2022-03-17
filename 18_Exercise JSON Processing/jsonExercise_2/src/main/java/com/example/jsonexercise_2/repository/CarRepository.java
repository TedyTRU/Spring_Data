package com.example.jsonexercise_2.repository;

import com.example.jsonexercise_2.model.entity.Car;
import com.example.jsonexercise_2.model.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("SELECT c FROM Car c WHERE c.make = ?1 ORDER BY c.model, c.travelledDistance DESC")
    List<Car> findAllByMakeOrderByModelThenByTravelledDistanceDesc(String make);

}
