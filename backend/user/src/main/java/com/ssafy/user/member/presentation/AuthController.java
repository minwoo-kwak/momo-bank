package com.ssafy.user.member.presentation;


import com.ssafy.user.common.CommonResponse;
import com.ssafy.user.member.application.AuthService;
import com.ssafy.user.member.dto.request.LoginRequest;
import com.ssafy.user.member.dto.response.LoginResponse;
import com.ssafy.user.member.dto.response.MypageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "인증 api")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @SecurityRequirement(name = "refreshToken")
    @Operation(summary = "어세스 토큰 재발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재발급 성공"),
            @ApiResponse(responseCode = "401", description = "만료된 리프레쉬 토큰입니다. <br> 유효하지 않은 리프레쉬 토큰입니다. ")
    })
    @PostMapping("/regenerate")
    public ResponseEntity regenerateToken(@RequestHeader("refreshToken") String refreshToken) throws Exception {
// 로직 구현

        Map<String, String> tokens = authService.regenerateToken(refreshToken);

        return CommonResponse.toResponseEntity(HttpStatus.OK, "토큰 재발급 완료", null, tokens);
    }



    @Operation(summary = "로그인", description = "로그인 성공 시, 헤더에 어세스 토큰과 리프레쉬 토큰을 포함시켜 응답한다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class))}),
            @ApiResponse(responseCode = "400", description = "제공된 정보와 일치하는 회원 정보 없음")
    })
    @PostMapping("/login")
    public ResponseEntity login (@Valid @RequestBody LoginRequest request) throws Exception {

        Map<String, Object> data = authService.login(request);



        return CommonResponse.toResponseEntity(HttpStatus.OK, "로그인 성공.", (LoginResponse)data.get("member"), (Map<String, String>)data.get("jwtTokens"));
    }





}
