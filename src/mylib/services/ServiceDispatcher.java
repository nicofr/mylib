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

public class ServiceDispatcher {
	
	private final HashMap<String, Service> services;
	private static final ServiceDispatcher instance = new ServiceDispatcher();
	
	private ServiceDispatcher() {
		services = new HashMap<>();
	}
	
	private void _registerService(Class<Service> serviceClass) throws ServiceException {
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
	
	public static void registerService(Class<Service> serviceClass) throws ServiceException {
		instance._registerService(serviceClass);
	}
	
	public static void executeService(String name, Collection<String> flags, Map<String, String> params) throws ServiceException {
		instance._executeService(name, flags, params);
	}

	private void _executeService(String name, Collection<String> flags,  Map<String, String> params) throws ServiceException {
		Service service = services.get(name);
		if (service == null) throw new ServiceException(ServiceErrorId.NoSuchServiceException, name);
		for (String flagname: flags) {
			Field f = ServiceUtils.getFlagByName(flagname, service);
			ServiceUtils.setValue(f, service, Boolean.valueOf(true));
		}
		for (String paramName : params.keySet()) {
			if (! ServiceUtils.hasServiceParamWithName(service, paramName)) throw new ServiceException(ServiceErrorId.NoSuchParameter, paramName);
		}
		for (Field f : service.getClass().getDeclaredFields()) {
			if (ServiceUtils.isFieldServiceParameter(f)) {
				if (! params.containsKey(ServiceUtils.getParamName(f))) {
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
	}
	
	

}
