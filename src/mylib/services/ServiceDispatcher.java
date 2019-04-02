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
import mylib.services.util.ServiceUtils;

public class ServiceDispatcher {

	private final HashMap<String, ExportsService> services;
	private static final ServiceDispatcher instance = new ServiceDispatcher();

	static {
		try {
			registerService(HelpService.class);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	private ServiceDispatcher() {
		// standard help service
		services = new HashMap<>();
	}

	private <TYPE extends ExportsService> void _registerService(Class<TYPE> serviceClass) throws ServiceException {
		try {
			ExportsService serviceInstance = serviceClass.getConstructor().newInstance();
			if (services.values().stream().anyMatch(s -> s.getServiceName().equals(serviceInstance.getServiceName()))) {
				throw new ServiceException(ServiceErrorId.ServiceNameInUse, serviceInstance.getServiceName());
			}
			ServiceUtils.validateService(serviceInstance);
			services.put(serviceInstance.getServiceName(), serviceInstance);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new ServiceWrapperException(e);
		}
	}

	public static <TYPE extends ExportsService> void registerService(Class<TYPE> serviceClass) throws ServiceException {
		instance._registerService(serviceClass);
	}
	
	public static String executeService(String... args) throws ServiceException {
		String name = args[0];
		Map<String, List<String>> params = new HashMap<>();
		name = args[0];
		for (int i = 1; i < args.length; i++) {
			if (ServiceUtils.isArgParam(args[i])) {
				params.put(ServiceUtils.parseParamName(args[i]), ServiceUtils.getParamArguments(i, args));
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
		ExportsService service = services.get(name);
		if (service == null)
			throw new ServiceException(ServiceErrorId.NoSuchService, name);
		for (String paramName : params.keySet()) {
			if (!ServiceUtils.hasServiceParamWithName(service, paramName))
				throw new ServiceException(ServiceErrorId.NoSuchParameter, paramName);
		}
		for (Field f : service.getClass().getDeclaredFields()) {
			if (ServiceUtils.isFieldServiceParameter(f)) {
				if (!params.containsKey(ServiceUtils.getParamName(f))) {
					if (!ServiceUtils.isParamOptional(f)) {
						throw new ServiceException(ServiceErrorId.ParamNotOptional, ServiceUtils.getParamName(f));
					} else {
						continue;
					}
				}
				f.setAccessible(true);
				try {
					if (!f.getType().isAssignableFrom(List.class)) {
						// single parameter
						f.set(service, ServiceUtils.getMapper(f).map(params.get(f.getName()).get(0)));
					} else {
						// collection
						f.set(service, params.get(f.getName()).stream().map((ThrowingFunction<? super String, ? extends Object>)(arg -> ServiceUtils.getMapper(f).map(arg))).collect(Collectors.toList()));
					}
					
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new ServiceWrapperException(e);
				}
			}
		}
		return service.performService();
	}
	
	static Map<String, ExportsService> getRegisteredServices() {
		return instance.services;
	}
}
