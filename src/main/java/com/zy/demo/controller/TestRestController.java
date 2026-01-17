package com.zy.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * TestRestController
 *
 * @author zy
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestRestController {

    @GetMapping("/get")
    public ResponseEntity<Object> get(@RequestParam(value = "id") Long id) {
        log.info("request param:id={}", id);
        return ResponseEntity.ok("get success");
    }

    @PostMapping("/post")
    public ResponseEntity<Object> post(@RequestBody String requestBody) {
        log.info("request param:requestBody={}", requestBody);
        return ResponseEntity.ok("post success");
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<Object> put(@PathVariable("id") Long id, @RequestBody String requestBody) {
        log.info("request param:id={},requestBody={}", id, requestBody);
        return ResponseEntity.ok("put success");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        log.info("request param:id={}", id);
        return ResponseEntity.ok("delete success");
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity<Object> patch(@PathVariable("id") Long id, @RequestBody Map<String, Object> map) {
        log.info("request param:id={},map={}", id, map);
        return ResponseEntity.ok("patch success");
    }

    @RequestMapping(method = RequestMethod.HEAD, value = "/head/{id}")
    public ResponseEntity<Object> head(@PathVariable("id") Long id) {
        log.info("request param:id={}", id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("last-modified", new Date().toString());
        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.OPTIONS, value = "/options")
    public ResponseEntity<Object> options() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Allow", "GET,POST,PUT,DELETE,PATCH,HEAD,OPTIONS");
        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }
}
