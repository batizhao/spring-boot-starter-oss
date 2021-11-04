package me.batizhao.oss.api;

import me.batizhao.oss.exception.StorageException;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

/**
 * @author batizhao
 * @date 2021/11/1
 */
public interface StorageService {

    /**
     * 列出所有对象
     *
     * @return List of items
     */
    List list();

    /**
     * 列出 path 下所有对象
     *
     * @return List of items
     */
    List list(Path path);

    /**
     * 列出所有对象，包括所有子对象
     *
     * @return List of items
     */
    List fullList();

    /**
     * 列出 path 下所有对象，包括所有子对象
     * @param path
     * @return
     */
    List fullList(Path path);

    /**
     * Upload file
     * @param source
     * @param file
     * @throws StorageException
     */
    void upload(Path source, InputStream file) throws StorageException;

    /**
     * Get file
     * @param path
     * @return
     * @throws StorageException
     */
    InputStream get(Path path) throws StorageException;


}
