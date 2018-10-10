package mylib.services.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collection;

import mylib.services.Service;
import mylib.services.annotations.Flag;
import mylib.services.annotations.Param;
import mylib.services.exceptions.ServiceErrorId;
import mylib.services.exceptions.ServiceException;
import mylib.services.exceptions.ServiceWrapperException;
import mylib.util.CollectionUtils;

public class ServiceUtils {
	
	private ServiceUtils() {}
	
	
	public static void validateService(Service service) throws ServiceException {
		Collection<String> paramNames = CollectionUtils.empty();
		Collection<String> flagNames = CollectionUtils.empty();
		for (Field field : service.getClass().getDeclaredFields()) {
			if (isFieldServiceParameter(field)) {
				validateParam(field, paramNames);
			}
			if (isFieldServiceFlag(field)) {
				validateFlag(field, flagNames);
			}
		}
	}
	
	private static boolean isFieldServiceFlag(Field f) {
		return f.getAnnotation(Flag.class) != null;
	}
	
	private static void validateFlag(Field f, Collection<String> flagNames) throws ServiceException {
		if (flagNames.contains(getFlagName(f))) throw new ServiceException(ServiceErrorId.ParamNameInUse, getParamName(f));
		if (! f.getType().equals(Boolean.class)) throw new ServiceException(ServiceErrorId.InvalidFlag, getParamName(f)+" is not of type Boolean");
		flagNames.add(getFlagName(f));
	}
	
	private static String getFlagName(Field f) throws ServiceException {
		if (isFieldServiceParameter(f)) {
			return f.getAnnotation(Flag.class).Name();
		}
		throw new ServiceException(ServiceErrorId.FieldIsNoFlag, f.getName());
	}

	public static Field getFlagByName(String name, Service service) throws ServiceException {
		Field f;
		try {
			f = service.getClass().getDeclaredField(name);
		} catch (SecurityException e) {
			throw new ServiceWrapperException(e);
		} catch (NoSuchFieldException e) {
			throw new ServiceException(ServiceErrorId.NoSuchFlag, name);
		}
		return f;
	}

	private static void validateParam(Field f, Collection<String> paramNames) throws ServiceException{
		if (paramNames.contains(getParamName(f))) throw new ServiceException(ServiceErrorId.ParamNameInUse, getParamName(f));
		if (!f.getType().equals(String.class) && hasStringMapper(f)) throw new ServiceException(ServiceErrorId.InvalidParameter, getParamName(f)+" is not of type String or has no String mapper");
		if (f.getModifiers() != Modifier.PRIVATE) throw new ServiceException(ServiceErrorId.InvalidParameter, getParamName(f) + " is not private");
		paramNames.add(getParamName(f));
	}
	
	public static boolean isFieldServiceParameter(Field f) {
		return f.getAnnotation(Param.class) != null;
	}
	
	public static String getParamName(Field f) throws ServiceException {
		if (isFieldServiceParameter(f)) {
			return f.getAnnotation(Param.class).Ident();
		}
		throw new ServiceException(ServiceErrorId.FieldIsNoParameter, f.getName());
	}
	
	private static boolean hasStringMapper(Field f) throws ServiceException {
		if (isFieldServiceParameter(f)) {
			try {
				return f.getAnnotation(Param.class).MapperClassName() != null &&  ParamStringMapper.class.isAssignableFrom(Class.forName(f.getAnnotation(Param.class).MapperClassName()));
			} catch (ClassNotFoundException e) {
				return false;
			}
		}
		throw new ServiceException(ServiceErrorId.FieldIsNoParameter, f.getName());
	}
	
	public static ParamStringMapper<?> getMapper(Field f) throws ServiceWrapperException {
		try {
			return (ParamStringMapper<?>) Class.forName(f.getAnnotation(Param.class).MapperClassName()).getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			throw new ServiceWrapperException(e);
		}
	}

	public static Field getParamByName(String name, Service service) throws ServiceException {
		Field f;
		try {
			f = service.getClass().getDeclaredField(name);
		} catch (SecurityException e) {
			throw new ServiceWrapperException(e);
		} catch (NoSuchFieldException e) {
			throw new ServiceException(ServiceErrorId.NoSuchParameter, name);
		}
		return f;
	}
	
	public static void setValue(Field f, Object parent, Object value) throws ServiceWrapperException  {
		f.setAccessible(true);
		try {
			f.set(parent, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new ServiceWrapperException(e);
		}
	}


	public static boolean hasServiceParamWithName(Service service, String paramName) {
		try {
			getParamByName(paramName, service);
		} catch (ServiceException e) {
			return false;
		}
		return true;
	}


	public static boolean isParamOptional(Field f) {
		return f.getAnnotation(Param.class).Optional();
	}
	
}
