package Trendithon.SpinOff.domain.heart.controller;

import Trendithon.SpinOff.domain.heart.dto.HeartJobPostingDto;
import Trendithon.SpinOff.domain.heart.dto.HeartProjectDto;
import Trendithon.SpinOff.domain.heart.entity.HeartJobPosting;
import Trendithon.SpinOff.domain.heart.service.HeartService;
import Trendithon.SpinOff.global.entity.ResponseResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    @PutMapping("/board/like/{boardId}")
    public ResponseEntity<ResponseResult<?>> insert(@RequestBody @Valid HeartProjectDto heartRequestDTO,
                                                    @PathVariable Long boardId) {
        try {
            heartService.insert(heartRequestDTO, boardId);
            return ResponseEntity.ok(ResponseResult.success("좋아요 추가 성공", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseResult.failure("이미 좋아요 누름"));
        }
    }

    @DeleteMapping("/board/unlike/{boardId}")
    public ResponseEntity<ResponseResult<?>> delete(@RequestBody @Valid HeartProjectDto heartProjectDto,
                                                    @PathVariable Long boardId) {
        try {
            heartService.delete(heartProjectDto, boardId);
            return ResponseEntity.ok(ResponseResult.success("좋아요 삭제 성공", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseResult.failure("좋아요 안 눌렀넹?"));
        }
    }

    @PutMapping("/jobPosting/like/{jobPostingId}")
    public ResponseEntity<ResponseResult<?>> insert(@RequestBody @Valid HeartJobPostingDto heartJobPostingDto,
                                                    @PathVariable Long jobPostingId) {
        try {
            heartService.insert(heartJobPostingDto.memberId(), jobPostingId);
            return ResponseEntity.ok(ResponseResult.success("좋아요 추가 성공", null));
        } catch (Exception e) {
            log.info("error message = {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseResult.failure("이미 좋아요를 눌렀습니다."));
        }
    }

    @DeleteMapping("/jobPosting/unlike/{jobPostingId}")
    public ResponseEntity<ResponseResult<?>> delete(@RequestBody @Valid HeartJobPostingDto heartJobPostingDto,
                                                    @PathVariable Long jobPostingId) {
        try {
            heartService.delete(heartJobPostingDto.memberId(), jobPostingId);
            return ResponseEntity.ok(ResponseResult.success("좋아요 삭제 성공", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseResult.failure("해당 공고에 좋아요를 누르지 않았습니다."));
        }
    }
}