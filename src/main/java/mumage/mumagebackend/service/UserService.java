package mumage.mumagebackend.service;

import mumage.mumagebackend.domain.User;
import mumage.mumagebackend.dto.UserJoinDto;
import mumage.mumagebackend.dto.UserRequestDto;
import mumage.mumagebackend.dto.UserResponseDto;
import mumage.mumagebackend.exception.CustomException;
import mumage.mumagebackend.exception.ErrCode;
import mumage.mumagebackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
    회원 가입 : create
     */
    public User join(UserJoinDto userJoinDto) {

        User user = new User();
        user.setLoginId(userJoinDto.getJoinId());
        user.setPassword(userJoinDto.getPassword());
        user.setName(userJoinDto.getName());
        user.setNickname(userJoinDto.getNickname());
        userRepository.save(user);
        return user;
    }

    /*
    회원 탈퇴 : delete
     */
    public boolean withdraw(Long id) {
        Optional<User> member = userRepository.findById(id);
        if (member.isPresent()) {
            userRepository.delete(member.get());
            return true;
        } else {
            throw new CustomException(ErrCode.NOT_EXIST_USER, HttpStatus.NOT_FOUND);
        }
    }

    /*
    중복 조회
     */

    // 아이디
    public boolean existLoginId(String loginId) {
        Optional<User> byLoginId = userRepository.findByLoginId(loginId);
        if (byLoginId.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    // 닉네임
    public boolean existNickname(String nickname) {
        Optional<User> byNickname = userRepository.findByNickname(nickname);
        if (byNickname.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    public UserResponseDto update(Long userId, UserRequestDto userRequestDto) {

        Optional<User> oUser = userRepository.findByUserId(userId);

        if (oUser.isPresent()) {
            new NotFoundException("해당 유저 없음");
        }

        User user = oUser.get();

        if(userRequestDto.getPassword() != null)
            user.setPassword(userRequestDto.getPassword());
        if(userRequestDto.getName() != null)
        user.setName(userRequestDto.getName());
        if(userRequestDto.getNickname() != null)
        user.setNickname(userRequestDto.getNickname());

        UserResponseDto response = UserResponseDto.builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .password(user.getPassword())
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileUrl(user.getProfileUrl())
                .build();

        return response;
    }

    public UserResponseDto read(Long userId) {

        Optional<User> user = userRepository.findByUserId(userId);

        UserResponseDto res = UserResponseDto.builder()
                .userId(user.get().getUserId())
                .loginId(user.get().getLoginId())
                .password(user.get().getPassword())
                .name(user.get().getName())
                .nickname(user.get().getNickname())
                .email(user.get().getEmail())
                .profileUrl(user.get().getProfileUrl())
                .build();
        return res;

    }
    /*
    전체 회원 조회
     */
    public List<User> findMembers() {
        return userRepository.findAll();
    }

    /*
    특정 회원 조회
     */
    public Optional<User> findOne(Long memberId) {
        return userRepository.findById(memberId);
    }

}
