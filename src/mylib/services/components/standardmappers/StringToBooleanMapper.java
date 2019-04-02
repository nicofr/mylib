package mylib.services.components.standardmappers;

import mylib.services.util.ParamStringMapper;

public class StringToBooleanMapper implements ParamStringMapper<Boolean> {

	@Override
	public Boolean map(String in) {
		return Boolean.parseBoolean(in);
	}

}
