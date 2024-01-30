package aborzilov.newsfeed.dto;
import java.util.Set;
import java.util.UUID;

import aborzilov.newsfeed.view.TagView;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class GetNewsOutDto {
    private String description;
    private Long id;
    private String image;
    private Set<TagView> tags;
    private String title;
    private UUID userId;
    private String username;
}
