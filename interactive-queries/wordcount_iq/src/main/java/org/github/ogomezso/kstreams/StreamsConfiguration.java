package org.github.ogomezso.kstreams;

import java.util.Properties;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

public class StreamsConfiguration {

        public static Properties createStreamConfig(Properties config) {

                Properties streamsConfiguration = new Properties();

                final String port = config.getProperty("port", "9080");
                final String host = config.getProperty("host", "localhost");
                final String topic = config.getProperty("topic", "text-lines-topic");
                final String bootstrapServers = config.getProperty("bootstrap.servers", "localhost:9092");
                final String securityProtocol = config.getProperty("security.protocol", "SASL_SSL");
                final String trustStoreLocation = config.getProperty("ssl.truststore.location", "/truststore.p12");
                final String trustStorePassword = config.getProperty("ssl.truststore.password", "mystorepassword");
                final String saslMechanism = config.getProperty("sasl.mechanism", "PLAIN");
                final String saslJaasConfig = config.getProperty("sasl.jaas.config");
                final String applicationId = config.getProperty("application.id", "wordcount-iq-example");
                final String clientId = config.getProperty("client.id", "wordcount-iq-example-client");

                streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG,
                                applicationId);
                streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG,
                                clientId);
                streamsConfiguration.put(StreamsConfig.REPLICATION_FACTOR_CONFIG,3);            
                streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
                                bootstrapServers);
                streamsConfiguration.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);

                streamsConfiguration.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
                                trustStoreLocation);
                streamsConfiguration.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, trustStorePassword);
                streamsConfiguration.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
                streamsConfiguration.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
                streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
                                Serdes.String().getClass());
                streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
                                Serdes.String().getClass());
                streamsConfiguration.put(StreamsConfig.APPLICATION_SERVER_CONFIG,
                                host + ":" + port);

                streamsConfiguration.put("host", host);
                streamsConfiguration.put("topic", topic);
                streamsConfiguration.put("port", port);

                return streamsConfiguration;
        }
}
