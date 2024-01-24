package AlexBorzilov.newsfeed.dto;

import java.util.Set;

import AlexBorzilov.newsfeed.error.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NewsDto {
    @Size(min = 3, max = 160, message = ValidationConstants.NEWS_DESCRIPTION_SIZE_NOT_VALID)
    @NotBlank(message = ValidationConstants.NEWS_DESCRIPTION_HAS_TO_BE_PRESENT)
    private String description;
    @Size(min = 3, max = 130, message = ValidationConstants.NEWS_IMAGE_HAS_TO_BE_PRESENT)
    @NotBlank(message = ValidationConstants.NEWS_IMAGE_HAS_TO_BE_PRESENT)
    private String image;
    Set<@NotBlank(message = ValidationConstants.TAGS_NOT_VALID)
    @Size(min = 3, max = 150, message = ValidationConstants.TAGS_NOT_VALID) String> tags;
    @Size(min = 3, max = 160, message = ValidationConstants.NEWS_TITLE_SIZE)
    @NotBlank(message = ValidationConstants.NEWS_TITLE_NOT_NULL)
    private String title;
}
