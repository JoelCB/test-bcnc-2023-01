package com.bcnc.demo.domain.repository;

import com.bcnc.demo.domain.Album;

import java.util.Optional;

public interface AlbumRepository {

    Optional<Album> findById(Long id);

    Long create(Album album);

}
