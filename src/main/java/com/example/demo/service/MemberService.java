package com.example.demo.service;

import com.example.demo.dao.MemberRepository;
import com.example.demo.model.Member;
import com.example.demo.model.MemberCreationStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MemberService {

    @Autowired
    private Validator validator;

    @Autowired
    private MemberRepository repository;

    public MemberCreationStatus createMember(Member member) {
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        if (!violations.isEmpty()) {
            return new MemberCreationStatus(violations);
        }

        // Check the uniqueness of the email address
        if (emailAlreadyExists(member.getEmail())) {
            return new MemberCreationStatus("Unique Email Violation");
        }
        return new MemberCreationStatus(repository.insert(member));
    }


    public boolean emailAlreadyExists(String email) {
        Member member = null;
        member = repository.findByEmail(email);
        return member != null;
    }

    public List<Member> findAllOrderByName() {
        return repository.findAll(Sort.by("name"));
    }

    public Member getMember(String id) {
        Optional<Member> op = repository.findById(id);
        return op.orElse(null);
    }
}
