package app.rest.controller;

import dao.ImageRepository;
import domain.Image;
import domain.ImageId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 */
@Slf4j
@RestController
@RequestMapping("/imageController")
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    /**
     *
     * @param image
     * @return
     */
    @PostMapping(value = "/saveOrUpdateImage")
    public boolean saveOrUpdateImage(@RequestBody Image image) {
        image.setImageId(UUID.randomUUID().toString());
        image.setDateTime(new Date());
        imageRepository.saveAndFlush(image);
        return true;
    }

    /**
     *
     * @param wordId
     * @param imageId
     * @return
     */
    @GetMapping(value = "/deleteImageByWordIdAndImageId")
    public boolean deleteImageByWordIdAndImageId(@RequestParam String wordId, @RequestParam String imageId) {
        log.info("deleteImageByWordIdAndImageId, wordId {}, imageId {}", wordId, imageId);
        if(imageRepository.findById(ImageId.builder().wordId(wordId).imageId(imageId).build()).isPresent()) {
            imageRepository.deleteById(ImageId.builder().wordId(wordId).imageId(imageId).build());
        }
        return true;
    }

    /**
     *
     * @param imageId
     * @param id
     * @return
     */
    @GetMapping(value = "/copyImageTo")
    public boolean copyImageTo(@RequestParam String imageId, @RequestParam String id) {
        Image image = imageRepository.findByImageId(imageId);
        Image newImage = Image.builder().image(image.getImage()).wordId(id).imageId(UUID.randomUUID().toString()).dateTime(new Date()).build();
        imageRepository.saveAndFlush(newImage);
        return true;
    }

    /**
     *
     * @param imageId
     * @param id
     * @return
     */
    @GetMapping(value = "/copyImageToAndDelete")
    public boolean copyImageToAndDelete(@RequestParam String imageId, @RequestParam String id) {
        Image image = imageRepository.findByImageId(imageId);
        Image newImage = Image.builder().image(image.getImage()).wordId(id).imageId(UUID.randomUUID().toString()).dateTime(new Date()).build();
        imageRepository.saveAndFlush(newImage);
        deleteImageByWordIdAndImageId(image.getWordId(), image.getImageId());
        return true;
    }

    /**
     *
     * @param wordId
     * @param imageId
     * @return
     */
    @GetMapping(value = "/getImageByWordIdAndImageId")
    public Image getImageByWordIdAndImageId(@RequestParam String wordId, @RequestParam String imageId) {
        return imageRepository.findByWordIdAndImageId(wordId, imageId);
    }

    /**
     *
     * @param meaningId
     * @param imageId
     * @return
     */
    @GetMapping(value = "/deleteImageByMeaningIdAndImageId")
    public boolean deleteImageByMeaningIdAndImageId(@RequestParam String meaningId, @RequestParam String imageId) {
        log.info("deleteImageByMeaningIdAndImageId, meaningId {}, imageId {}", meaningId, imageId);
        if(imageRepository.findById(ImageId.builder().wordId(meaningId).imageId(imageId).build()).isPresent()) {
            imageRepository.deleteById(ImageId.builder().wordId(meaningId).imageId(imageId).build());
        }
        return true;
    }

    /**
     *
     * @param meaningId
     * @param imageId
     * @return
     */
    @GetMapping(value = "/getImageByMeaningIdAndImageId")
    public Image getImageByMeaningIdAndImageId(@RequestParam String meaningId, @RequestParam String imageId) {
        return imageRepository.findByWordIdAndImageId(meaningId, imageId);
    }

    /**
     *
     * @param wordId
     * @return
     */
    @GetMapping(value = "/getAllImagesByWordId")
    public List<Image> getAllImagesByWordId(@RequestParam String wordId) {
        Assert.notNull(wordId, "wordId is null");
        return imageRepository.findByWordIdOrderByDateTimeAsc(wordId);
    }

    /**
     *
     * @param meaningId
     * @return
     */
    @GetMapping(value = "/getAllImagesByMeaningId")
    public List<Image> getAllImagesByMeaningId(@RequestParam String meaningId) {
        return imageRepository.findByWordIdOrderByDateTimeAsc(meaningId);
    }


}
