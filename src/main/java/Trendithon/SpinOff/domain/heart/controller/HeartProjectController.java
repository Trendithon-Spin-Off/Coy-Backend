package Trendithon.SpinOff.domain.heart.controller;

import Trendithon.SpinOff.domain.board.entity.Board;
import Trendithon.SpinOff.domain.board.dto.BoardResponseDto;
import Trendithon.SpinOff.domain.heart.entity.HeartProject;
import Trendithon.SpinOff.domain.heart.dto.HeartProjectDto;
import Trendithon.SpinOff.domain.heart.repository.HeartProjectRepository;
import Trendithon.SpinOff.domain.member.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class HeartProjectController {

    private final HeartProjectRepository heartProjectRepository;

    @Autowired
    public HeartProjectController(HeartProjectRepository heartProjectRepository) {
        this.heartProjectRepository = heartProjectRepository;
    }

    @GetMapping("/likedlist/{memberName}")
    public ResponseEntity<BoardResponseDto> showLikedList(@PathVariable String memberName) {
        List<HeartProject> heartProjects = heartProjectRepository.findByMemberName(memberName);
        if (!heartProjects.isEmpty()) {
            HeartProject heartProject = heartProjects.get(0); // 첫 번째 항목을 가져옴
            Board board = heartProject.getBoard();
            BoardResponseDto responseDto = new BoardResponseDto();
            responseDto.setBno(board.getBno());
            responseDto.setProjectName(board.getProjectName());
            responseDto.setDescription(board.getProjectDescription());
            responseDto.setMainFeature(board.getProjectFeatures());
            responseDto.setImageUrl(board.getProjectImage());
            responseDto.setGithubUrl(board.getGithub());
            responseDto.setSelectedPart(Collections.singletonList(board.getSelectedParts()));
            responseDto.setProjectMembers(Collections.singletonList(board.getProjectMembers()));
            responseDto.setCategory(board.getCategory());
            responseDto.setBoardLike(board.getBoardLike());
            responseDto.setWriter(board.getWriter());
            // 나머지 필드들도 위와 같이 설정해주어야 합니다.

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
