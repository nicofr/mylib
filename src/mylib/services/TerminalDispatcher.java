package mylib.services;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import mylib.lambdautils.ThrowingFunction;
import mylib.services.exceptions.ServiceErrorId;
import mylib.services.exceptions.ServiceException;
import mylib.services.exceptions.ServiceWrapperException;
import mylib.services.util.TerminalServiceUtils;

public class TerminalDispatcher {

	private final HashMap<String, ExportsTerminalService> services;
	private static final TerminalDispatcher instance = new TerminalDispatcher();

	static {
		try {
			registerService(HelpService.class);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	private TerminalDispatcher() {
		// standard help service
		services = new HashMap<>();
	}

	private <TYPE extends ExportsTerminalService> void _registerService(Class<TYPE> serviceClass) throws ServiceException {
		try {
			ExportsTerminalService serviceInstance = serviceClass.getConstructor().newInstance();
			if (services.values().stream().anyMatch(s -> s.getServiceName().equals(serviceInstance.getServiceName()))) {
				throw new ServiceException(ServiceErrorId.ServiceNameInUse, serviceInstance.getServiceName());
			}
			TerminalServiceUtils.validateService(serviceInstance);
			services.put(serviceInstance.getServiceName(), serviceInstance);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new ServiceWrapperException(e);
		}
	}

	public static <TYPE extends ExportsTerminalService> void registerService(Class<TYPE> serviceClass) throws ServiceException {
		instance._registerService(serviceClass);
	}
	
	public static String executeService(String... args) throws ServiceException {
		String name = args[0];
		Map<String, List<String>> params = new HashMap<>();
		name = args[0];
		for (int i = 1; i < args.length; i++) {
			if (TerminalServiceUtils.isArgParam(args[i])) {
				params.put(TerminalServiceUtils.parseParamName(args[i]), TerminalServiceUtils.getParamArguments(i, args));
			}
		}
		return executeService(name, params);
	}

	public static String executeService(String name, Map<String, List<String>> params)
			throws ServiceException {
		return instance._executeService(name, params);
	}

	private String _executeService(String name, Map<String, List<String>> params)
			throws ServiceException {
		ExportsTerminalService service = services.get(name);
		if (service == null)
			throw new ServiceException(ServiceErrorId.NoSuchService, name);
		for (String paramName : params.keySet()) {
			if (!TerminalServiceUtils.hasServiceParamWithName(service, paramName))
				throw new ServiceException(ServiceErrorId.NoSuchParameter, paramName);
		}
		for (Field f : service.getClass().getDeclaredFields()) {
			if (TerminalServiceUtils.isFieldServiceParameter(f)) {
				if (!params.containsKey(TerminalServiceUtils.getParamName(f))) {
					if (!TerminalServiceUtils.isParamOptional(f)) {
						throw new ServiceException(ServiceErrorId.ParamNotOptional, TerminalServiceUtils.getParamName(f));
					} else {
						continue;
					}
				}
				f.setAccessible(true);
				try {
					if (!f.getType().isAssignableFrom(List.class)) {
						// single parameter
						f.set(service, TerminalServiceUtils.getMapper(f).map(params.get(f.getName()).get(0)));
					} else {
						// collection
						f.set(service, params.get(f.getName()).stream().map((ThrowingFunction<? super String, ? extends Object>)(arg -> TerminalServiceUtils.getMapper(f).map(arg))).collect(Collectors.toList()));
					}
					
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new ServiceWrapperException(e);
				}
			}
		}
		return service.performService();
	}
	
	static Map<String, ExportsTerminalService> getRegisteredServices() {
		return instance.services;
	}
}
