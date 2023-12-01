package org.github.ogomezso.kstreams.infrastructure.kafka;

import org.github.ogomezso.kstreams.domain.model.ChuckFact;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ChuckAdapter {

  ChuckFact sendFact(String topic) throws JsonProcessingException;
}
