package softuni.exam.instagraphlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.instagraphlite.models.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserName(String userName);

    Optional<User> findUserByUserName(String userName);

    //    @Query("SELECT u FROM User u " +
//            "ORDER BY SIZE(u.post) DESC, u.id")
    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.posts p " +
            "ORDER BY SIZE(p) DESC, u.id")
    List<User> findAllByPostsCountOrderByPostsDescThenByUserId();

}
