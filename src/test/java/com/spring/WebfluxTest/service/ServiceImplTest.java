package com.spring.WebfluxTest.service;

import com.spring.WebfluxTest.domain.Item;
import com.spring.WebfluxTest.repository.ItemReactiveRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
class ServiceImplTest {
    @Autowired
    private CartService cartService;

    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    private final int testItemSize = 24;
    private final Random random = new Random();

    @BeforeEach
    void setItem() {
        StepVerifier.create(
                itemReactiveRepository.deleteAll()
        ).verifyComplete();

        // Create Item List
        List<String> itemNameList = Arrays.asList(new String[]{"Lego","LeagueOfLegends","StarCraft","Robot","Banana","Beatles","BattleField","BMW"});
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
//                            Long.valueOf(random.nextInt(1000))
                            Long.valueOf((i+1) * 10)
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
    public void findByNameFluxTest() {
        StepVerifier.create(
                cartService.itemSearchByName("BMW", "MadeBy Germany", true)
        ).expectNextMatches(item -> {
            Assertions.assertThat(item.getId()).isNotBlank();
            Assertions.assertThat(item.getName()).isEqualTo("BMW");
            Assertions.assertThat(item.getDescription()).isEqualTo("MadeBy Germany");
            Assertions.assertThat(item.getPrice()).isEqualTo(80L);
            return true;
        }).verifyComplete();
    }

    @Test
    public void findByNameFluxMultiTest() {
        // testItemSize between 21 ~ 24
        StepVerifier.create(
                cartService.itemSearchByName("Robot", "MadeBy Japan", false).count()
        ).expectNextMatches(count -> {
                Assertions.assertThat(count).isEqualTo(3);
            return true;
        }).verifyComplete();
    }
}