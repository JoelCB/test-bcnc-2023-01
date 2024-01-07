package com.bcnc.demo.application.rest.response;

import com.bcnc.demo.domain.Photo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetPhotoResponse {

    private Long id;
    private Long albumId;
    private String title;
    private String url;
    private String thumbnailUrl;

    public static GetPhotoResponse fromDomain(Photo photo) {

        return GetPhotoResponse.builder()
                .id(photo.getId())
                .albumId(photo.getAlbumId())
                .title(photo.getTitle())
                .url(photo.getUrl())
                .thumbnailUrl(photo.getThumbnailUrl())
                .build();
    }
}
