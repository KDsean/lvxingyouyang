package com.lvxingyouyang.controller;

import com.lvxingyouyang.dto.ApiResponse;
import com.lvxingyouyang.service.IGuideService;
import com.lvxingyouyang.entity.Guide;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guides")
@RequiredArgsConstructor
public class GuideController {
    private final IGuideService guideService;

    @GetMapping
    public ApiResponse<?> getGuides(
            @RequestParam(required = false) String destination,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ApiResponse.success(guideService.getGuides(destination, page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getGuideDetail(@PathVariable Long id) {
        return ApiResponse.success(guideService.getGuideDetail(id));
    }

    @PostMapping
    public ApiResponse<?> publishGuide(@RequestBody Guide guide) {
        return ApiResponse.success(guideService.publishGuide(guide));
    }

    @PostMapping("/{id}/like")
    public ApiResponse<?> likeGuide(@PathVariable Long id) {
        guideService.likeGuide(id);
        return ApiResponse.success("点赞成功");
    }

    @GetMapping("/popular")
    public ApiResponse<?> getPopularGuides(@RequestParam(defaultValue = "10") Integer limit) {
        return ApiResponse.success(guideService.getPopularGuides(limit));
    }

    @GetMapping("/search")
    public ApiResponse<?> searchGuides(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "") String destination,
            @RequestParam(required = false, defaultValue = "") String tag,
            @RequestParam(required = false, defaultValue = "latest") String sortBy,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "12") Integer pageSize) {
        return ApiResponse.success(guideService.searchGuides(keyword, destination, tag, sortBy, page, pageSize));
    }
}
