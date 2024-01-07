package com.bcnc.demo.infraestructure.api.model;

import com.bcnc.demo.domain.Photo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoExternalModel {

    @Id
    private Long id;

    private Long albumId;

    private String title;

    private String url;

    private String thumbnailUrl;

    public static PhotoExternalModel fromDomain(Photo photo) {

        return PhotoExternalModel.builder()
                .id(photo.getId())
                .albumId(photo.getAlbumId())
                .title(photo.getTitle())
                .url(photo.getUrl())
                .thumbnailUrl(photo.getThumbnailUrl())
                .build();
    }

    public Photo toDomain() {

        return Photo.builder()
                .id(this.getId())
                .albumId(this.getAlbumId())
                .title(this.getTitle())
                .url(this.getUrl())
                .thumbnailUrl(this.getThumbnailUrl())
                .build();
    }
}
