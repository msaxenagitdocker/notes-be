package util.worker;

import dao.VideoRepository;
import domain.VideoId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder @AllArgsConstructor
public class DeleteVideoThread extends Thread {

    private VideoRepository videoRepository;
    private VideoId videoId;

    @Override
    public void run() {
        log.info("deleting {}", videoId);
        videoRepository.deleteById(videoId);
    }
}
