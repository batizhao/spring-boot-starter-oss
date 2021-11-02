package me.batizhao.minio.api;

import me.batizhao.minio.config.StorageProperties;

import java.io.File;
import java.util.List;

/**
 * @author batizhao
 * @date 2021/11/1
 */
public class LocalStorageService extends BaseStorageService implements StorageService<File> {

    /**
     * Creates new instance
     *
     * @param properties the encoding properties
     */
    public LocalStorageService(StorageProperties properties) {
        super(properties);
    }

    @Override
    public List<File> list() {
        return null;
    }
}
