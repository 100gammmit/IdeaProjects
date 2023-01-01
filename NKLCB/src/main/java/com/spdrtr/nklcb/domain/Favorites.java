package com.spdrtr.nklcb.domain;

import lombok.Setter;

import javax.persistence.*;

@Entity
public class Favorites {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private long id;
    @ManyToOne @JoinColumn(name = "article_id") private Article article;
    @ManyToOne @JoinColumn(name = "member_id") private Member member;
}
