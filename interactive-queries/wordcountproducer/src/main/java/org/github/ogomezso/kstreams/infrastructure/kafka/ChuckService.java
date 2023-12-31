package org.github.ogomezso.kstreams.infrastructure.kafka;

import java.util.Properties;

import org.github.ogomezso.kstreams.domain.ChuckFactPort;
import org.github.ogomezso.kstreams.domain.ChuckFactService;
import org.github.ogomezso.kstreams.domain.model.ChuckFact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChuckService implements ChuckAdapter {

  private final ChuckFactPort chuckFactPort = new ChuckFactService();
  private final ChuckProducer producer;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public ChuckService(Properties appConfig) {
    this.producer = new ChuckProducer(appConfig);
  }

  @Override
  public ChuckFact sendFact(String topic) throws JsonProcessingException {
    ChuckFact fact = chuckFactPort.buildFact();
    String message = objectMapper.writeValueAsString(fact);
    producer.produceJsonMessage(topic,message);
    return fact;
  }
}
