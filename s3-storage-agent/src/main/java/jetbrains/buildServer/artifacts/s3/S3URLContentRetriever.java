package jetbrains.buildServer.artifacts.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.intellij.openapi.util.io.StreamUtil;
import jetbrains.buildServer.agent.IOUtil;
import jetbrains.buildServer.artifacts.URLContentRetriever;
import jetbrains.buildServer.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import static com.amazonaws.regions.Regions.US_WEST_2;

/**
 * Created by Nikita.Skvortsov
 * date: 31.03.2016.
 */
public class S3URLContentRetriever implements URLContentRetriever {
  private static final String REGIONAL_HOST = "s3-" + US_WEST_2.getName() + ".amazonaws.com";
  private final AmazonS3 myClient;

  public S3URLContentRetriever(AmazonS3 s3client) {
    myClient = s3client;
  }

  @Nullable
  @Override
  public String downloadUrlTo(@NotNull String s, @NotNull File file) throws IOException {
    try {
      URL url = new URL(s);
      if (!REGIONAL_HOST.equals(url.getHost())) {
        throw new IOException("Not an amazon S3 url: " + s);
      }

      String[] bucketAndPath = url.getPath().split("/", 2);
      String bucket = URLDecoder.decode(bucketAndPath[0], "UTF-8");
      String path = URLDecoder.decode(bucketAndPath[1], "UTF-8");

      final GetObjectRequest request = new GetObjectRequest(bucket, path);
      final S3Object object = myClient.getObject(request);

      final String eTag = object.getObjectMetadata().getETag();

      file.getParentFile().mkdirs();
      file.createNewFile();

      S3ObjectInputStream is = null;
      FileOutputStream os = null;
      try {
        is = object.getObjectContent();
        os = new FileOutputStream(file);
        StreamUtil.copyStreamContent(is, os);
      } finally {
        FileUtil.close(is);
        FileUtil.close(os);
      }

      return eTag;

    } catch (MalformedURLException e) {
      throw new IOException(e);
    }
  }

  @Nullable
  @Override
  public String getDigest(@NotNull String s) throws IOException {
    try {
      URL url = new URL(s);
      if (!REGIONAL_HOST.equals(url.getHost())) {
        throw new IOException("Not an amazon S3 url: " + s);
      }

      String[] bucketAndPath = url.getPath().split("/", 2);
      String bucket = bucketAndPath[0];
      String path = bucketAndPath[1];

      final GetObjectRequest request = new GetObjectRequest(bucket, path);
      final S3Object object = myClient.getObject(request);

      return object.getObjectMetadata().getETag();

    } catch (MalformedURLException e) {
      throw new IOException(e);
    }
  }

  @Override
  public void interrupt() {

  }
}