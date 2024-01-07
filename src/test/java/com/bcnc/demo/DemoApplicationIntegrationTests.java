package com.bcnc.demo;

import com.bcnc.demo.application.rest.response.GetAlbumResponse;
import com.bcnc.demo.infraestructure.api.model.AlbumExternalModel;
import com.bcnc.demo.infraestructure.api.model.PhotoExternalModel;
import com.bcnc.demo.infraestructure.repository.entity.AlbumEntity;
import com.bcnc.demo.infraestructure.repository.entity.PhotoEntity;
import com.bcnc.demo.infraestructure.repository.jpa.AlbumJpaRepository;
import com.bcnc.demo.infraestructure.repository.jpa.PhotoJpaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class DemoApplicationIntegrationTests {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static MockWebServer mockExternalService;

	@Autowired
	private MockMvc mvc;

	@Autowired
	private AlbumJpaRepository albumJpaRepository;

	@Autowired
	private PhotoJpaRepository photoJpaRepository;

	@BeforeAll
	static void setUp() throws IOException {
		mockExternalService = new MockWebServer();
		mockExternalService.start();
	}

	@AfterAll
	static void tearDown() throws IOException {
		mockExternalService.shutdown();
	}

	@BeforeEach
	private void reset() {
		photoJpaRepository.deleteAll();
		albumJpaRepository.deleteAll();
	}

	@DynamicPropertySource
	static void mockPortProperty(DynamicPropertyRegistry registry) {
		String baseUrl = String.format("http://localhost:%s", mockExternalService.getPort());
		registry.add("external.api.url", () -> baseUrl);
	}

	@Test
	void testGetFromExternalApiAndSaveOK() throws Exception {

		// Init test data
		AlbumExternalModel exAlbum = mockExternalAlbum(1L);
		List<PhotoExternalModel> exPhotos = List.of(
				mockExternalPhoto(1L, 1L),
				mockExternalPhoto(2L, 1L)
		);

		// Mock external service calls
		enqueueExternalServiceResponse(exAlbum);
		enqueueExternalServiceResponse(exPhotos);

		// Perform call to the service
		MvcResult result = mvc.perform(post(String.format("/v1/albums/%s/getFromExternalApiAndSave", "1")))
				.andExpect(status().isOk())
				.andReturn();

		GetAlbumResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), GetAlbumResponse.class);

		// Assert response body
		assertEquals(exAlbum.getId(), response.getId());
		assertEquals(exAlbum.getTitle(), response.getTitle());
		assertEquals(exAlbum.getUserId(), response.getUserId());
		assertEquals(exPhotos.size(), response.getPhotos().size());

		// Check external api calls
		RecordedRequest firstRecordedRequest = mockExternalService.takeRequest();
		assertEquals("GET", firstRecordedRequest.getMethod());
		assertEquals("/albums/1", firstRecordedRequest.getPath());

		RecordedRequest secondRecordedRequest = mockExternalService.takeRequest();
		assertEquals("GET", secondRecordedRequest.getMethod());
		assertEquals("/photos?albumId=1", secondRecordedRequest.getPath());

		// Check saved data
		assertTrue(albumJpaRepository.findById(exAlbum.getId()).isPresent());
		assertEquals(photoJpaRepository.findAllByAlbumId(exAlbum.getId()).size(), exPhotos.size());
	}

	@Test
	void getFromExternalApiOk() throws Exception {

		// Init test data
		AlbumExternalModel exAlbum = mockExternalAlbum(1L);
		List<PhotoExternalModel> exPhotos = List.of(
				mockExternalPhoto(1L, 1L),
				mockExternalPhoto(2L, 1L)
		);

		// Mock external service calls
		enqueueExternalServiceResponse(exAlbum);
		enqueueExternalServiceResponse(exPhotos);

		// Perform call to the service
		MvcResult result = mvc.perform(get(String.format("/v1/albums/%s/getFromExternalApi", "1")))
				.andExpect(status().isOk())
				.andReturn();

		GetAlbumResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), GetAlbumResponse.class);

		// Assert response body
		assertEquals(exAlbum.getId(), response.getId());
		assertEquals(exAlbum.getTitle(), response.getTitle());
		assertEquals(exAlbum.getUserId(), response.getUserId());
		assertEquals(exPhotos.size(), response.getPhotos().size());

		// Check external api calls
		RecordedRequest firstRecordedRequest = mockExternalService.takeRequest();
		assertEquals("GET", firstRecordedRequest.getMethod());
		assertEquals("/albums/1", firstRecordedRequest.getPath());

		RecordedRequest secondRecordedRequest = mockExternalService.takeRequest();
		assertEquals("GET", secondRecordedRequest.getMethod());
		assertEquals("/photos?albumId=1", secondRecordedRequest.getPath());

		// Check that there isn't any saved data
		assertTrue(albumJpaRepository.findAll().isEmpty());
		assertTrue(photoJpaRepository.findAll().isEmpty());
	}

	@Test
	void getFromDatabaseOk() throws Exception {

		// Init test data
		AlbumEntity exAlbum = mockAlbumEntity(1L);
		List<PhotoEntity> exPhotos = List.of(
				mockPhotoEntity(1L, exAlbum),
				mockPhotoEntity(2L, exAlbum)
		);

		// Persist test data
		albumJpaRepository.save(exAlbum);
		exPhotos.forEach(exPhoto -> photoJpaRepository.save(exPhoto));

		// Perform call to the service
		MvcResult result = mvc.perform(get(String.format("/v1/albums/%s/getFromDatabase", "1")))
				.andExpect(status().isOk())
				.andReturn();

		GetAlbumResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), GetAlbumResponse.class);

		// Assert response body
		assertEquals(exAlbum.getId(), response.getId());
		assertEquals(exAlbum.getTitle(), response.getTitle());
		assertEquals(exAlbum.getUserId(), response.getUserId());
		assertEquals(exPhotos.size(), response.getPhotos().size());

		// Check that there is not any external api call
		RecordedRequest recordedRequest = mockExternalService.takeRequest(5, TimeUnit.SECONDS);
		assertNull(recordedRequest);
	}

	private AlbumExternalModel mockExternalAlbum(Long albumId) {

		return AlbumExternalModel.builder()
				.id(albumId)
				.title(String.format("Album %d title", albumId))
				.userId(1L)
				.build();
	}

	private PhotoExternalModel mockExternalPhoto(Long photoId, Long albumId) {

		return PhotoExternalModel.builder()
				.id(photoId)
				.albumId(albumId)
				.title(String.format("Photo %d title", photoId))
				.url(String.format("Photo %d url", photoId))
				.thumbnailUrl(String.format("Photo %d thumbnailUrl", photoId))
				.build();
	}

	private AlbumEntity mockAlbumEntity(Long albumId) {

		return AlbumEntity.builder()
				.id(albumId)
				.title(String.format("Album %d title", albumId))
				.userId(1L)
				.build();
	}

	private PhotoEntity mockPhotoEntity(Long photoId, AlbumEntity album) {

		return PhotoEntity.builder()
				.id(photoId)
				.album(album)
				.title(String.format("Photo %d title", photoId))
				.url(String.format("Photo %d url", photoId))
				.thumbnailUrl(String.format("Photo %d thumbnailUrl", photoId))
				.build();
	}

	private <T> void enqueueExternalServiceResponse(T body) throws JsonProcessingException {

		mockExternalService.enqueue(new MockResponse()
				.setBody(objectMapper.writeValueAsString(body))
				.addHeader("Content-Type", "application/json")
		);
	}
}
