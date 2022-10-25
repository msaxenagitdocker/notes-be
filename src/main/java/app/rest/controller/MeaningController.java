package app.rest.controller;

import dao.MeaningRepository;
import domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/meaningController")
public class MeaningController {

    @Autowired
    DataSource datasource;

    @Autowired
    private MeaningRepository meaningRepository;

    @GetMapping(value = "/priorityPlus")
    public boolean plus(@RequestParam String id) {
        Meaning meaning = meaningRepository.getById(id);
        meaning.setPriority(meaning.getPriority() + 1);
        meaningRepository.save(meaning);
        return true;
    }

    @GetMapping(value = "/priorityMinus")
    public boolean minus(@RequestParam String id) {
        Meaning meaning = meaningRepository.getById(id);
        meaning.setPriority(meaning.getPriority() - 1);
        meaningRepository.save(meaning);
        return true;
    }

    @GetMapping(value = "/meaningsByWordId")
    public List<MeaningWrapper> meaningsByWordId(@RequestParam String wordId, @RequestParam String sortBy) {
        Assert.notNull(wordId, "wordId is null");
        List<MeaningWrapper> meanings = new ArrayList<>();
        List<Meaning> listOfMeanings;

        if("byDate".equals(sortBy)) {
            listOfMeanings = meaningRepository.findByWordOrderByDate(wordId);
        } else {
            listOfMeanings = meaningRepository.findByWordOrderByPriorityDesc(wordId);
        }

        listOfMeanings.forEach(meaning -> {
            MeaningWrapper obj = MeaningWrapper.builder()
                    .priority(meaning.getPriority())
                    .id(meaning.getId()).word(meaning.getWord()).meaning(meaning.getMeaning())
                    .date(meaning.getDate()).build();

            obj.setAttachedFilesAvailable(false);
            obj.setVideoAvailable(false);
            obj.setAudioAvailable(false);
            meanings.add(obj);
        });
        return meanings;
    }

    @GetMapping(value = "/onlyMeaningsByWordId")
    public List<Meaning> onlyMeaningsByWordId(@RequestParam(name="wordId") String wordId) {
        Assert.notNull(wordId, "wordId is null");
        List<Meaning> list = meaningRepository.findByWord(wordId);
        return list;
    }

    @PutMapping(value = "/saveMeaning")
    public boolean saveMeaning(@RequestBody Meaning meaning) {
        Assert.notNull(meaning, "meaning is null");

        try {
            meaning.setId(UUID.randomUUID().toString());
            meaning.setDate(new Date());
            meaningRepository.saveAndFlush(meaning);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping(value = "/editMeaning")
    public boolean editWord(@RequestParam(name="meaningId") String meaningId, @RequestParam(name="newValue") String newValue) {
        Assert.notNull(meaningId, "meaningId is null");

        try {
            Meaning meaning = meaningRepository.getOne(meaningId);
            meaning.setMeaning(newValue);
            meaningRepository.saveAndFlush(meaning);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
