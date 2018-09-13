package com.org.service.blobstore;

/*
 * Copyright 2013 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//[START gcs_imports]
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.org.util.FileStorageProperties;
import com.google.appengine.api.blobstore.*;

import java.io.File;
import java.io.FileInputStream;
//[END gcs_imports]
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;


/**
 * A simple servlet that proxies reads and writes to its Google Cloud Storage bucket.
 */
@Service
public class FileStorageService {

  public static final boolean SERVE_USING_BLOBSTORE_API = false;

  /**
   * This is where backoff parameters are configured. Here it is aggressively retrying with
   * backoff, up to 10 times but taking no more that 15 seconds total to do so.
   */
  private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
      .initialRetryDelayMillis(10)
      .retryMaxAttempts(10)
      .totalRetryPeriodMillis(15000)
      .build());

  /**Used below to determine the size of chucks to read in. Should be > 1kb and < 10MB */
  private static final int BUFFER_SIZE = 2 * 1024 * 1024;

  /**
   * Retrieves a file from GCS and returns it in the http response.
   * If the request path is /gcs/Foo/Bar this will be interpreted as
   * a request to read the GCS file named Bar in the bucket Foo.
   */
  public void doPost(File file, String fileName) throws IOException {
	    GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
	    GcsFilename gcsFileName = getFileName(fileName);
	    GcsOutputChannel outputChannel;
	    outputChannel = gcsService.createOrReplace(gcsFileName, instance);
	    copy(new FileInputStream(file), Channels.newOutputStream(outputChannel));
	  }

public OutputStream getOutputStream(String fileName) throws IOException {
	  GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
	  GcsFilename gcsFileName = getFileName(fileName);
	  GcsOutputChannel outputChannel;
	  outputChannel = gcsService.createOrReplace(gcsFileName, instance);
	  return Channels.newOutputStream(outputChannel);
}

public void doPost(InputStream inputStream, String fileName) throws IOException {
	    GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
	    GcsFilename gcsFileName = getFileName(fileName);
	    GcsOutputChannel outputChannel;
	    outputChannel = gcsService.createOrReplace(gcsFileName, instance);
	    copy(inputStream, Channels.newOutputStream(outputChannel));
	  }

public InputStream doGet(String fileName) throws IOException {
	    GcsFilename gcsFileName = getFileName(fileName);
	    if (SERVE_USING_BLOBSTORE_API) {
	      BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	      BlobKey blobKey = blobstoreService.createGsBlobKey(
	          "/gs/" + gcsFileName.getBucketName() + "/" + gcsFileName.getObjectName());
	      blobstoreService.serve(blobKey, null);
	      return null;
	    } else {
	      GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(gcsFileName, 0, BUFFER_SIZE);
	      return Channels.newInputStream(readChannel);
	      
	    }
	  }

public void doGet( HttpServletResponse resp, String fileName) throws IOException {
	GcsFilename gcsFileName = getFileName(fileName);
    if (SERVE_USING_BLOBSTORE_API) {
      BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
      BlobKey blobKey = blobstoreService.createGsBlobKey(
          "/gs/" + gcsFileName.getBucketName() + "/" + gcsFileName.getObjectName());
      blobstoreService.serve(blobKey, resp);
    } else {
      GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(gcsFileName, 0, BUFFER_SIZE);
      copy(Channels.newInputStream(readChannel), resp.getOutputStream());
    }
  }

private GcsFilename getFileName(HttpServletRequest req) {
    String[] splits = req.getRequestURI().split("/", 4);
    if (!splits[0].equals("") || !splits[1].equals("gcs")) {
      throw new IllegalArgumentException("The URL is not formed as expected. " +
          "Expecting /gcs/<bucket>/<object>");
    }
    return new GcsFilename(splits[2], splits[3]);
  }

public boolean delete(String filename) {
	try {
		return gcsService.delete(getFileName(filename));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return false;
}

	private GcsFilename getFileName(String filename) {
	  	return new GcsFilename(FileStorageProperties.BUCKET_NAME, filename);
	  }

	  /**
	   * Transfer the data from the inputStream to the outputStream. Then close both streams.
	   */
	  private void copy(InputStream input, OutputStream output) throws IOException {
	    try {
	      byte[] buffer = new byte[BUFFER_SIZE];
	      int bytesRead = input.read(buffer);
	      while (bytesRead != -1) {
	        output.write(buffer, 0, bytesRead);
	        bytesRead = input.read(buffer);
	      }
	    } finally {
	      input.close();
	      output.close();
	    }
	  }
}
