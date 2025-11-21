package br.com.alura;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class ReadingReport {

    private static final Path SOURCE = new File("src/main/resources/report.txt").toPath();
    public static void main(String[] args) {
        var readingReportService = new ReadingReport();
        var service = new KafkaService<User>(ReadingReport.class.getSimpleName(),
                "USER_GENERATE_READING_REPORT",
                readingReportService::parse,
                User.class,
                Map.of());
        service.run();
    }

    void parse(ConsumerRecord<String, User> record) throws IOException {
        System.out.println("------------------------------------------");
        System.out.println("Processing report for" + record.value());

        var user = record.value();
        var target = new File(user.getReportPath());
        IO.copyTo(SOURCE, target);
        IO.append(target, "Created for " + user.getUuid());

        System.out.println("File created: " + target.getAbsolutePath());
    }
}
