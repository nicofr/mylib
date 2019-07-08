package mylib.services;

public class QuitService implements ExportsTerminalService{
	
	private boolean quit = false;

	@Override
	public String performService() {
		quit = true;
		return "";
	}

	@Override
	public String getServiceName() {
		return "quit";
	}

	public boolean isQuit() {
		return quit;
	}

}
