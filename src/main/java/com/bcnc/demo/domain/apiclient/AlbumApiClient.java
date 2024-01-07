package com.bcnc.demo.domain.apiclient;

import com.bcnc.demo.domain.Album;

import java.util.Optional;

public interface AlbumApiClient {

    Optional<Album> findById(Long id);

}
