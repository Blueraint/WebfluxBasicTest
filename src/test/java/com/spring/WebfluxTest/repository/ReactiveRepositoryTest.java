package com.spring.WebfluxTest.repository;

import com.spring.WebfluxTest.domain.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@DataMongoTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
class ReactiveRepositoryTest {
    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @Autowired
    CartReactiveRepository cartReactiveRepository;

    private static final Random random = new Random();

    private final int testItemSize = 16;

    @BeforeEach
    void setItem() {
        StepVerifier.create(
                itemReactiveRepository.deleteAll()
        ).verifyComplete();

        // Create Item List
        List<String> itemNameList = Arrays.asList(new String[]{"Lego","Apple","Banana","Robot","StarCraft","LeagueOfLegends","BattleField","BMW"});
        List<String> itemMadeByList = Arrays.asList(new String[]{"USA","China","Korea","Japan","India","UK","Canada","Germany"});
        List<Item> itemList = new ArrayList<>();

        IntStream.range(0,testItemSize).forEach(i -> {
//            itemList.add(new Item(
//                    itemNameList.get(random.nextInt(itemNameList.size())),
//                    "MadeBy " + itemMadeByList.get(random.nextInt(itemMadeByList.size())),
//                    Long.valueOf(random.nextInt(1000))
//                    )
//            );

            itemList.add(new Item(
                            itemNameList.get(i%itemNameList.size()),
                            "MadeBy " + itemMadeByList.get(i%itemMadeByList.size()),
                            Long.valueOf(random.nextInt(1000))
                    )
            );
        });

        itemList.forEach(item -> System.out.println("@@@ Inserted Item : " + item.toString()));

        StepVerifier.create(
                itemReactiveRepository.saveAll(itemList)
        ).expectNextMatches(item -> {
            System.out.println("### ReactiveRepository saved Item : " + item.toString());
            return true;
        })
                .expectNextCount(testItemSize-1) // cursor Next 값이 n-1 번만큼 돈다
                .verifyComplete();
    }

    @Test
    public void itemCountEqualTest() {
        StepVerifier.create(
                itemReactiveRepository.findAll().count()
        ).expectNextMatches(count -> {
            Assertions.assertThat(count).isEqualTo(testItemSize);
            return true;
        }).verifyComplete();
    }

    @Test
    public void itemSearchByNameCountEqualTest() {

        StepVerifier.create(
                itemReactiveRepository.findByNameContaining("Robot")
        ).expectNextMatches(item -> {
            System.out.println("### find Items : " + item.toString());
            return true;
        }).expectNextCount(1).verifyComplete();

        StepVerifier.create(
                itemReactiveRepository.findByNameContaining("Robot").count()
        ).expectNextMatches(count -> {
            Assertions.assertThat(count).isEqualTo(2);
            return true;
        }).verifyComplete();
    }

    @Test
    public void itemSearchByNameMonoTest() {
        StepVerifier.create(
                itemReactiveRepository.findByName("BMW")
        ).expectNextMatches(item -> {
            Assertions.assertThat(item.getId()).isNotBlank();
            Assertions.assertThat(item.getName()).isEqualTo("BMW");
            Assertions.assertThat(item.getDescription()).isEqualTo("MadeBy Germany");
            Assertions.assertThat(item.getPrice()).isLessThan(1000);
            return true;
        }).verifyComplete();
    }
}