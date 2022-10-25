package app.rest.controller;

import dao.GenericFileRepository;
import domain.BaseResponse;
import domain.GenericFile;
import domain.GenericFileId;
import domain.Video;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/genericFileController")
public class GenericFileController {

    @Autowired
    private GenericFileRepository genericFileRepository;

    @PostMapping(value = "/save")
    public GenericFile save(@RequestBody GenericFile genericFile) {
        genericFile.setId(UUID.randomUUID().toString());
        genericFile.setDateTime(new Date());
        genericFileRepository.saveAndFlush(genericFile);
        genericFile.setFileData(null);
        return genericFile;
    }

    @GetMapping(value = "/getAllGenericFilesByLinkId")
    public List<GenericFile> getAllGenericFilesByLinkId(@RequestParam String linkId) {
        List<GenericFile> genericFileList = genericFileRepository.findByLinkId(linkId);
        genericFileList.forEach(genericFile -> genericFile.setFileData(null));
        return genericFileList;
    }

    // TODO check this method
    @GetMapping(value = "/getAllGenericFilesMetadataByLinkId")
    public List<GenericFile> getAllGenericFilesMetadataByLinkId(@RequestParam(name="linkId") String linkId) {
        log.info("getAllGenericFilesMetadataByLinkId linkId {}", linkId);

        List<Object[]> tempGenericFileList = genericFileRepository.getSelectedColumnsByLinkId(linkId);
        log.info("tempGenericFileList {}", tempGenericFileList);

        List<GenericFile> genericFileList = new ArrayList<>();

        for (int i = 0; i < tempGenericFileList.size(); i++) {
            Object[] details = tempGenericFileList.get(i);
            if(details != null && details.length != 0) {
                String id = (String)details[0];
                String fileName = (String)details[1];
                genericFileList.add(GenericFile.builder().linkId(linkId).id(id).fileName(fileName).build());
            }
        }

        return genericFileList;
    }

    @PostMapping(value = "/downloadFilesById")
    public GenericFile downloadFilesByLinkId(@RequestBody GenericFileId fileId) {
        return genericFileRepository.findById(fileId).get();
    }


    @DeleteMapping(value = "/delete")
    public BaseResponse deleteFile(@RequestParam(name="id") String id, @RequestParam(name="linkId") String linkId) {
        GenericFileId fileId = GenericFileId.builder().id(id).linkId(linkId).build();
        if(genericFileRepository.findById(fileId).isPresent()) {
            genericFileRepository.deleteById(fileId);
        }
        return Util.getSuccessResponse();
    }
}
