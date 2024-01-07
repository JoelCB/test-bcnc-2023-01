package com.bcnc.demo.infraestructure.repository.jpa;

import com.bcnc.demo.infraestructure.repository.entity.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumJpaRepository extends JpaRepository<AlbumEntity, Long> {

}
