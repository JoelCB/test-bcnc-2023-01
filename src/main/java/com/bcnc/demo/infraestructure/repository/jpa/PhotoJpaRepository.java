package com.bcnc.demo.infraestructure.repository.jpa;

import com.bcnc.demo.infraestructure.repository.entity.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoJpaRepository extends JpaRepository<PhotoEntity, Long> {

    List<PhotoEntity> findAllByAlbumId(Long albumId);

}
