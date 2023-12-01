package org.github.ogomezso.kstreams.infrastructure.rest;

import java.time.Instant;

import org.github.ogomezso.kstreams.domain.model.ChuckFact;
import org.github.ogomezso.kstreams.infrastructure.rest.model.ChuckFactResponse;

class FactResponseMapper {

  ChuckFactResponse toResponse(ChuckFact fact) {
    return ChuckFactResponse.builder()
        .fact(fact.getFact())
        .timestamp(Instant.now().toEpochMilli())
        .build();
  }
}
