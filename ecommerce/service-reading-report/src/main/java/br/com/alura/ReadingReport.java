package br.com.alura;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class ReadingReport {

    // private static final Path SOURCE = Paths.get(ReadingReport.class.getClassLoader().getResource("report.txt").toURI());
    public static void main(String[] args) {
        var readingReportService = new ReadingReport();
        var service = new KafkaService<User>(ReadingReport.class.getSimpleName(),
                "ECOMMERCE_USER_GENERATE_READING_REPORT",
                readingReportService::parse,
                Map.of());
        service.run();
    }

    void parse(ConsumerRecord<String, Message<User>> record) throws IOException, URISyntaxException {
        Path SOURCE = Paths.get(ReadingReport.class.getClassLoader().getResource("report.txt").toURI());
        System.out.println("------------------------------------------");
        System.out.println("Processing report for" + record.value());

        var user = record.value().getPayload();
        var target = new File(user.getReportPath());
        IO.copyTo(SOURCE, target);
        IO.append(target, "Created for " + user.getUuid());

        System.out.println("File created: " + target.getAbsolutePath());
    }
}
