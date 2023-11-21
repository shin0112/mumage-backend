package mumage.mumagebackend.service;

import lombok.RequiredArgsConstructor;
import mumage.mumagebackend.domain.Follow;
import mumage.mumagebackend.domain.User;
import mumage.mumagebackend.dto.FollowListDto;
import mumage.mumagebackend.repository.FollowsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowsRepository followsRepository;

    // 누가 누구 팔로우 했는지 follow 생성 후 db에 저장
    @Transactional
    public void save(User from, User to){
        if(isFollowing(from,to)) // 이미 팔로잉 중인 경우 예외 발생
            throw new IllegalStateException("이미 팔로잉 중입니다.");

        Follow follow = Follow.builder() // 팔로우 객체를 생성한다.
                .from(from)
                .to(to)
                .build();

        from.getFollowing().add(follow);
        to.getFollower().add(follow);

        followsRepository.save(follow); // db에 저장
    }

    // 팔로우 정보 db에서 삭제
    @Transactional
    public void delete(User from, User to){
        Follow follow = followsRepository.findByFromAndTo(from, to);

        follow.getTo().getFollower().remove(follow); // to의 팔로워에서 이 팔로우 정보를 삭제한다.
        follow.getFrom().getFollowing().remove(follow); // from의 팔로잉에서도 마찬가지

        followsRepository.delete(follow);
    }

    // 팔로워 목록 반환
    public Set<User> getFollower(Long userId){
        return followsRepository.getFollower(userId);
    }

    // 팔로잉 목록 반환
    public Set<User> getFollowing(Long userId){
        return followsRepository.getFollowing(userId);
    }

    // 팔로워 수 반환
    public Long countFollower(User user){
        return followsRepository.countFollower(user.getUserId());
    }

    // 팔로잉 수 반환
    public Long countFollowing(User user){
        return followsRepository.countFollowing(user.getUserId());
    }

    // from 유저가 to 유저를 팔로잉 중인지 반환
    public boolean isFollowing(User from, User to){
        Follow follow = followsRepository.findByFromAndTo(from, to);
        return follow != null;
    }

    // from 유저의 to 유저에 대한 팔로우 정보 dto
    public FollowListDto convertDto(User from, User to){
        return FollowListDto.builder()
                .picture(to.getProfileUrl())
                .userId(to.getUserId())
                .userName(to.getName())
                .isFollow(this.isFollowing(from,to))
                .build();
    }
}
