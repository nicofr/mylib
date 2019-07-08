package mylib.services;

import mylib.services.annotations.ExportParam;
import mylib.services.util.TerminalServiceUtils;

public class HelpService implements ExportsTerminalService {
	
	@ExportParam(Ident = "n", Optional = true, Help = "Name of service of which help text should be printed")
	private String targetname;

	@Override
	public String performService() {
		if (targetname != null) {
			if (TerminalDispatcher.getRegisteredServices().containsKey(targetname)) {
				ExportsTerminalService service = TerminalDispatcher.getRegisteredServices().get(targetname);
				return getServiceName() +": " + service.getHelpText() + TerminalServiceUtils.printAllParamHelp(service);
			} else {
				return "No such servcie";
			}
		} else {
			String res = "";
			for (ExportsTerminalService service : TerminalDispatcher.getRegisteredServices().values()) {
				res = res + service.getServiceName() + ": " + service.getHelpText() + TerminalServiceUtils.printAllParamHelp(service) + System.lineSeparator();
			}
			return res;
		}
	}

	@Override
	public String getServiceName() {
		return "help";
	}
	
	 @Override
	public String getHelpText() {
		return "Prints help text of all services or specific service. Use with parameter -n <servicename> to print help text of specific service.";
	}

}
