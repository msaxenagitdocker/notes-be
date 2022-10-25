package domain;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class WordWrapper {
    private String id;
    private String word;
    private String type;
    private Date date;
    private boolean isVideoAvailable;
    private boolean isAudioAvailable;
    private boolean isMeaningAvailable;
    private boolean isAttachedFilesAvailable;
    private List<Video> videoList;
    private List<Audio> audioList;
    private List<GenericFile> attachedFileList;
    private int meaningCount;
    private int priority;
}
