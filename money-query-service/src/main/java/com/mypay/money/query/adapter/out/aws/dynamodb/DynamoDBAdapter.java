package com.mypay.money.query.adapter.out.aws.dynamodb;

import com.mypay.money.query.adapter.axon.QueryMoneySumByAddress;
import com.mypay.money.query.application.port.out.GetMoneySumByRegionPort;
import com.mypay.money.query.application.port.out.InsertMoneyIncreaseEventByAddress;
import com.mypay.money.query.domain.MoneySumByRegion;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class DynamoDBAdapter implements GetMoneySumByRegionPort, InsertMoneyIncreaseEventByAddress {
    private static final String TABLE_NAME = "MoneyIncreaseEventByRegion";
    private static final String ACCESS_KEY = "";
    private static final String SECRET_KEY = "";
    private final DynamoDbClient dynamoDbClient;
    private final MoneySumByAddressMapper moneySumByAddressMapper;

    public DynamoDBAdapter() {
        this.moneySumByAddressMapper = new MoneySumByAddressMapper();
        AwsBasicCredentials credentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
        this.dynamoDbClient = DynamoDbClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Override
    public int getMoneySumByAddress(String address) {
        String pk = address;
        String sk = "-1";
        MoneySumByAddress moneySumByAddress = getItem(pk, sk);
        return moneySumByAddress.getBalance();
    }

    private void updateItem(String pk, String sk, int balance) {
        try {
            HashMap<String, AttributeValue> attrMap = new HashMap<>();
            attrMap.put("PK", AttributeValue.builder().s(pk).build());
            attrMap.put("SK", AttributeValue.builder().s(sk).build());

            String balanceStr = String.valueOf(balance);
            // Create an UpdateItemRequest
            UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .key(attrMap)
                    .attributeUpdates(
                            new HashMap<String, AttributeValueUpdate>() {{
                                put("balance", AttributeValueUpdate.builder()
                                        .value(AttributeValue.builder().n(balanceStr).build())
                                        .action(AttributeAction.PUT)
                                        .build());
                            }}
                    ).build();


            UpdateItemResponse response = dynamoDbClient.updateItem(updateItemRequest);

            // 결과 출력.
            Map<String, AttributeValue> attributes = response.attributes();
            if (attributes != null) {
                for (Map.Entry<String, AttributeValue> entry : attributes.entrySet()) {
                    String attributeName = entry.getKey();
                    AttributeValue attributeValue = entry.getValue();
                }
            } else {
            }
        } catch (DynamoDbException e) {
        }
    }

    private void putItem(String pk, String sk, int balance) {
        try {
            HashMap<String, AttributeValue> attrMap = new HashMap<>();
            attrMap.put("PK", AttributeValue.builder().s(pk).build());
            attrMap.put("SK", AttributeValue.builder().s(sk).build());
            attrMap.put("balance", AttributeValue.builder().n(String.valueOf(balance)).build());

            PutItemRequest request = PutItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .item(attrMap)
                    .build();

            dynamoDbClient.putItem(request);
        } catch (DynamoDbException e) {

        }
    }

    // PK, SK 사용 단 1개의 결과만 나옴
    private MoneySumByAddress getItem(String pk, String sk) {
        try {
            HashMap<String, AttributeValue> attrMap = new HashMap<>();
            attrMap.put("PK", AttributeValue.builder().s(pk).build());
            attrMap.put("SK", AttributeValue.builder().s(sk).build());

            GetItemRequest request = GetItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .key(attrMap)
                    .build();

            GetItemResponse response = dynamoDbClient.getItem(request);
            if(response.hasItem()){
                return moneySumByAddressMapper.mapToMoneySumByAddress(response.item());
            }else{
                return null;
            }
        } catch (DynamoDbException e) {
        }
        return null;
    }

    // PK,SK를 사용하여 n개의 데이터 가져옴
    private void queryItem(String id) {
        try {
            // PK 만 써도 된다.
            HashMap<String, Condition> attrMap = new HashMap<>();
            attrMap.put("PK", Condition.builder()
                    .attributeValueList(AttributeValue.builder().s(id).build())
                    .comparisonOperator(ComparisonOperator.EQ)
                    .build());

            QueryRequest request = QueryRequest.builder()
                    .tableName(TABLE_NAME)
                    .keyConditions(attrMap)
                    .build();

            QueryResponse response = dynamoDbClient.query(request);
        } catch (DynamoDbException e) {

        }
    }

    @Override
    public void insertMoneyIncreaseEventByAddress(String addressName, int moneyIncrease) {
        // 1. raw event insert(Insert)
        // PK: 강남구#231214 SK : 5000
        String pk = addressName + "#" + "231214";
        String sk = String.valueOf(moneyIncrease);
        putItem(pk, sk, moneyIncrease);

        // 2. 지역 정보 잔액 증가(Query, Update)
        // 2-1. 지역별/일별 정보 PK: 강남구#231214#summary SK: -1 balance: + 5,000
        String summaryPk = pk + "#summary";
        String summarySk = "-1";
        MoneySumByAddress moneySumByAddress = getItem(summaryPk, summarySk);
        if(moneySumByAddress == null){
            putItem(summaryPk, summarySk,moneyIncrease);
        }else{
            int balance = moneySumByAddress.getBalance();
            balance += moneyIncrease;
            updateItem(summaryPk, summarySk, balance);
        }

        // 2-2. 지역별 정보 PK: 강남구 SK: -1 balance: + 5,000
        String summaryPk2 = addressName;
        String summarySk2 = "-1";
        MoneySumByAddress moneySumByAddress2 = getItem(summaryPk2, summarySk2);
        if(moneySumByAddress2 == null){
            putItem(summaryPk2, summarySk2, moneyIncrease);
        }else{
            int balance2 = moneySumByAddress2.getBalance();
            balance2 += moneyIncrease;
            updateItem(summaryPk2, summarySk2, balance2);
        }


    }

    @QueryHandler
    public MoneySumByRegion query (QueryMoneySumByAddress query){

        return MoneySumByRegion.generateMoneySumByRegion(
                new MoneySumByRegion.MoneySumByRegionId(UUID.randomUUID().toString()),
                new MoneySumByRegion.RegionName(query.getAddress()),
                new MoneySumByRegion.MoneySum(getMoneySumByAddress(query.getAddress()))
        );
    }
}

