package org.github.ogomezso.kstreams.domain.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ChuckFact {
  String fact;
}
