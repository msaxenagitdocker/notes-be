package dao;

import domain.Audio;
import domain.AudioId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AudioRepository extends JpaRepository<Audio, AudioId> {
    List<Audio> findByRefIdOrderByDateTime(String refId);
}
