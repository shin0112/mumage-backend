package mumage.mumagebackend.domain;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Follow {

    @Id @GeneratedValue
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user")
    private User from; // 누가 팔로우 하는가

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="to_user")
    private User to; // 누구를 팔로우 하는가

    @Builder
    Follow(User from, User to){
        this.from = from;
        this.to = to;
    }

}

