package dao;

import domain.GenericFile;
import domain.GenericFileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface GenericFileRepository extends JpaRepository<GenericFile, GenericFileId> {
    List<GenericFile> findByLinkId(String wordOrMeaningId);

    @Query(value="select id, fileName from genericfile v where v.linkId =:linkId order by datetime", nativeQuery=true)
    List<Object[]> getSelectedColumnsByLinkId(String linkId);
}
