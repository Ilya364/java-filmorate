package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private long id;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    @Email
    private final String email;
    @NotBlank
    private final String login;
    private final Set<Long> friends = new HashSet<>();

    public void addAllFriends(Collection<Long> friends) {
        this.friends.addAll(friends);
    }

    public void addFriend(long friendId) {
        friends.add(friendId);
    }

    public void removeFriend(long friendId) {
        friends.remove(friendId);
    }
}
