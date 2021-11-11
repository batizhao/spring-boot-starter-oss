package me.batizhao.oss.api;

import lombok.SneakyThrows;
import me.batizhao.oss.config.StorageProperties;

import java.io.FileInputStream;
import java.io.IOException;
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

    @SneakyThrows
    @Override
    public List list() {
        return Files.walk(Paths.get(getProperties().getUrl()), 1)
                .skip(1)
                .filter(p -> {
                    try {
                        return !Files.isHidden(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public List list(String prefix) {
        return Files.walk(Paths.get(getProperties().getUrl()), 1)
                .skip(1)
                .filter(p -> {
                    try {
                        return !Files.isHidden(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                })
                .filter(p -> p.toAbsolutePath().toString().replace(getProperties().getUrl(), "").contains("/" + prefix))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public List fullList() {
        return Files.walk(Paths.get(getProperties().getUrl()), Integer.MAX_VALUE)
                .skip(1)
                .filter(p -> {
                    try {
                        return !Files.isHidden(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                })
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public List fullList(String prefix) {
        return Files.walk(Paths.get(getProperties().getUrl()), Integer.MAX_VALUE)
                .skip(1)
                .filter(p -> {
                    try {
                        return !Files.isHidden(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                })
                .filter(Files::isRegularFile)
                .filter(p -> p.toAbsolutePath().toString().replace(getProperties().getUrl(), "").contains("/" + prefix))
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
