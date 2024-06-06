package com.example.springsecurity.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springsecurity.config.auth.PrincipalDetails;
import com.example.springsecurity.domain.User;
import com.example.springsecurity.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Objects;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtProperties jwtProperties, UserRepository userRepository) {
        super(authenticationManager);
        this.jwtProperties = jwtProperties;

        this.userRepository = userRepository;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(jwtProperties.getHEADER_STRING());
        if (header == null || !header.startsWith(jwtProperties.getTOKEN_PREFIX())) {
            chain.doFilter(request, response);
            return;
        }
        System.out.println("header : " + header);
        String token = request.getHeader(jwtProperties.getHEADER_STRING())
                .replace(jwtProperties.getTOKEN_PREFIX(), "");

        String username = JWT.require(Algorithm.HMAC512(jwtProperties.getSecret())).build().verify(token)
                .getClaim("email").asString();
        String role = JWT.require(Algorithm.HMAC512(jwtProperties.getSecret())).build().verify(token)
                .getClaim("role").asString();
        System.out.println("role: "+ role);
        if (username != null) {
            User user = userRepository.findByEmail(username);
            System.out.println("체크체크: " + user.getRole());
            if (user != null && Objects.equals(role, user.getRole())) {
                System.out.println("role 동일");

                // 인증은 토큰 검증시 끝. 인증을 하기 위해서가 아닌 스프링 시큐리티가 수행해주는 권한 처리를 위해
                // 아래와 같이 토큰을 만들어서 Authentication 객체를 강제로 만들고 그걸 세션에 저장!
                PrincipalDetails principalDetails = new PrincipalDetails(user);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        principalDetails.getUsername(),
                        principalDetails.getPassword(),
                        principalDetails.getAuthorities());
                System.out.println("인증확인: " + authentication.getAuthorities());

                // 강제로 시큐리티의 세션에 접근하여 값 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new UsernameNotFoundException("role is null");
            }
        }

        chain.doFilter(request, response);

    }
}
