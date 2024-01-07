package com.bcnc.demo.domain.service;

import com.bcnc.demo.domain.Album;
import com.bcnc.demo.domain.apiclient.AlbumApiClient;
import com.bcnc.demo.domain.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumApiClient albumApiClient;

    @Override
    public Optional<Album> getFromExternalApiAndSave(Long albumId) {

        return albumApiClient.findById(albumId)
                .map(albumRepository::create)
                .flatMap(albumRepository::findById);
    }

    @Override
    public Optional<Album> getFromExternalApi(Long albumId) {

        return albumApiClient.findById(albumId);
    }

    @Override
    public Optional<Album> getFromDatabase(Long albumId) {

        return albumRepository.findById(albumId);
    }
}
