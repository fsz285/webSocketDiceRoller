package dieroll;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SettingsController implements InitializingBean {
	
	private static final Logger LOG = LoggerFactory.getLogger(SettingsController.class);

	@Value("${settingsPath}")
	private String settingsPath;

	@MessageMapping("/settings")
	@SendTo("/topic/settings")
	public Settings settings(Settings settings) throws Exception {
		Properties p = new Properties();
		createSettingsFileIfNotExisting();
		try (InputStream is = new FileInputStream(settingsPath)) {
			p.load(is);
		}
		if (settings.getColor() != null) {
			p.put(settings.getName() + "." + SettingsConstants.COLOR, settings.getColor());
			try (OutputStream os = new FileOutputStream(settingsPath)) {
				p.store(os, null);
			}
		} else {
			String color = p.getProperty(settings.getName() + "." + SettingsConstants.COLOR);
			if (color != null) {
				settings.setColor(color);
			} else {
				settings.setColor(SettingsConstants.DEFAULT_COLOR);
			}
		}
		return settings;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(settingsPath == null || settingsPath.isEmpty()){
			LOG.error("No explicit settings path defined falling back to default.");
			File userDir = new File(System.getProperty("user.dir"));
			settingsPath = userDir.toPath().resolve("settings.properties").toAbsolutePath().toString();
		}
		LOG.info("Using settings path: " + settingsPath);
	}
	
	private void createSettingsFileIfNotExisting() throws IOException{
		Path settingsFile = Paths.get(settingsPath);
		if(!Files.exists(settingsFile)){
			Files.createFile(settingsFile);
		}
	}

}
