package br.com.alura;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class BatchSendMessageService {
    private final Connection connection;

    BatchSendMessageService() throws SQLException {
        String url = "jdbc:sqlite:user_database.db";
        this.connection = DriverManager.getConnection(url);
        connection.createStatement().execute("""
                create table if not exists Users (
                uuid varchar(200) primary key,
                email varchar(200))""");
    }

    public static void main(String[] args) throws SQLException {
        var batchService = new BatchSendMessageService();
        var service = new KafkaService<String>(BatchSendMessageService.class.getSimpleName(),
                "ECOMMERCE_SEND_MESSAGE_TO_ALL_USERS",
                batchService::parse,
                String.class,
                Map.of());
        service.run();
    }

    private final KafkaDispatcher<User> userDispatcher = new KafkaDispatcher<>();

    void parse(ConsumerRecord<String, Message<String>> record) throws InterruptedException, ExecutionException, SQLException {
        System.out.println("------------------------------------------");
        System.out.println("Processing new batch");
        var message = record.value();
        System.out.println("Topic " + message.getPayload());

        for (User user : getAllUsers()) {
            userDispatcher.send(message.getPayload(), user.getUuid(), user, message.getId().continueWith(BatchSendMessageService.class.getSimpleName()));
        }
    }

    private List<User> getAllUsers() throws SQLException {
        var results = connection.prepareStatement("select uuid from Users").executeQuery();
        List<User> users = new ArrayList<>();
        while (results.next()) {
            users.add(new User(results.getString(1)));
        }
        return users;
    }
}
