package com.bcnc.demo.domain.service;

import com.bcnc.demo.domain.Album;
import com.bcnc.demo.domain.Photo;
import com.bcnc.demo.domain.apiclient.AlbumApiClient;
import com.bcnc.demo.domain.repository.AlbumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class AlbumServiceUnitTests {

    @InjectMocks
    private AlbumServiceImpl albumService;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private AlbumApiClient albumApiClient;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFromExternalApiAndSaveOk() {

        Long albumId = 1L;
        Album album = mockAlbum(albumId);

        when(albumApiClient.findById(albumId)).thenReturn(Optional.of(album));
        when(albumRepository.create(album)).thenReturn(albumId);
        when(albumRepository.findById(albumId)).thenReturn(Optional.of(album));

        Optional<Album> result = albumService.getFromExternalApiAndSave(albumId);

        assertTrue(result.isPresent());
        assertEquals(album, result.get());
    }

    @Test
    void getFromExternalApiAndSaveNotFound() {

        Long albumId = 1L;

        when(albumApiClient.findById(albumId)).thenReturn(Optional.empty());

        Optional<Album> result = albumService.getFromExternalApiAndSave(albumId);

        assertTrue(result.isEmpty());
    }

    @Test
    void getFromExternalApiOk() {

        Long albumId = 1L;
        Album album = mockAlbum(albumId);

        when(albumApiClient.findById(albumId)).thenReturn(Optional.of(album));

        Optional<Album> result = albumService.getFromExternalApi(albumId);

        assertTrue(result.isPresent());
        assertEquals(album, result.get());
    }

    @Test
    void getFromExternalApiNotFound() {

        Long albumId = 1L;

        when(albumApiClient.findById(albumId)).thenReturn(Optional.empty());

        Optional<Album> result = albumService.getFromExternalApi(albumId);

        assertTrue(result.isEmpty());
    }

    @Test
    void getFromDatabaseOk() {

        Long albumId = 1L;
        Album album = mockAlbum(albumId);

        when(albumRepository.findById(albumId)).thenReturn(Optional.of(album));

        Optional<Album> result = albumService.getFromDatabase(albumId);

        assertTrue(result.isPresent());
        assertEquals(album, result.get());
    }

    @Test
    void getFromDatabaseNotFound() {

        Long albumId = 1L;

        when(albumRepository.findById(albumId)).thenReturn(Optional.empty());

        Optional<Album> result = albumService.getFromDatabase(albumId);

        assertTrue(result.isEmpty());
    }

    private Album mockAlbum(Long albumId) {

        Photo photo = Photo.builder()
                .id(1L)
                .albumId(1L)
                .title("Photo 1 title")
                .url("Photo 1 url")
                .thumbnailUrl("Photo 1 thumbnailUrl")
                .build();

        return Album.builder()
                .id(albumId)
                .title("Album title")
                .userId(1L)
                .photos(List.of(photo))
                .build();
    }
}
