package com.bcnc.demo.infraestructure.repository.entity;

import com.bcnc.demo.domain.Album;
import com.bcnc.demo.domain.Photo;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumEntity {

    @Id
    private Long id;

    private Long userId;

    private String title;

    @OneToMany(mappedBy = "album")
    private List<PhotoEntity> photos = Collections.emptyList();

    public static AlbumEntity fromDomain(Album album) {

        AlbumEntity albumEntity = AlbumEntity.builder()
                .id(album.getId())
                .userId(album.getUserId())
                .title(album.getTitle())
                .build();

        if (album.getPhotos() != null) {

            List<PhotoEntity> photos = album.getPhotos()
                    .stream()
                    .map(PhotoEntity::fromDomain)
                    .toList();

            photos.forEach(photo -> photo.setAlbum(albumEntity));

            albumEntity.setPhotos(photos);
        }

        return albumEntity;
    }

    public Album toDomain() {

        Album.AlbumBuilder builder = Album.builder()
                .id(this.getId())
                .userId(this.getUserId())
                .title(this.getTitle());

        if (this.getPhotos() != null) {
            builder.photos(this.getPhotos()
                        .stream()
                        .map(PhotoEntity::toDomain)
                        .peek(photo -> photo.setAlbumId(this.getId()))
                        .toList());
        }

        return builder.build();
    }
}
