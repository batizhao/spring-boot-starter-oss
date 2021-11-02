package me.batizhao.oss.api;

import me.batizhao.oss.config.StorageProperties;

import java.io.File;
import java.util.List;

/**
 * @author batizhao
 * @date 2021/11/1
 */
public class DasService extends BaseStorageService implements StorageService<File> {

    /**
     * Creates new instance
     *
     * @param properties the encoding properties
     */
    public DasService(StorageProperties properties) {
        super(properties);
    }

    @Override
    public List<File> list() {
        return null;
    }
}
