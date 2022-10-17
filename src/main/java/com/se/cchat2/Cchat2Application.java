package com.se.cchat2;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
public class Cchat2Application {

	public static void main(String[] args) throws IOException {

		ClassLoader classLoader = Cchat2Application.class.getClassLoader();
		File file = new File((classLoader.getResource("firebase_chatapplication.json")).getFile());
		FileInputStream firebase_serv = new FileInputStream(file.getAbsolutePath());

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(firebase_serv))
				.setDatabaseUrl("https://chatapplication-dd7ee-default-rtdb.asia-southeast1.firebasedatabase.app")
				.build();

		FirebaseApp.initializeApp(options);

		SpringApplication.run(Cchat2Application.class, args);
	}

}
