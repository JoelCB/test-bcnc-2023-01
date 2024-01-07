package com.bcnc.demo.domain.service;

import com.bcnc.demo.domain.Album;

import java.util.Optional;

public interface AlbumService {

    Optional<Album> getFromExternalApiAndSave(Long albumId);

    Optional<Album> getFromExternalApi(Long albumId);

    Optional<Album> getFromDatabase(Long albumId);
}
