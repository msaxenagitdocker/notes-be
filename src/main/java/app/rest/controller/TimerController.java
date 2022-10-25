package app.rest.controller;

import dao.TimerRepository;
import domain.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 */
@Slf4j
@RestController
@RequestMapping("/timerController")
public class TimerController {

    @Autowired
    private TimerRepository timerRepository;

    /**
     *
     * @param timer
     * @return
     */
    @PostMapping(value = "/saveOrUpdateTime")
    public boolean saveOrUpdateImage(@RequestBody Timer timer) {
        log.info("saveOrUpdateTime {}", timer);
        timerRepository.saveAndFlush(timer);
        return true;
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/deleteTimerById")
    public boolean deleteTimerById(@RequestParam String id) {
        log.info("deleteTimerById, id {}", id);
        if(timerRepository.findById(id).isPresent()) {
            timerRepository.deleteById(id);
        }
        return true;
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getTimeById")
    public String getTimeById(@RequestParam String id) {
        Timer timer = timerRepository.getOne(id);
        if(timer == null || timer.getTime() == null) {
            return null;
        }
        return timer.getTime();
    }

    /**
     *
     * @return
     */
    @GetMapping(value = "/getAllTimer")
    public List<Timer> getAllTimer() {
        return timerRepository.findAll();
    }

}
