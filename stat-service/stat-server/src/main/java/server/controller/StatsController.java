package server.controller;

import dto.HitDto;
import dto.StatsDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.service.HitServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private final Logger log = LoggerFactory.getLogger(StatsController.class);
    private final HitServiceImpl hitService;

    @PostMapping("/hit")
    public HitDto createHit(@RequestBody HitDto hitDto) {
        return hitService.createHit(hitDto);
    }

    @GetMapping("/stats")
    public List<StatsDto> getStats(@RequestParam String start,
                                   @RequestParam String end,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(required = false) Boolean unique) {
        return hitService.getStats(start, end, uris, unique);
    }
}
