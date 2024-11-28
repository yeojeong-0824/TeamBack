package com.yeojeong.application.domain.member.domain;

import org.springframework.data.repository.CrudRepository;

public interface RedisAuthedRepository extends CrudRepository<RedisAuthed, String> {
}
