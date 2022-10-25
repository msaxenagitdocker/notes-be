package app.rest.controller;

import dao.VideoRepository;
import domain.BaseResponse;
import domain.Video;
import domain.VideoId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.Util;

import java.util.*;

@RestController
@RequestMapping("/videoController")
public class VideoController {

    @Autowired
    private VideoRepository videoRepository;

    // TODO currently video id = word id or meaning id
    @PostMapping(value = "/saveOrUpdateVideo")
    public String saveOrUpdateVideo(@RequestBody Video video) {
        video.setVideoId(UUID.randomUUID().toString());
        video.setDateTime(new Date());
        videoRepository.saveAndFlush(video);
        return null;
    }

    @GetMapping(value = "/wordVideo")
    public String wordVideo(@RequestParam(name="wordOrMeaningId") String wordOrMeaningId, @RequestParam(name="videoId") String videoId) {
        Optional<Video> video = videoRepository.findById(VideoId.builder().refId(wordOrMeaningId).videoId(videoId).build());
        if(video.isPresent()) {
            return video.get().getVideo();
        }
        return null;
    }

    @GetMapping(value = "/videosBywordOrMeaningId")
    public List<Video> wordVideo(@RequestParam(name="wordOrMeaningId") String wordOrMeaningId) {
        List<Object[]> videoList = videoRepository.getSelectedColumnsByRefId(wordOrMeaningId);

        List<Video> videoListResponse = new ArrayList<>();

        for (int i = 0; i < videoList.size(); i++) {
            Object[] videoDetails = videoList.get(i);
            if(videoDetails != null && videoDetails.length != 0) {
                String refId = (String)videoDetails[0];
                String videoId = (String)videoDetails[1];
                String description = (String)videoDetails[2];
                videoListResponse.add(Video.builder().refId(refId).videoId(videoId).description(description).build());
            }
        }
        return videoListResponse;
    }

    @DeleteMapping(value = "/deleteVideo")
    public BaseResponse deleteVideo(@RequestParam(name="wordOrMeaningId") String wordOrMeaningId, @RequestParam(name="videoId") String videoId) {
        if(videoRepository.findById(VideoId.builder().refId(wordOrMeaningId).videoId(videoId).build()).isPresent()) {
            videoRepository.deleteById(VideoId.builder().refId(wordOrMeaningId).videoId(videoId).build());
        }
        return Util.getSuccessResponse();
    }
}
