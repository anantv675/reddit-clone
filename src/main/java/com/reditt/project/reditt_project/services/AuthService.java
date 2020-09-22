package com.reditt.project.reditt_project.services;

import com.reditt.project.reditt_project.Repositories.UserRepository;
import com.reditt.project.reditt_project.Repositories.VerificationTokenRepository;
import com.reditt.project.reditt_project.Security.JwtProvider;
import com.reditt.project.reditt_project.dto.AuthenticationResponse;
import com.reditt.project.reditt_project.dto.LoginRequest;
import com.reditt.project.reditt_project.dto.RegisterRequest;
import com.reditt.project.reditt_project.entities.NotificationEmail;
import com.reditt.project.reditt_project.entities.User;
import com.reditt.project.reditt_project.entities.UserPrincipal;
import com.reditt.project.reditt_project.entities.VerificationToken;
import com.reditt.project.reditt_project.exceptions.SpringRedittException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthenticatedReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor // used for dependency injection
public class AuthService {

    private final PasswordEncoder passwordEncoder; // as field injection is not recommended, @Autowired annotation will not be used
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setUserName(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setCreatedDate(Instant.now());
        user.setEnabled(false); // this will be set to true once email verification takes place

        userRepository.save(user);

        // when the user creates an account, a token corresponding to that user is generated
        String token = generateVerificationToken(user);

        // then, a mail is sent using the MailService class to that user's mail.
        // the token generated is looked up in the db when the user clicks ["http://localhost:8080/api/auth/accountVerification/" + token]
        // and the user corresponding to that token is enabled
        mailService.sendMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(), "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(User user) {

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyToken(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(()->new SpringRedittException("Token not found"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String userName = verificationToken.getUser().getUserName();
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new SpringRedittException("Users not found with name : " + userName));
        user.setEnabled(true);
        userRepository.save(user);
    }

    //this method will create a UserNamePassword authentication token and return a jwt to the user after authentication.
    //authenticationManager will authenticate the user using the UserDetailsService class( using loadByUserName method) , which returns a UserDetails object to
    //the authenticationManager class.
    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate); // this token will be sent when the user logs in
        return new AuthenticationResponse(token,loginRequest.getUsername()); // the token will be returned to the user
    }


    @Transactional(readOnly = true)
    public User getCurrentUser() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUserName(userPrincipal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + userPrincipal.getUsername()));
    }
}
