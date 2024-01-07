package com.bcnc.demo.application.rest;

import com.bcnc.demo.application.rest.response.GetAlbumResponse;
import com.bcnc.demo.domain.exception.NotFoundException;
import com.bcnc.demo.domain.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/albums")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlbumRESTController {

    public final AlbumService albumService;

    @PostMapping("/{id}/getFromExternalApiAndSave")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GetAlbumResponse> getFromExternalApiAndSave(@PathVariable Long id) {

        return albumService.getFromExternalApiAndSave(id)
                .map(GetAlbumResponse::fromDomain)
                .map(album -> ResponseEntity.ok().body(album))
                .orElseThrow(NotFoundException::new);
    }

    @GetMapping("/{id}/getFromExternalApi")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetAlbumResponse> getFromExternalApi(@PathVariable Long id) {

        return albumService.getFromExternalApi(id)
                .map(GetAlbumResponse::fromDomain)
                .map(album -> ResponseEntity.ok().body(album))
                .orElseThrow(NotFoundException::new);
    }

    @GetMapping("/{id}/getFromDatabase")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetAlbumResponse> getFromDatabase(@PathVariable Long id) {

        return albumService.getFromDatabase(id)
                .map(GetAlbumResponse::fromDomain)
                .map(album -> ResponseEntity.ok().body(album))
                .orElseThrow(NotFoundException::new);
    }
}
