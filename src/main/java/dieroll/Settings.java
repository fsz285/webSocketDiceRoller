package dieroll;

public class Settings {
	
	@Override
	public String toString() {
		return "Settings [name=" + name + ", color=" + color + "]";
	}

	public String name;
	
	public String color;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	
	public String getColor() {
		return color;
	}

}
