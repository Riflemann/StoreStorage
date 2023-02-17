package com.storage.storagestoresocks.services;

import java.nio.file.Path;
import java.util.Map;

public interface FileService {

    boolean saveToFile(String fileName, Map<? extends Number, ? extends Object> hashMap);

    boolean cleanFile(String fileName);

    String readFile(String fileName);

    Path createTempFile(String suffix);
}
