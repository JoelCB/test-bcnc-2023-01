package com.bcnc.demo.application.rest.response;

import com.bcnc.demo.domain.Album;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetAlbumResponse {

    private Long id;
    private Long userId;
    private String title;
    private List<GetPhotoResponse> photos;

    public static GetAlbumResponse fromDomain(Album album) {

        GetAlbumResponseBuilder builder = GetAlbumResponse.builder()
                .id(album.getId())
                .userId(album.getUserId())
                .title(album.getTitle());

        if (album.getPhotos() != null) {
            builder.photos(album.getPhotos()
                        .stream()
                        .map(GetPhotoResponse::fromDomain)
                        .toList());
        }

        return builder.build();
    }
}
