package com.bcnc.demo.infraestructure.api;

import com.bcnc.demo.domain.Album;
import com.bcnc.demo.domain.Photo;
import com.bcnc.demo.domain.apiclient.AlbumApiClient;
import com.bcnc.demo.domain.exception.NotFoundException;
import com.bcnc.demo.infraestructure.api.model.AlbumExternalModel;
import com.bcnc.demo.infraestructure.api.model.PhotoExternalModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlbumApiClientImpl implements AlbumApiClient {

    private final RestClient restClient;

    @Value("${external.api.url}")
    private String url;

    @Value("${external.api.albumPath}")
    private String albumPath;

    @Value("${external.api.photoPath}")
    private String photoPath;

    @Override
    public Optional<Album> findById(Long id) {

        String uri = UriComponentsBuilder
                .fromHttpUrl(url)
                .path(albumPath)
                .path(String.format("/%d", id))
                .toUriString();

        AlbumExternalModel albumExternalModel = restClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    throw new NotFoundException(String.format("Not found albums with id %d", id));
                })
                .body(AlbumExternalModel.class);

        return Optional.ofNullable(albumExternalModel)
                    .map(AlbumExternalModel::toDomain)
                    .map(album -> {
                        album.setPhotos(findPhotosByAlbumId(id));
                        return album;
                    });
    }

    private List<Photo> findPhotosByAlbumId(Long albumId) {

        String uri = UriComponentsBuilder
                .fromHttpUrl(url)
                .path(photoPath)
                .queryParam("albumId", albumId)
                .toUriString();

        PhotoExternalModel[] photoExternalModels = restClient.get().uri(uri)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {})
                .body(PhotoExternalModel[].class);

        if (photoExternalModels != null) {

            return Arrays.stream(photoExternalModels)
                    .map(PhotoExternalModel::toDomain)
                    .toList();

        } else {
            return Collections.emptyList();
        }
    }
}
