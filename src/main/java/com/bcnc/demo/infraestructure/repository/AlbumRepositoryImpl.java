package com.bcnc.demo.infraestructure.repository;

import com.bcnc.demo.domain.Album;
import com.bcnc.demo.domain.repository.AlbumRepository;
import com.bcnc.demo.infraestructure.repository.entity.AlbumEntity;
import com.bcnc.demo.infraestructure.repository.entity.PhotoEntity;
import com.bcnc.demo.infraestructure.repository.jpa.AlbumJpaRepository;
import com.bcnc.demo.infraestructure.repository.jpa.PhotoJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlbumRepositoryImpl implements AlbumRepository {

    private final AlbumJpaRepository albumJpaRepository;
    private final PhotoJpaRepository photoJpaRepository;

    @Override
    @Transactional
    public Optional<Album> findById(Long id) {

        return albumJpaRepository.findById(id)
               .map(albumEntity -> {
                    albumEntity.setPhotos(photoJpaRepository.findAllByAlbumId(id));
                    return albumEntity;
                })
                .map(AlbumEntity::toDomain);
    }

    @Override
    @Transactional
    public Long create(Album album) {

        AlbumEntity albumEntity = AlbumEntity.fromDomain(album);
        List<PhotoEntity> photoEntityList = albumEntity.getPhotos();

        albumEntity.setPhotos(Collections.emptyList());
        albumJpaRepository.save(albumEntity);

        photoEntityList.forEach(photoEntity -> {
            photoEntity.setAlbum(albumEntity);
            photoJpaRepository.save(photoEntity);
        });

        return album.getId();
    }
}
