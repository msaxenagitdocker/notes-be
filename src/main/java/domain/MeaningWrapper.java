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
public class MeaningWrapper {
    private String id;
    private String word;
    private String meaning;
    private Date date;
    private boolean isAttachedFilesAvailable;
    private boolean isVideoAvailable;
    private boolean isAudioAvailable;
    private List<GenericFile> attachedFileList;
    private List<Video> videoList;
    private List<Audio> audioList;
    private int priority;
}
