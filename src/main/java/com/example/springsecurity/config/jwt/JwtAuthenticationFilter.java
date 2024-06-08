package com.example.springsecurity.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springsecurity.dto.LoginRequestDto;
import com.example.springsecurity.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.List;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    private final JwtProperties jwtProperties;

    private final TokenService tokenService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProperties jwtProperties, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtProperties = jwtProperties;
        this.tokenService = tokenService;
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response)
            throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : 진입");

        ObjectMapper mapper = new ObjectMapper();

        LoginRequestDto loginRequestDto = null;
        try {
            loginRequestDto = mapper.readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("JwtAuthenticationFilter : "+loginRequestDto);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        System.out.println("JwtAuthenticationFilter : 토큰생성완료");
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        System.out.printf("Authentication : %s%n", userDetails.getUsername());
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                  Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        String rolesString = String.join(",", roles);

        String access = tokenService.accessToken(userDetails.getUsername(), rolesString, jwtProperties.getSecret());
        System.out.println("필터 email: "+userDetails.getUsername());
        String refresh = tokenService.refreshToken(userDetails.getUsername(), rolesString, jwtProperties.getSecret());

        response.addHeader(jwtProperties.getHEADER_STRING(), jwtProperties.getTOKEN_PREFIX()+access);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("{\"token\": \"" + jwtProperties.getTOKEN_PREFIX() + access + "\"}");
    }

    //로그인 실패
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Authentication failed: " + failed.getMessage());
        System.out.println("Authentication failed: " + failed.getMessage());
    }

}
