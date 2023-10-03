package mumage.mumagebackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 15)
    private String memberId;
    @Column(nullable = false, length = 15)
    private String password;
    @Column(nullable = false, length = 10)
    private String name;
    @Column(nullable = false, length = 15)
    private String nickname;

}
