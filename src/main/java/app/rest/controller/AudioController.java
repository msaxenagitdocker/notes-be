package app.rest.controller;

import dao.AudioRepository;
import domain.Audio;
import domain.AudioId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/audioController")
public class AudioController {

    @Autowired
    private AudioRepository audioRepository;

    /**
     *
     * @param audio
     * @return
     */
    @PostMapping(value = "/saveOrUpdateAudio")
    public String saveOrUpdateAudio(@RequestBody Audio audio) {
        audio.setAudio(audio.getAudio().split(",")[1]);
        if(audio.getAudioId() == null) {
            audio.setAudioId(UUID.randomUUID().toString());
            audio.setDateTime(new Date());
        }
        try {
            audioRepository.saveAndFlush(audio);
            return audio.getAudio();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param refId
     * @return
     */
    @GetMapping(value = "/audioByRefIdOrderByDateTime")
    public List<Audio> wordAudio(@RequestParam(name="refId") String refId) {
        Assert.notNull(refId, "refId is null");
        List<Audio> audioList = audioRepository.findByRefIdOrderByDateTime(refId);
        audioList.forEach(audio -> audio.setAudio(null));
        return audioList;
    }

    /**
     *
     * @param audio
     * @return
     */
    @GetMapping(value = "/audioById")
    public Audio wordAudio(Audio audio) {
        Assert.notNull(audio, "audio is null");
        Optional<Audio> audioObj = audioRepository.findById(AudioId.builder().refId(audio.getRefId()).audioId(audio.getAudioId()).build());
        if(audioObj.isPresent()) {
            return audioObj.get();
        }
        return null;
    }

    /**
     *
     * @param refId
     * @return
     */
    @GetMapping(value = "/deleteAudioById")
    public boolean deleteAudioById(@RequestParam(name="refId") String refId, @RequestParam(name="audioId") String audioId) {
        Assert.notNull(refId, "RefId is null");
        AudioId auId = AudioId.builder().audioId(audioId).refId(refId).build();
        if(audioRepository.findById(auId).isPresent()) {
            audioRepository.deleteById(auId);
            return true;
        }
        return false;
    }
}
