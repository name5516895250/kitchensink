package com.example.demo.rest;

import com.example.demo.model.Member;
import com.example.demo.model.MemberCreationStatus;
import com.example.demo.service.MemberService;

import org.apache.commons.text.StringEscapeUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.net.URI;
import java.util.List;


@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/")
    public ResponseEntity<Void> getIndex() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/index.html"))
                .build();
    }

    @PostMapping(value = "/api/member", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberCreationStatus> postMember(@RequestBody Member member) {
        MemberCreationStatus status = memberService.createMember(member);
        return status.isSuccessful() ? ResponseEntity.ok(status) : ResponseEntity.badRequest().body(status);
    }

    @GetMapping(value = "/api/members", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Member>> getMembers() {
        return ResponseEntity.ok(memberService.findAllOrderByName());
    }

    @GetMapping(value = "/rest/members/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getXMLMember(@PathVariable("id") String id) {
        Member m = memberService.getMember(id);
        return m != null ? ResponseEntity.ok(getXmlString(m)) : ResponseEntity.notFound().build();
    }

    @SneakyThrows
    private String getXmlString(Member m) {
        return "<member><id>" + StringEscapeUtils.escapeXml11(m.getId()) +
                "</id><name>" + StringEscapeUtils.escapeXml11(m.getName()) +
                "</name><email>" + StringEscapeUtils.escapeXml11(m.getEmail()) +
                "</email><phoneNumber>" + StringEscapeUtils.escapeXml11(m.getPhoneNumber()) +
                "</phoneNumber></member>";

    }

    @GetMapping(value = "/rest/members", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getXMLMembers() {
        StringBuilder sb = new StringBuilder("<collection>");
        memberService.getMembers().forEach(m -> sb.append(getXmlString(m)));
        sb.append("</collection>");
        return ResponseEntity.ok(sb.toString());
    }
}
