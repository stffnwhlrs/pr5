mongod

bin/zookeeper-server-start.sh config/zookeeper.properties

bin/kafka-server-start.sh config/server.properties

bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic great-page-alert

bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic fake-news-alert

bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic count-all-tweets

bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic tweet-comparison


bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic great-page-alert

bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic fake-news-alert

bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic count-all-tweets

bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic tweet-comparison
