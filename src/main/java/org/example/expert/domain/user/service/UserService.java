package org.example.expert.domain.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.common.service.S3Service;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserImageResponse;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.dto.response.UserSearchResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    public UserResponse getUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidRequestException("User not found"));
        return new UserResponse(user.getId(), user.getEmail());
    }

    @Transactional
    public void changePassword(long userId, UserChangePasswordRequest userChangePasswordRequest) {
        validateNewPassword(userChangePasswordRequest);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        if (passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
        }

        if (!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new InvalidRequestException("잘못된 비밀번호입니다.");
        }

        user.changePassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
    }


    private static void validateNewPassword(UserChangePasswordRequest userChangePasswordRequest) {
        if (userChangePasswordRequest.getNewPassword().length() < 8 ||
                !userChangePasswordRequest.getNewPassword().matches(".*\\d.*") ||
                !userChangePasswordRequest.getNewPassword().matches(".*[A-Z].*")) {
            throw new InvalidRequestException("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
        }
    }

    public List<UserSearchResponse> findUsersByNickname(String nickname) {
        return userRepository.findByNickname(nickname).stream()
                .map(user -> new UserSearchResponse(user.getId(), user.getEmail(), user.getNickname())).toList();
    }

    @Transactional
    public UserImageResponse updateImage(AuthUser authUser, MultipartFile userImageRequest) {
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new InvalidRequestException("User not found"));

        if(userImageRequest.getName().isBlank()){
            throw new InvalidRequestException("이미지는 필수 값입니다.");
        }

        if (!user.getImageUrl().isEmpty()) {
            s3Service.delete(getImage(user.getImageUrl()));
        }
        String uploadUrl = s3Service.upload(userImageRequest);
        user.updateImage(uploadUrl);

        return new UserImageResponse(user.getId(), user.getEmail(), user.getNickname(), user.getImageUrl());
    }

    private String getImage(String profileUrl) {
        return profileUrl.substring(profileUrl.lastIndexOf("/") + 1);
    }
}
