package mylib.services;

public interface ExportsTerminalService {
	
	public String performService();
	public String getServiceName();
	
	public default String getHelpText() {
		return "";
	}
}
