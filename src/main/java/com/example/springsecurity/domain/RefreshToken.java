package com.example.springsecurity.domain;

import com.example.springsecurity.dto.RefreshTokenRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RefreshToken extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, name = "refresh_token")
    private String refreshToken;

    public RefreshToken(User user, String refreshToken) {
        this.user = user;
        this.refreshToken = refreshToken;
    }

    @Transactional
    public void updateRefreshToken(RefreshTokenRequestDto refreshTokenRequestDto){
        this.user = refreshTokenRequestDto.getUser();
        this.refreshToken = refreshTokenRequestDto.getRefresh();
    }
}
