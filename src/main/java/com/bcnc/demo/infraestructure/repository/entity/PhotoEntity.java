package com.bcnc.demo.infraestructure.repository.entity;

import com.bcnc.demo.domain.Photo;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoEntity {

    @Id
    private Long id;

    private String title;

    private String url;

    private String thumbnailUrl;

    @ManyToOne
    private AlbumEntity album;

    public static PhotoEntity fromDomain(Photo photo) {

        return PhotoEntity.builder()
                .id(photo.getId())
                .title(photo.getTitle())
                .url(photo.getUrl())
                .thumbnailUrl(photo.getThumbnailUrl())
                .build();
    }

    public Photo toDomain() {

        return Photo.builder()
                .id(this.getId())
                .title(this.getTitle())
                .url(this.getUrl())
                .thumbnailUrl(this.getThumbnailUrl())
                .build();
    }
}
