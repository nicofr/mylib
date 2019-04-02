package mylib.services.components.standardmappers;

import mylib.services.util.ParamStringMapper;

public class StringToIntegerMapper implements ParamStringMapper<Integer> {

	@Override
	public Integer map(String in) {
		return Integer.parseInt(in);
	}

}
