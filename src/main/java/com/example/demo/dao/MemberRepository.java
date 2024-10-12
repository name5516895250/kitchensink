package com.example.demo.dao;

import com.example.demo.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface MemberRepository extends MongoRepository<Member, String> {

    Member findByEmail(String email);

}
