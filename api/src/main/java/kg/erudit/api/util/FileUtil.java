package kg.erudit.api.util;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Component
public class FileUtil {
    @Value("${file-directory}")
    private String root;

    public String encodeFileToBase64Binary(String fileName, String path) throws IOException {
        File file = new File(String.format("%s/%s/%s", root, path, fileName));
        if (!file.exists())
            return null;
        byte[] fileContent = Files.readAllBytes(file.toPath());
        return Base64.encodeBase64String(fileContent);
    }

    public File getFile(String path) {
        return new File(String.format("%s/%s", root, path));
//        return Files.readAllBytes(file.toPath());
    }

    public void saveFile(kg.erudit.common.inner.File file, String path) {
        byte[] data = Base64.decodeBase64(file.getBase64());
        File file1 = new File(String.format("%s/%s/%s", root, path, file.getFullFileName()));
        if (!file1.getParentFile().exists())
            file1.getParentFile().mkdirs();

        try (OutputStream stream = new FileOutputStream(file1)) {
            stream.write(data);
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(String path) {
        File file = new File(String.format("%s/%s/", root, path));
        file.delete();
    }

    public void deleteDirectory(String path) throws IOException {
        File file = new File(String.format("%s/%s/", root, path));
        if (!file.exists())
            return;
        FileUtils.deleteDirectory(file);
    }

    public void deleteOldFiles(String path, List<String> fileNames) throws IOException {
        File filesRoot = new File(String.format("%s/%s/", root, path));
        if (!filesRoot.exists() || filesRoot.listFiles() == null)
            return;
        Arrays.stream(filesRoot.listFiles())
                .filter(file -> !fileNames.contains(file.getName()))
                .forEach(File::delete);
//        for (File file : ){
//            if ()
//                file.delete();
//
//        }
    }

//    @PostConstruct
//    void init() throws IOException {
//        deleteDirectory("news/c1539c7ecbdb49b9b06fc340709ceb93");
//    }
}
