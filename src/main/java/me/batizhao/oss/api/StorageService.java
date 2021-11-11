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
     * 列出子文件或目录
     *
     * @return List of items
     */
    List list();

    /**
     * 列出子文件或目录，以 prefix 为前缀
     * @param prefix
     *
     * @return List of items
     */
    List list(String prefix);

    /**
     * 列出所有子文件
     *
     * @return List of regular file
     */
    List fullList();

    /**
     * 列出所有子文件，以 prefix 为前缀
     * @param prefix
     *
     * @return List of regular file
     */
    List fullList(String prefix);

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
