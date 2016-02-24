package dieroll;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SettingsController {

	@Value("${settingsPath}")
	private String settingsPath;

	@MessageMapping("/settings")
	@SendTo("/topic/settings")
	public Settings settings(Settings settings) throws Exception {
		System.err.println(settings);
		Properties p = new Properties();
		try (InputStream is = new FileInputStream(settingsPath)) {
			p.load(is);
		}
		try (OutputStream os = new FileOutputStream(settingsPath)) {
			if (settings.getColor() != null) {
				p.put(settings.getName() + "." + SettingsConstants.COLOR, settings.getColor());
				p.store(os, settingsPath);
			} else {
				String color = p.getProperty(settings.getName() + "." + SettingsConstants.COLOR);
				if (color != null) {
					settings.setColor(color);
				} else {
					settings.setColor(SettingsConstants.DEFAULT_COLOR);
				}
			}
		}
		return settings;
	}

//	@Override
//	public void onApplicationEvent(ContextRefreshedEvent arg0) {
//		File settingsFile = new File(settingsPath);
//		if (!settingsFile.exists()) {
//			settingsFile.getParentFile().mkdirs();
//			try {
//				settingsFile.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
	

}
