package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.User;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.response.UserTokenResponseDto;
import com.whatachad.app.repository.UserRepository;
import com.whatachad.app.security.AuthConstant;
import com.whatachad.app.type.UserMetaType;
import com.whatachad.app.type.UserRoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final static Set<UserRoleType> USER_ROLE_FILTER_SET = new HashSet<>(Arrays.asList(
            UserRoleType.ROLE_CUSTOMER));

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User getUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "User"));
        return user;
    }

    @Transactional
    public List<User> getUserList() {
        List<User> userList = userRepository.findAll();
        return userList;
    }

    public User signIn(UserLoginRequestDto loginDTO) {
        User user = getUser(loginDTO.getId());
        if (!user.isValid()) {
            log.error("인증되지 않은 사용자 입니다. - {}", user.getId());
            throw new CommonException(BError.NOT_SUPPORT, user.getId());
        }
        // 패스워드 검증
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new CommonException(BError.NOT_MATCH, "Password");
        }
        return user;
    }

    public User signUp(User input) {
        try {
            return createUser(input);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.debug(e.getMessage(), e);
            throw new CommonException(BError.FAIL_REASON, "Sign Up", e.getMessage());
        }
    }

    @Transactional
    public User createUser(User input) {
        try {
            userRepository.findById(input.getId()).ifPresent(user -> {
                throw new CommonException(BError.EXIST, "ID");
            });
            return userRepository.save(setUser(input));
        } catch (Exception e) {
            log.error(e.getMessage());
            log.debug(e.getMessage(), e);
            throw new CommonException(BError.FAIL_REASON, "User Create", e.getMessage());
        }
    }

    @Transactional
    public User updateUser(User input) throws CommonException {
        userRepository.findById(input.getId())
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "User"));
        return userRepository.save(setUser(input));
    }

    @Transactional
    public void deleteUser(String id) throws CommonException {
        try {
            userRepository.findById(id)
                    .ifPresent(idExist -> userRepository.deleteById(id));
            return;
        } catch (Exception e) {
            log.error(e.getMessage());
            log.debug(e.getMessage(), e);
            throw new CommonException(BError.FAIL, "User Delete");
        }
    }

    @Transactional
    protected User setUser(User input) {

        // Admin User를 제외한 나머지는 Role에 대한 검사를 진행한다.
        if (!input.getId().equals(AuthConstant.ADMIN_USER) &&
                !USER_ROLE_FILTER_SET.containsAll(
                        Arrays.stream(input.getMeta().get(UserMetaType.ROLE).split(","))
                                .map(s -> UserRoleType.valueOf(s))
                                .collect(Collectors.toSet()))) {
            throw new CommonException(BError.NOT_MATCH, "User Role");
        }

        User user = userRepository.findById(input.getId()).orElse(
                User.builder()
                        .id(input.getId()) // 변경하지 않을 사항
                        .name(input.getName()) // 변경하지 않을 사항
                        .meta(new HashMap<>())
                        .build()
        );

        // Modify Only Input Data Exist like Patch Method
        if (Objects.nonNull(input.getPassword())) {
            user.setPassword(input.getPassword());
        }
        // Unique Value
        if (Objects.nonNull(input.getEmail())) {
            if (userRepository.existsByEmail(input.getEmail())) {
                throw new CommonException(BError.EXIST, "Email");
            }
            user.setEmail(input.getEmail());
        }
        if (Objects.nonNull(input.getName())) {
            user.setName(input.getName());
        }
        // Unique Value
        if (Objects.nonNull(input.getPhone())) {
            if (userRepository.existsByPhone(input.getPhone())) {
                throw new CommonException(BError.EXIST, "PhoneNumber");
            }
            user.setPhone(input.getPhone());
        }
        if (Objects.nonNull(input.getMeta())) {
            input.getMeta().entrySet().forEach(entry -> {
                user.getMeta().put(entry.getKey(), entry.getValue());
            });
        }

        return user;
    }

    @Transactional
    public void updateAuthStatus(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new CommonException(BError.NOT_EXIST, "Email");
        });
        user.setValid(true);
        userRepository.save(user);
    }

    public String getLoginUserId() {
        return Optional.ofNullable((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .orElseThrow(() -> new CommonException(BError.NOT_VALID, "User"));
    }

    public boolean isValidUser(User executeUser, User originUser) {
        //admin User일 때는 항상 모든 명령에 대해 유효하다고 판단한다.
        if (executeUser.getId().equals(AuthConstant.ADMIN_USER)) {
            return true;
        }
        return executeUser.getId().equals(originUser.getId());
    }

    @Transactional(readOnly = true)
    public List<User> getFollowingUser(){
        String loginUserId = getLoginUserId();
        return userRepository.findFollowingUser(loginUserId);
    }
}
