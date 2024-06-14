package com.ninjaone.dundie_awards.controller;

import com.ninjaone.dundie_awards.AwardsCache;
import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.controller.dto.ActivityDto;
import com.ninjaone.dundie_awards.service.ActivityService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
class ActivityControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AwardsCache awardsCache;

    @Autowired
    private MessageBroker messageBroker;

    @Autowired
    private ActivityService activityService;

    @Test
    void createActivityIntegrationTest() throws ExecutionException, InterruptedException {
        ActivityDto activityDto = new ActivityDto();
        activityDto.setOccuredAt(LocalDateTime.now());
        activityDto.setEvent("{\"employeeId\":" + 1 + "}");

        List<CompletableFuture<Void>> futures = IntStream.range(0, 100000)
                .mapToObj(i -> CompletableFuture.runAsync(() ->
                        webTestClient.post().uri("/activity")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(activityDto)
                                .exchange()
                                .expectStatus().isOk()
                ))
                .toList();

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allFutures.get();
        await().atMost(300, SECONDS).until(
                () -> messageBroker.getMessages().isEmpty() && activityService.getNonProcessedActivities().isEmpty()
        );

        assertThat(awardsCache.getTotalAwards()).isEqualTo(100000);
    }
}
