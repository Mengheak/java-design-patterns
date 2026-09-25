package com.pisethjava.builder;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DemoController {

    private final WebClient roomWebClient;

    @GetMapping("/demo/room")
    public RoomResponse room() {
        return RoomResponse.builder()
                .roomCode("DLX001")
                .roomName("Deluxe Room")
                .roomType("DELUXE")
                .price(BigDecimal.valueOf(80))
                .currency("USD")
                .wifi(true)
                .balcony(true)
                .build();
    }

    @GetMapping("/demo/url")
    public Map<String, String> url(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        String url = UriComponentsBuilder
                .fromPath("/api/v1/rooms")
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("roomType", "DELUXE")
                .build()
                .toUriString();

        return Map.of(
                "message", "This URL was built by UriComponentsBuilder",
                "url", url,
                "webClient", roomWebClient.toString()
        );
    }
}
