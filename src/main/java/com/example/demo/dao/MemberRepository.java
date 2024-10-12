package com.example.demo.dao;

import com.example.demo.model.Member;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MemberRepository {

    private List<Member> members = new ArrayList<>();

    @PostConstruct
    void postConstruct() {
        Member m = new Member();
        m.setName("First");
        m.setEmail("first@gmail.com");
        m.setPhoneNumber("1111111111");
        insert(m);
    }

    public Member findByEmail(String email) {
        for(Member member: members) {
            if (member.getEmail().equals(email)) {
               return member;
            }
        }
        return null;
    }

    public List<Member> findAllByNameAsc() {
        members.sort(Comparator.comparing(Member::getName));
        return members;
    }

    public Member insert(Member member) {
        member.setId(String.valueOf(System.currentTimeMillis()));
        members.add(member);
        return member;
    }

    public Member findById(String id) {
        for(Member member: members) {
            if (member.getId().equals(id)) {
                return member;
            }
        }
        return null;
    }
}
