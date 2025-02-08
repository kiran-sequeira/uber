package com.company.uber.repository;


import com.company.uber.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;


@Repository
public class UserRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<User> userTable;

    public UserRepository(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
    }

    @PostConstruct
    private void init() {
        userTable = enhancedClient.table("User", TableSchema.fromBean(User.class));
    }

    public void save(User user) {
        userTable.putItem(user);
    }

    public User findById(String userId) {
        return userTable.getItem(r -> r.key(Key.builder().partitionValue(userId).build()));
    }
}

