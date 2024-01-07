package com.bcnc.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Photo {

    private Long id;
    private Long albumId;
    private String title;
    private String url;
    private String thumbnailUrl;

}
