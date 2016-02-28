package dieroll;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
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
		LOG.info("Using settings path: " + settingsPath);
	}

	// @Override
	// public void onApplicationEvent(ContextRefreshedEvent arg0) {
	// File settingsFile = new File(settingsPath);
	// if (!settingsFile.exists()) {
	// settingsFile.getParentFile().mkdirs();
	// try {
	// settingsFile.createNewFile();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//

}
