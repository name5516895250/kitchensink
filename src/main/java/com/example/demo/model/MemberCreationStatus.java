package com.example.demo.model;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class MemberCreationStatus {

    private Map<String, String> violations;

    private String validationError;

    private Member member;

    public MemberCreationStatus(Set<ConstraintViolation<Member>> violations) {
        this.violations = new LinkedHashMap<>();
        for (ConstraintViolation violation : violations) {
            this.violations.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
    }

    public MemberCreationStatus(String validationError) {
        this.validationError = validationError;
    }

    public MemberCreationStatus(Member member) {
        this.member = member;
    }

    public boolean isSuccessful() {
        return violations == null && validationError == null;
    }

}
