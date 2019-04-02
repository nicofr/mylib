package mylib.services.components.standardmappers;

import java.util.Arrays;
import java.util.List;

import mylib.services.util.ParamStringMapper;

public class StringToStringListMapper implements ParamStringMapper<List<String>> {

	@Override
	public List<String> map(String in) {
		return Arrays.asList(in.split(","));
	}

}
