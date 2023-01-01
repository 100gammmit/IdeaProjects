package com.spdrtr.nklcb.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList ="memberId"),
        @Index(columnList ="memberPassword"),
        @Index(columnList ="memberName"),
})
@Entity
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private long id;
    @Setter @Column(nullable = false) private String memberId;
    @Setter @Column(nullable = false) private String memberPassword;
    @Setter @Column(nullable = false) private String memberName;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private final Set<Favorites> favorites = new LinkedHashSet<>();
}
