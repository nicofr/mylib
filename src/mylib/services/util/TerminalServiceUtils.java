package mylib.services.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import mylib.services.ExportsTerminalService;
import mylib.services.annotations.ExportFlag;
import mylib.services.annotations.ExportParam;
import mylib.services.exceptions.ServiceErrorId;
import mylib.services.exceptions.ServiceException;
import mylib.services.exceptions.ServiceWrapperException;
import mylib.util.CollectionUtils;

public class TerminalServiceUtils {
	
	private TerminalServiceUtils() {}
	
	public static void validateService(ExportsTerminalService service) throws ServiceException {
		Collection<String> paramNames = CollectionUtils.empty();
		for (Field field : service.getClass().getDeclaredFields()) {
			if (isFieldServiceParameter(field)) {
				validateParam(field, paramNames);
			}
		}
	}
	
	public static Field getFlagByName(String name, ExportsTerminalService service) throws ServiceException {
		Field f;
		try {
			f = Stream.of(service.getClass().getDeclaredFields())
					.filter(element -> element.getAnnotation(ExportParam.class).Ident().equals(name)).findFirst().orElse(null);
		} catch (SecurityException e) {
			throw new ServiceWrapperException(e);
		}
		return f;
	}

	private static void validateParam(Field f, Collection<String> paramNames) throws ServiceException{
		if (paramNames.contains(getParamName(f))) throw new ServiceException(ServiceErrorId.ParamNameInUse, getParamName(f));
		if (!f.getType().equals(String.class) && !hasStringMapper(f) && !isFieldServiceFlag(f)) throw new ServiceException(ServiceErrorId.InvalidParameter, getParamName(f)+" is not of type String or has no String mapper");
		if (!(!isFieldServiceFlag(f) || f.getType().isAssignableFrom(Boolean.class))) throw new ServiceException(ServiceErrorId.InvalidParameter, getParamName(f)+" Flag is not of type boolean");
		if (f.getModifiers() != Modifier.PRIVATE) throw new ServiceException(ServiceErrorId.InvalidParameter, getParamName(f) + " is not private");
		paramNames.add(getParamName(f));
	}
	
	public static boolean isFieldServiceFlag(Field f) {
		return f.getAnnotation(ExportFlag.class) != null;
	}
	
	public static boolean isFieldServiceParameter(Field f) {
		return f.getAnnotation(ExportParam.class) != null || isFieldServiceFlag(f);
	}
	
	public static String getParamName(Field f) throws ServiceException {
		if (isFieldServiceParameter(f)) {
			return f.getAnnotation(ExportParam.class).Ident();
		}
		throw new ServiceException(ServiceErrorId.FieldIsNoParameter, f.getName());
	}
	
	private static boolean hasStringMapper(Field f) throws ServiceException {
		if (isFieldServiceParameter(f)) {
			return f.getAnnotation(ExportParam.class).MapperClass() != null &&  ParamStringMapper.class.isAssignableFrom(f.getAnnotation(ExportParam.class).MapperClass());
		}
		throw new ServiceException(ServiceErrorId.FieldIsNoParameter, f.getName());
	}
	
	public static ParamStringMapper<?> getMapper(Field f) throws ServiceWrapperException {
		try {
			return (ParamStringMapper<?>) f.getAnnotation(ExportParam.class).MapperClass().getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new ServiceWrapperException(e);
		}
	}

	public static Field getParamByName(String name, ExportsTerminalService service) throws ServiceException {
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


	public static boolean hasServiceParamWithName(ExportsTerminalService service, String paramName) {
		try {
			getParamByName(paramName, service);
		} catch (ServiceException e) {
			return false;
		}
		return true;
	}

	public static List<String> getParamArguments(int posParam, String... strings) {
		List<String> res = new ArrayList<>();
		for (int i = posParam+1; i < strings.length && !isArgParam(strings[i]); i++) {
			res.add(strings[i]);
		}
		return res;
	}

	public static boolean isParamOptional(Field f) {
		return f.getAnnotation(ExportParam.class).Optional();
	}


	public static boolean isArgParam(String string) {
		 return string.startsWith("-");
	}

	public static String parseParamName(String string) {
		return string.substring(1, string.length());
	}


	public static String printAllParamHelp(ExportsTerminalService service) {
		// TODO Auto-generated method stub
		return "";
	}
	
}
