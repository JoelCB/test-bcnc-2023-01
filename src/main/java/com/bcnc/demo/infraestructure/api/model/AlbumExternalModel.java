package com.bcnc.demo.infraestructure.api.model;

import com.bcnc.demo.domain.Album;
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
public class AlbumExternalModel {

    @Id
    private Long id;

    private Long userId;

    private String title;


    public static AlbumExternalModel fromDomain(Album album) {

        return AlbumExternalModel.builder()
                .id(album.getId())
                .userId(album.getUserId())
                .title(album.getTitle())
                .build();
    }
    
    public Album toDomain() {

        return Album.builder()
                .id(this.getId())
                .userId(this.getUserId())
                .title(this.getTitle())
                .build();
    }
}
