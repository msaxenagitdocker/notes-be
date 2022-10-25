package dao;

import domain.Meaning;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeaningRepository extends JpaRepository<Meaning, String> {
    List<Meaning> findByWord(String wordId);
    List<Meaning> findByWordOrderByDate(String wordId);
    List<Meaning> findByWordOrderByPriorityDesc(String wordId);
}
