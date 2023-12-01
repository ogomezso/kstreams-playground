package org.github.ogomezso.kstreams.domain;

import org.github.ogomezso.kstreams.domain.model.ChuckFact;

import com.github.javafaker.Faker;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChuckFactService implements ChuckFactPort {

  private final Faker faker = new Faker();

  @Override
  public ChuckFact buildFact() {
    return ChuckFact.builder()
        .fact(faker.chuckNorris().fact())
        .build();
  }
}
