package org.github.ogomezso.kstreams;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.state.HostInfo;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.WindowStore;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class WordCountInteractiveQueriesExample {

  public static void main(final String[] args) throws Exception {

    final Properties streamsProperties = StreamsConfigHandler(args);

    final KafkaStreams streams = createStreams(streamsProperties);

    streams.cleanUp();
    streams.start();

    // Start the Restful proxy for servicing remote access to state stores
    final WordCountInteractiveQueriesRestService restService = startRestProxy(streams,
        streamsProperties.getProperty("host"),
        Integer.parseInt(streamsProperties.getProperty("port")));

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        streams.close();
        restService.stop();
      } catch (final Exception e) {
        // ignored
      }
    }));
  }

  static Properties StreamsConfigHandler(final String[] args) throws IOException {
    try (InputStream input = new FileInputStream(args[0])) {
      Properties config = new Properties();
      config.load(input);

      return StreamsConfiguration.createStreamConfig(config);
    } catch (IOException ex) {
      ex.printStackTrace();
      throw ex;
    }
  }

  static WordCountInteractiveQueriesRestService startRestProxy(final KafkaStreams streams,
      final String host,
      final int port) throws Exception {
    final HostInfo hostInfo = new HostInfo(host, port);
    final WordCountInteractiveQueriesRestService wordCountInteractiveQueriesRestService = new WordCountInteractiveQueriesRestService(
        streams, hostInfo);
    wordCountInteractiveQueriesRestService.start(port);
    return wordCountInteractiveQueriesRestService;
  }

  static KafkaStreams createStreams(final Properties streamsConfiguration) {
    final Serde<String> stringSerde = Serdes.String();
    final StreamsBuilder builder = new StreamsBuilder();
    final KStream<String, String> textLines = builder.stream(streamsConfiguration.getProperty("topic"),
        Consumed.with(Serdes.String(), Serdes.String()));

    final KGroupedStream<String, String> groupedByWord = textLines
        .flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
        .groupBy((key, word) -> word, Grouped.with(stringSerde, stringSerde));

    groupedByWord.count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("word-count")
        .withValueSerde(Serdes.Long()));

    groupedByWord.windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(1)))
        .count(Materialized.<String, Long, WindowStore<Bytes, byte[]>>as("windowed-word-count")
            .withValueSerde(Serdes.Long()));

    return new KafkaStreams(builder.build(), streamsConfiguration);
  }
}