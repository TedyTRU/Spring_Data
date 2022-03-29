package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.domain.entities.Picture;

@Repository
public interface PictureRepository  extends JpaRepository<Picture, Long> {

    Picture findPictureByUrl(String url);

    boolean existsByUrl(String url);
}
