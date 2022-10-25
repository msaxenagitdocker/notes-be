package dao;

import domain.Image;
import domain.ImageId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 */
public interface ImageRepository extends JpaRepository<Image, ImageId> {
    List<Image> findByWordId(String wordId);
    List<Image> findByWordIdOrderByDateTimeAsc(String wordId);
    Image findByImageId(String imageId);
    Image findByWordIdAndImageId(String wordId, String imageId);
    boolean deleteImageByWordIdAndImageId(String wordId, String imageId);
    boolean deleteByWordId(String wordId);
}
