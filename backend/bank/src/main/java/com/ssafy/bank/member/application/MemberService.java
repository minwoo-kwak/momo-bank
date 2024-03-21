package com.ssafy.bank.member.application;

import com.ssafy.bank.common.ErrorCode;
import com.ssafy.bank.common.exception.CustomException;
import com.ssafy.bank.member.domain.Member;
import com.ssafy.bank.member.domain.repository.MemberRepository;
import com.ssafy.bank.member.domain.repository.MemberRepositoryCustom;
import com.ssafy.bank.member.dto.request.JoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepositoryCustom memberRepositoryCustom;
    private final MemberRepository memberRepository;

    public void join(JoinRequest request) throws Exception {


        // 이미 가입된 아이디면 예외처리
        if (memberRepositoryCustom.findMemberToCheckDtoById(request.getId()) != null)
            throw new CustomException(ErrorCode.ALREADY_JOINED_ID);

        // 멤버 엔티티 생성 및 저장
        Member member = Member.builder()
                .id(request.getId())
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .birthDate(toLocalDateTime(request.getBirthdate()))
                .password(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()))
//                .password(request.getPassword())
                .build();


        memberRepository.save(member);
    }


    //     String을 LocalDateTime으로 변환
    private LocalDateTime toLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date + " 00:00:00", formatter);
    }
}