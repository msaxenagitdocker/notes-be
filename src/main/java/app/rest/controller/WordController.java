package app.rest.controller;

import util.worker.DeleteVideoThread;
import dao.*;
import domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import util.Util;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/wordController")
public class WordController {

    @Autowired
    DataSource datasource;

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private MeaningRepository meaningRepository;

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private GenericFileRepository genericFileRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private TimerRepository timerRepository;

    @PostMapping(value = "/changeWordCategory")
    public boolean changeWordCategory(@RequestBody Word word) {
        Optional<Word> ifAny = wordRepository.findById(word.getId());
        if(ifAny.isPresent()) {
            Word currentWord = wordRepository.findById(word.getId()).get();
            currentWord.setType(word.getType());
            wordRepository.save(currentWord);
        }
        return true;
    }

    @GetMapping(value = "/priorityPlus")
    public boolean plus(@RequestParam String id) {
        Word word = wordRepository.findById(id).get();
        word.setPriority(word.getPriority() + 1);
        wordRepository.save(word);
        return true;
    }

    @GetMapping(value = "/priorityMinus")
    public boolean minus(@RequestParam String id) {
        Word word = wordRepository.getById(id);
        word.setPriority(word.getPriority() - 1);
        wordRepository.save(word);
        return true;
    }

    @GetMapping("/wordByQuery")
    public List<WordWrapper> wordByQuery(
            @RequestParam(name="query") String query,
            @RequestParam(name="startCounter") Integer startCounter,
            @RequestParam(name="type") String type,
            @RequestParam(name="isMeaningSearch") boolean isMeaningSearch,
            @RequestParam(name="order") String order,
            @RequestParam(name="isPrioritySearch") boolean isPrioritySearch) {

        Map<String, Word> wordsMap = new HashMap<>();
        List<Word> wordsList = new ArrayList<>();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        final Integer pageSize = 10;
        String sortOrder = "ASC".equalsIgnoreCase(order) ? "ASC" : "DESC";
        String QUERY = "select * from svenska.word where word like (?) ";

        String isPrioritySearchFlag = " ORDER BY datetime ";
        if(isPrioritySearch) {
            isPrioritySearchFlag = " ORDER BY priority ";
        }
        String SUFFIX = isPrioritySearchFlag + sortOrder + " limit " + startCounter + ", " + pageSize;

        try {
            con = datasource.getConnection();

            if(type.equalsIgnoreCase("ALL")) {
                stmt = con.prepareStatement(QUERY + SUFFIX);
                stmt.setString(1, "%" + query.toLowerCase() + "%");
            } else {
                stmt = con.prepareStatement(QUERY + " and type = ? " + SUFFIX);
                stmt.setString(1, "%" + query.toLowerCase() + "%");
                stmt.setString(2, type);
            }

            rs = stmt.executeQuery();
            Word w;
            while(rs.next()) {
                String id = rs.getString("id");
                String word = rs.getString("word");
                String wordType = rs.getString("type");
                int priority = rs.getInt("priority");
                w = Word.builder().priority(priority).id(id).word(word).type(wordType).build();
                wordsList.add(w);
                wordsMap.put(id, w);
            }
            //wordRepository.findByWordIgnoreCaseContaining(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Util.closeAll(rs, stmt, con);
        }


        if(isMeaningSearch && !query.equals("%")) {
            log.info("search meaning query ::: {}", query);
            String wordIds = "";
            try {
                con = datasource.getConnection();
                stmt = con.prepareStatement("select word from svenska.meaning where lower(meaning) like (?) limit " + startCounter + ", " + pageSize);
                stmt.setString(1, "%" + query.toLowerCase() + "%");
                rs = stmt.executeQuery();

                while(rs.next()) {
                    wordIds += "'" + rs.getString("word") + "',";
                }


                if(wordIds.length() > 0) {

                    wordIds = wordIds.substring(0, wordIds.length() - 1);
                    Connection con1 = null;
                    try {
                        con1 = datasource.getConnection();

                        // TODO there should be a search meaning query with or without type
                        //stmt = con.prepareStatement("select * from svenska.word where id in (" + wordIds + ") AND type = ? " + SUFFIX);
                        stmt = con.prepareStatement("select * from svenska.word where id in (" + wordIds + ") " + SUFFIX);

                        //stmt.setString(1, type);
                        rs = stmt.executeQuery();
                        Word w;
                        while(rs.next()) {
                            String id = rs.getString("id");
                            String word = rs.getString("word");
                            String wordType = rs.getString("type");
                            int priority = rs.getInt("priority");

                            wordsMap.put(id, Word.builder().priority(priority).id(id).word(word).type(wordType).build());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Util.closeAll(rs, stmt, con1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Util.closeAll(rs, stmt, con);
            }
        }

        // TODO - from frontend give call to these word ids one by one and lazy load all their dependencies
        Word w;
        for (String key : wordsMap.keySet()) {
            w = wordsMap.get(key);
            if(!wordsList.contains(w)) {
                wordsList.add(w);
            }
        }

        List<WordWrapper> responseWordsList = new ArrayList<>();
        wordsList.forEach(word -> {
            WordWrapper wrapper = new WordWrapper();
            wrapper.setWord(word.getWord());
            wrapper.setId(word.getId());
            wrapper.setDate(word.getDate());
            wrapper.setType(word.getType());
            wrapper.setPriority(word.getPriority());

            wrapper.setAttachedFilesAvailable(false);
            wrapper.setAudioAvailable(false);
            wrapper.setVideoAvailable(false);
            wrapper.setMeaningAvailable(false);
            wrapper.setAttachedFilesAvailable(false);

            responseWordsList.add(wrapper);
        });
        return responseWordsList;
    }

    @GetMapping("/wordCount")
    public Integer wordCount(@RequestParam(name="query") String query,
                                  @RequestParam(name="type") String type) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = datasource.getConnection();
            if("".equals(type) || "ALL".equals(type)) {
                stmt = con.prepareStatement("select count(*) from svenska.word where word like (?)");
                stmt.setString(1, "%" + query.toLowerCase() + "%");
            } else {
                stmt = con.prepareStatement("select count(*) from svenska.word where word like (?) and type = ?");
                stmt.setString(1, "%" + query.toLowerCase() + "%");
                stmt.setString(2, type);
            }

            rs = stmt.executeQuery();
            while(rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Util.closeAll(rs, stmt, con);
        }
        return 0;
    }

    @GetMapping(value = "/editWord")
    public boolean editWord(@RequestParam(name="wordId") String wordId, @RequestParam(name="newValue") String newValue) {
        Assert.notNull(wordId, "wordId is null");
        try {
            Word w = wordRepository.findById(wordId).get();
            w.setWord(newValue);
            wordRepository.saveAndFlush(w);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @PostMapping(value = "/saveWord")
    public String saveWord(@RequestBody Word word) {
        Assert.notNull(word, "word is null");
        log.info("saving word {}", word);
        try {
            word.setId(UUID.randomUUID().toString());
            word.setDate(new Date());
            wordRepository.saveAndFlush(word);
            return word.getId();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void deleteAllVideosByRefId(String refId) {
        videoRepository.getVideoIdByRefId(refId).forEach(objects -> {
            String videoId = objects[1].toString();
            DeleteVideoThread.builder().videoRepository(videoRepository).videoId(VideoId.builder().refId(refId).videoId(videoId).build()).build().start();
        });
    }

    @DeleteMapping(value = "/deleteWord")
    public BaseResponse deleteWord(@RequestParam(name="wordId") String wordId) {
        log.info("deleteWord wordId {}", wordId);
        try {
            List<Meaning> meanings = meaningRepository.findByWord(wordId);
            log.info("meanings {}", meanings);
            meanings.forEach(meaning ->
                deleteMeaning(meaning.getId())
            );

            // TODO - do not fetch or do a selective fetch and delete quickly
            audioRepository.findByRefIdOrderByDateTime(wordId).forEach(audio ->
                audioRepository.deleteById(AudioId.builder().refId(wordId).audioId(audio.getAudioId()).build())
            );

            // TODO - do not fetch or do a selective fetch and delete quickly
            imageRepository.findByWordId(wordId).forEach(image -> {
                imageRepository.deleteById(ImageId.builder().wordId(wordId).imageId(image.getImageId()).build());
            });

            deleteAllVideosByRefId(wordId);

            // TODO - do not fetch or do a selective fetch and delete quickly
            genericFileRepository.findByLinkId(wordId).forEach(genericFile ->
                    genericFileRepository.deleteById(GenericFileId.builder().id(genericFile.getId()).linkId(wordId).build())
            );

            wordRepository.deleteById(wordId);

            return Util.getSuccessResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Util.getErrorResponse();
    }

    @DeleteMapping(value = "/deleteMeaning")
    public BaseResponse deleteMeaning(@RequestParam(name="meaningId") String meaningId) {
        log.info("Deleting meaning object audio + image + video meaningId {}", meaningId);

        audioRepository.findByRefIdOrderByDateTime(meaningId).forEach(audio ->
                audioRepository.deleteById(AudioId.builder().refId(meaningId).audioId(audio.getAudioId()).build())
        );

        imageRepository.findByWordId(meaningId).forEach(image ->
                imageRepository.deleteById(ImageId.builder().wordId(meaningId).imageId(image.getImageId()).build())
        );

        deleteAllVideosByRefId(meaningId);
        /*
        videoRepository.findByRefIdOrderByDateTime(meaningId).forEach(video ->
                videoRepository.deleteById(VideoId.builder().refId(meaningId).videoId(video.getVideoId()).build())
        );
        */

        genericFileRepository.findByLinkId(meaningId).forEach(genericFile ->
                genericFileRepository.deleteById(GenericFileId.builder().id(genericFile.getId()).linkId(meaningId).build())
        );

        meaningRepository.deleteById(meaningId);

        return Util.getSuccessResponse();
    }
}
