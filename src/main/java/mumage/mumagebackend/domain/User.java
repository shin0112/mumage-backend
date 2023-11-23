package mumage.mumagebackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false, length = 15, unique = true)
    private String loginId;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, length = 10)
    private String name;
    @Column(nullable = false, length = 15, unique = true)
    private String nickname;
    @Column
    private String profileUrl;
    @Column
    private String email;
    @Column
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Comments> commentsList; // 회원이 단 댓글 목록

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Likes> likesList; // 회원이 누른 좋아요 목록

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private Set<Posts> postsList; // 회원이 작성한 글 목록

    @OneToMany(mappedBy = "from", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Follow> following; // 팔로잉 목록

    @OneToMany(mappedBy = "to", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Follow> follower; // 팔로워 목록

    @ManyToMany
    @JoinTable(name = "user_genre",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres; // 선택한 장르

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.getRole()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.loginId;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

        public Set<Comments> getCommentsList() {
        return commentsList;
    }

    public Set<Likes> getLikesList() {
        return likesList;
    }

    public void setCommentsList(Set<Comments> commentsList) {
        this.commentsList = commentsList;
    }

    public void setLikesList(Set<Likes> likesList) {
        this.likesList = likesList;
    }

    public Set<Posts> getPostsList() {
        return postsList;
    }

    public void setPostsList(Set<Posts> postsList) {
        this.postsList = postsList;
    }

    public Set<Follow> getFollowing() {
        return following;
    }

    public void setFollowing(Set<Follow> following) {
        this.following = following;
    }

    public Set<Follow> getFollower() {
        return follower;
    }

    public void setFollower(Set<Follow> follower) {
        this.follower = follower;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }
}

