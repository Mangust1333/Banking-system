package com.kubancevvladislav.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Сущность для представления связи между пользователями как их дружбы.
 * @author Кубанцев Владислав
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(
        name = "user_friends",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "friend_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(onlyExplicitlyIncluded = true)
public class UserFriendsEntity {
    @EmbeddedId
    private UserFriendsPK id;
    /** Логин первого пользователя */
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false, nullable = false)
    private UserEntity user;
    /** Логин второго пользователя (друга) */
    @ManyToOne
    @JoinColumn(name = "friend_id", insertable = false, updatable = false, nullable = false)
    private UserEntity friend;

    @Column(name = "user_login", nullable = false)
    private String userLogin;

    @Column(name = "friend_login", nullable = false)
    private String friendLogin;
    /**
     * Конструктор с установкой составного ключа.
     * @param user пользователь
     * @param friend друг
     */
    public UserFriendsEntity(UserEntity user, UserEntity friend) {
        this.user = user;
        this.friend = friend;
        this.userLogin = user.getLogin();
        this.friendLogin = friend.getLogin();
        this.id = new UserFriendsPK(user.getId(), friend.getId());
    }
}
