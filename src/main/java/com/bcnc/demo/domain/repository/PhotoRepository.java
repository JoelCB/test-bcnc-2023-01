package com.bcnc.demo.domain.repository;

import com.bcnc.demo.domain.Album;

import java.util.List;

public interface PhotoRepository {

    List<Album> findByAlbumId(Long albumId);

    Long create(Album album);

}
