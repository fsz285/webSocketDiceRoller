package dieroll;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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

}
