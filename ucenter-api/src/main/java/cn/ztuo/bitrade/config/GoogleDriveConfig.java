
package cn.ztuo.bitrade.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

@Configuration
public class GoogleDriveConfig {

    @Bean
    public Drive googleDriveService() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("path/to/credentials.json"))
                .createScoped(Collections.singleton(DriveScopes.DRIVE_FILE));
        return new Drive.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName("ExchangeApp")
                .build();
    }
}
