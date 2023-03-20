package com.whatachad.app.model.domain;

import com.whatachad.app.security.AuthConstant;
import com.whatachad.app.type.UserMetaType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "USER_TABLE")
public class User extends BaseTime {

    @Id
    @Column(name = "USER_ID")
    private String id;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @Column(unique = true)
    private String phone;

    @Column
    private boolean valid;

    @OneToMany(mappedBy = "user")
    List<Schedule> schedules = new ArrayList<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyEnumerated(EnumType.STRING)
    @CollectionTable(
            name = "USER_META",
            joinColumns = @JoinColumn(name = "ID")
    )
    @MapKeyColumn(name = "META_TYPE")
    @Column(name = "META_VALUE")
    private Map<UserMetaType, String> meta;

    public boolean isValid() {
        return id.equals(AuthConstant.ADMIN_USER) || valid;
    }
}
