package mylib.services;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import mylib.services.exceptions.ServiceErrorId;
import mylib.services.exceptions.ServiceException;
import mylib.services.exceptions.ServiceWrapperException;
import mylib.services.util.ServiceUtils;
import mylib.util.CollectionUtils;

public class ServiceDispatcher {

	private final HashMap<String, Service> services;
	private static final ServiceDispatcher instance = new ServiceDispatcher();

	private ServiceDispatcher() {
		services = new HashMap<>();
	}

	private <TYPE extends Service> void _registerService(Class<TYPE> serviceClass) throws ServiceException {
		try {
			Service serviceInstance = serviceClass.getConstructor().newInstance();
			if (services.values().stream().anyMatch(s -> s.getName().equals(serviceInstance.getName()))) {
				throw new ServiceException(ServiceErrorId.ServiceNameInUse, serviceInstance.getName());
			}
			ServiceUtils.validateService(serviceInstance);
			services.put(serviceInstance.getName(), serviceInstance);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new ServiceWrapperException(e);
		}
	}

	public static <TYPE extends Service> void registerService(Class<TYPE> serviceClass) throws ServiceException {
		instance._registerService(serviceClass);
	}
	
	public static String executeService(String... args) throws ServiceException {
		String name = args[0];
		Collection<String> flags = CollectionUtils.empty();
		Map<String, String> params = new HashMap<>();
		name = args[0];
		for (int i = 1; i < args.length; i++) {
			if (ServiceUtils.isArgFlag(args[i])) {
				flags.add(ServiceUtils.parseFlag(args[i]));
			} else if (ServiceUtils.isArgParam(args[i])) {
				params.put(ServiceUtils.parseParamName(args[i]), ServiceUtils.parseParamValue(args[i]));
			}
		}
		return executeService(name, flags, params);
	}

	public static String executeService(String name, Collection<String> flags, Map<String, String> params)
			throws ServiceException {
		return instance._executeService(name, flags, params);
	}

	private String _executeService(String name, Collection<String> flags, Map<String, String> params)
			throws ServiceException {
		Service service = services.get(name);
		if (service == null)
			throw new ServiceException(ServiceErrorId.NoSuchService, name);
		for (String flagname : flags) {
			Field f = ServiceUtils.getFlagByName(flagname, service);
			ServiceUtils.setValue(f, service, Boolean.valueOf(true));
		}
		for (String paramName : params.keySet()) {
			if (!ServiceUtils.hasServiceParamWithName(service, paramName))
				throw new ServiceException(ServiceErrorId.NoSuchParameter, paramName);
		}
		for (Field f : service.getClass().getDeclaredFields()) {
			if (ServiceUtils.isFieldServiceParameter(f)) {
				if (!params.containsKey(ServiceUtils.getParamName(f))) {
					if (!ServiceUtils.isParamOptional(f)) {
						throw new ServiceException(ServiceErrorId.ParamNotOptional, ServiceUtils.getParamName(f));
					}
				}
				f.setAccessible(true);
				try {
					f.set(service, ServiceUtils.getMapper(f).map(params.get(f.getName())));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new ServiceWrapperException(e);
				}
			}
		}
		return service.perform();
	}
}
