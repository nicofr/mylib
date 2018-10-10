package mylib.services;

public abstract class Service {
	
	private final String name;
	
	public abstract void perform();
	
	public Service(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
