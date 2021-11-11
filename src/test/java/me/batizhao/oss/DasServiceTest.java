package me.batizhao.oss;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.oss.api.StorageService;
import me.batizhao.oss.config.StorageAutoConfiguration;
import me.batizhao.oss.config.StorageProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author batizhao
 * @date 2021/10/29
 */
@ExtendWith(SpringExtension.class)
@Import({StorageAutoConfiguration.class})
@EnableConfigurationProperties(value = StorageProperties.class)
@TestPropertySource(properties = {"pecado.storage.location=das",
        "pecado.storage.url=/Users/batizhao/Documents/upload-dir/test"})
@Slf4j
public class DasServiceTest {

    @Autowired
    StorageService storageService;

    @Test
    void testList() {
        assertThat(storageService.list().size(), is(3));
    }

    @Test
    void testListPath() {
        assertThat(storageService.list("5a").size(), is(1));
        assertThat(storageService.list("xx").size(), is(1));
    }

    @Test
    void testFullList() {
        assertThat(storageService.fullList().size(), is(3));
    }

    @Test
    void testFullListPath() {
        assertThat(storageService.fullList("5a").size(), is(1));
        assertThat(storageService.fullList("6c").size(), is(1));
    }
}
