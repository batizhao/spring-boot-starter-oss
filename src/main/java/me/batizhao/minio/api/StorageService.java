package me.batizhao.minio.api;

import java.util.List;

/**
 * @author batizhao
 * @date 2021/11/1
 */
public interface StorageService<T> {

    /**
     * List all objects at root of the bucket
     *
     * @return List of items
     */
    List<T> list();

}
