package me.batizhao.oss.api;

import io.minio.messages.Item;
import lombok.SneakyThrows;
import me.batizhao.oss.config.StorageProperties;
import me.batizhao.oss.exception.StorageException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author batizhao
 * @date 2021/11/1
 */
public class DasService extends BaseStorageService implements StorageService {

    /**
     * Creates new instance
     *
     * @param properties the encoding properties
     */
    public DasService(StorageProperties properties) {
        super(properties);
    }

    @Override
    public List list() {
        return null;
    }

    @Override
    public List list(Path path) {
        return null;
    }

    @Override
    public List fullList() {
        return null;
    }

    @SneakyThrows
    @Override
    public List fullList(Path path) {
        return Files.find(path, Integer.MAX_VALUE, (p, a) -> p.toString().endsWith(".vm") && a.isRegularFile())
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public void upload(Path source, InputStream file) {
        Path target = Paths.get(getProperties().getUrl()).resolve(source);
        Files.createDirectories(target.getParent());
        Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
    }

    @SneakyThrows
    @Override
    public InputStream get(Path path) {
        Path target = Paths.get(getProperties().getUrl()).resolve(path);
        return new FileInputStream(target.toFile());
    }
}
