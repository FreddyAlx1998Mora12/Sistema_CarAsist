package unl.academic.sistema_carasist.auth.application.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unl.academic.sistema_carasist.auth.application.dtos.LoginRequestDTO;
import unl.academic.sistema_carasist.auth.application.dtos.LoginResponseDTO;
import unl.academic.sistema_carasist.auth.application.ports.in.IAuthenthicationUseCase;
import unl.academic.sistema_carasist.auth.application.ports.in.IRefreshTokenUseCase;
import unl.academic.sistema_carasist.auth.application.ports.in.IValidateTokenUseCase;
import unl.academic.sistema_carasist.auth.domain.Token;
import unl.academic.sistema_carasist.auth.domain.TokenType;
import unl.academic.sistema_carasist.auth.infraestructure.mapper.ITokenMapper;
import unl.academic.sistema_carasist.auth.infraestructure.repository.ITokenRepository;
import unl.academic.sistema_carasist.exceptions.domain.BusinessException;
import unl.academic.sistema_carasist.security.application.ports.IJwtService;
import unl.academic.sistema_carasist.security.config.JwtProperties;
import unl.academic.sistema_carasist.security.domain.SecurityUser;
import unl.academic.sistema_carasist.user.application.ports.out.IUserRepository;
import unl.academic.sistema_carasist.user.domain.User;
import unl.academic.sistema_carasist.user.infraestructure.mapper.IUserMapper;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenthicationServiceImpl implements IAuthenthicationUseCase,
        IRefreshTokenUseCase,
        IValidateTokenUseCase {

    private final AuthenticationManager authenticationManager;
    private final IUserRepository userRepository;
    private final ITokenRepository tokenRepository;
    private final IJwtService jwtService;
    private final JwtProperties jwtProperties;
    private final ITokenMapper tokenMapper;
    private final IUserMapper userMapper;

    @Override
    @Transactional
    public LoginResponseDTO authenticate(LoginRequestDTO request) {
        log.info("Authenticating user: {}", request.username());
        LoginResponseDTO logDTO = null;
        try {
            // Autenticar con Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );

            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

            // Revocar tokens anteriores del tipo ACCESS
            tokenRepository.revokeAllUserTokensByType(
                    securityUser.getUser().getUserId(),
                    TokenType.ACCESS_TOKEN
            );

            // Generar nuevos tokens
            String accessToken = jwtService.generateAccessToken(securityUser);
            String refreshToken = jwtService.generateRefreshToken(securityUser);

            // Guardar tokens en la base de datos
            saveToken(accessToken, TokenType.ACCESS_TOKEN, securityUser.getUser().getUserId(),
                    jwtProperties.getExpiration());
            saveToken(refreshToken, TokenType.REFRESH_TOKEN, securityUser.getUser().getUserId(),
                    jwtProperties.getRefreshExpiration());

            log.info("User authenticated successfully: {}", request.username());

            logDTO = new LoginResponseDTO(accessToken
                    , "Bearer"
                    , securityUser.getUser().getUserId()
                    , jwtProperties.getExpiration() / 1000
                    , refreshToken, securityUser.getUsername(), LocalDateTime.now());
            /*AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtProperties.getExpiration() / 1000) // Segundos
                    .userId(securityUser.getUserId())
                    .username(securityUser.getUsername())
                    .issuedAt(LocalDateTime.now())
                    .build();*/

        } catch (BadCredentialsException e) {
            log.error("Authentication failed for user: {}", request.username());
            throw new BusinessException("Invalid username or password");
        }

        return logDTO;
    }

    @Override
    @Transactional
    public void logout(String token) {
        log.info("Logging out user");

        Token tokenOpt = tokenRepository.findByToken(token).map(tokenMapper::toToken).orElse(null);
        if (tokenOpt!= null) {
            //Token tokenEntity = (Token) tokenOpt.get();
            tokenOpt.setRevoked(true);
            tokenRepository.save(tokenMapper.toTokenEntity(tokenOpt));
            log.info("Token revoked successfully");
        }
    }

    @Override
    public LoginResponseDTO refreshToken(String refreshToken) {
        LoginResponseDTO log_responseDTO = null;
        log.info("Refreshing access token");

        // Validar refresh token
        Token tokenOpt = tokenRepository.findValidToken(refreshToken, LocalDateTime.now())
                .map(tokenMapper::toToken).orElse(null);
        if (tokenOpt == null) {
            throw new BusinessException("Invalid or expired refresh token");
        }

        // Extraer username y cargar usuario
        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findActiveUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        SecurityUser securityUser = new SecurityUser(user);

        // Validar token con el usuario
        if (!jwtService.isTokenValid(refreshToken, securityUser)) {
            throw new BusinessException("Invalid refresh token");
        }

        // Revocar tokens ACCESS anteriores
        tokenRepository.revokeAllUserTokensByType(
                user.getUserId(),
                TokenType.ACCESS_TOKEN
        );

        // Generar nuevo access token
        String newAccessToken = jwtService.generateAccessToken(securityUser);

        // Guardar nuevo access token
        saveToken(newAccessToken, TokenType.ACCESS_TOKEN, user.getUserId(),
                jwtProperties.getExpiration());

        log.info("Access token refreshed successfully for user: {}", username);

        log_responseDTO = new LoginResponseDTO(
                newAccessToken
                ,"Bearer"
                ,user.getUserId()
                ,jwtProperties.getExpiration() / 1000
                ,refreshToken
                ,username
                ,LocalDateTime.now()
        );
        return log_responseDTO;
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Token tokenOpt = tokenRepository.findValidToken(token,LocalDateTime.now())
                    .map(tokenMapper::toToken).orElse(null);
            if (tokenOpt == null) {
                return false;
            }

            //Token tokenEntity = tokenOpt.get();
            String username = jwtService.extractUsername(token);

            User user = userRepository.findActiveUserByUsername(username)
                    .orElse(null);

            if (user == null) {
                return false;
            }

            SecurityUser securityUser = new SecurityUser(user);
            return jwtService.isTokenValid(token, securityUser) && tokenOpt.isValid();

        } catch (Exception e) {
            log.error("Error validating token: {}", e.getMessage());
            return false;
        }
        //return false;
    }

    @Override
    @Transactional
    public void revokeToken(String token) {
        Token tokenOpt = tokenRepository.findByToken(token).map(tokenMapper::toToken).orElse(null);
        if (tokenOpt != null) {
            //Token tokenEntity = tokenOpt.get();
            tokenOpt.setRevoked(true);
            tokenRepository.save(tokenMapper.toTokenEntity(tokenOpt));
        }
    }

    private void saveToken(String tokenValue, TokenType tokenType,
                           Integer userId, Long expirationMs) {
        Token token = Token.builder()
                .token(tokenValue)
                .tokenType(tokenType)
                .idUsuario(userId)
                .revoked(false)
                .expired(false)
                .expiresAt(LocalDateTime.now().plusNanos(expirationMs * 1_000_000))
                .build();

        tokenRepository.save(tokenMapper.toTokenEntity(token));
    }
}
