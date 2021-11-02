package me.batizhao.oss.api;

import me.batizhao.oss.config.StorageProperties;
import org.springframework.util.Assert;

/**
 * @author batizhao
 * @date 2021/11/2
 */
public abstract class BaseStorageService {

    /**
     * The encoding properties.
     */
    private final StorageProperties properties;

    /**
     * Creates new instance
     * @param properties the encoding properties
     */
    protected BaseStorageService(StorageProperties properties) {
        Assert.notNull(properties, "Properties can not be null");
        this.properties = properties;
    }

    protected StorageProperties getProperties() {
        return this.properties;
    }

}
