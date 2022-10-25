package dao;

import domain.Video;
import domain.VideoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, VideoId> {
    List<Video> findByRefIdOrderByDateTime(String refId);

    @Query(value="select refId, videoId from video v where v.refId =:refId", nativeQuery=true)
    List<Object[]> getVideoIdByRefId(@Param("refId") String refId);

    @Query(value="select refId, videoId, description from video v where v.refId =:refId order by datetime", nativeQuery=true)
    List<Object[]> getSelectedColumnsByRefId(@Param("refId") String refId);
}
