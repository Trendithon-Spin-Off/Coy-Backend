package Trendithon.SpinOff.domain.heart.service;

import Trendithon.SpinOff.domain.heart.dto.HeartJobPostingDto;
import Trendithon.SpinOff.domain.heart.dto.HeartProjectDto;
import Trendithon.SpinOff.domain.board.entity.Board;
import Trendithon.SpinOff.domain.heart.entity.HeartJobPosting;
import Trendithon.SpinOff.domain.heart.entity.HeartProject;
import Trendithon.SpinOff.domain.board.repository.BoardRepository;
import Trendithon.SpinOff.domain.heart.repository.HeartJobPostingRepository;
import Trendithon.SpinOff.domain.heart.repository.HeartProjectRepository;
import Trendithon.SpinOff.domain.heart.valid.exception.HeartAlreadyException;
import Trendithon.SpinOff.domain.jobposting.entity.JobPosting;
import Trendithon.SpinOff.domain.jobposting.repository.JobPostingJpaRepository;
import Trendithon.SpinOff.domain.member.entity.Member;
import Trendithon.SpinOff.domain.member.repository.MemberJpaRepository;

import Trendithon.SpinOff.domain.profile.valid.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final MemberJpaRepository memberRepository;
    private final HeartProjectRepository heartProjectRepository;
    private final BoardRepository boardRepository;
    private final HeartJobPostingRepository heartJobPostingRepository;
    private final JobPostingJpaRepository jobPostingJpaRepository;

    @Transactional
    public void insert(HeartProjectDto heartRequestDTO, Long boardId) throws Exception {
        Member member = memberRepository.findById(heartRequestDTO.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(
                        "Could not found member id : " + heartRequestDTO.getMemberId()));

        Board board = boardRepository.findById(heartRequestDTO.getBoardId())
                .orElseThrow(() -> new MemberNotFoundException(
                        "Could not found board id : " + heartRequestDTO.getBoardId()));
        // 이미 좋아요 되어 있음
        if (heartProjectRepository.findByMemberAndBoard(member, board).isPresent()) {
            //에러
            throw new HeartAlreadyException("이미 좋아요를 눌렀습니다.");
        }

        HeartProject heart = HeartProject.builder()
                .board(board)
                .member(member)
                .build();

        heartProjectRepository.save(heart);

        // 좋아요 누를 시 게시물의 좋아요 증가
        board.setBoard_like(board.getBoard_like() + 1);
        boardRepository.save(board);
    }

    @Transactional
    public void delete(HeartProjectDto heartRequestDTO, Long boardId) {

        Member member = memberRepository.findById(heartRequestDTO.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(
                        "Could not found member id : " + heartRequestDTO.getMemberId()));

        Board board = boardRepository.findById(heartRequestDTO.getBoardId())
                .orElseThrow(() -> new MemberNotFoundException(
                        "Could not found board id : " + heartRequestDTO.getBoardId()));

        HeartProject heart = heartProjectRepository.findByMemberAndBoard(member, board)
                .orElseThrow(() -> new MemberNotFoundException("Could not found heart id"));

        heartProjectRepository.delete(heart);

        board.setBoard_like(board.getBoard_like() - 1);
        boardRepository.save(board);

    }


    public Member getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // 인증안되었을 때
        }
        return (Member) authentication.getPrincipal();
    }

    @Transactional
    public void insert(Long memberId, Long jobPostingId) throws Exception {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(
                        "Could not found member id : " + memberId));

        JobPosting jobPosting = jobPostingJpaRepository.findById(jobPostingId)
                .orElseThrow(() -> new MemberNotFoundException(
                        "Could not found board id : " + jobPostingId));
        // 이미 좋아요 되어 있음
        if (heartJobPostingRepository.findByMemberAndJobPosting(member, jobPosting).isPresent()) {
            //에러
            throw new HeartAlreadyException("이미 좋아요를 눌렀습니다.");
        }

        HeartJobPosting heart = HeartJobPosting.builder()
                .jobPosting(jobPosting)
                .member(member)
                .build();

        heartJobPostingRepository.save(heart);

        // 좋아요 누를 시 게시물의 좋아요 증가
        jobPosting.increaseLikeCount();
        jobPostingJpaRepository.save(jobPosting);
    }

    @Transactional
    public void delete(Long memberId, Long jobPostingId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(
                        "Could not found member id : " + memberId));

        JobPosting jobPosting = jobPostingJpaRepository.findById(jobPostingId)
                .orElseThrow(() -> new MemberNotFoundException(
                        "Could not found board id : " + jobPostingId));

        HeartJobPosting heart = heartJobPostingRepository.findByMemberAndJobPosting(member, jobPosting)
                .orElseThrow(() -> new MemberNotFoundException("Could not found heart id"));

        heartJobPostingRepository.delete(heart);

        jobPosting.decreaseLikeCount();
        jobPostingJpaRepository.save(jobPosting);
    }
}
