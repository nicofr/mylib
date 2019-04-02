package mylib.services.components.standardmappers;

import mylib.services.util.ParamStringMapper;

public class IdentityMapper implements ParamStringMapper<String> {

	@Override
	public String map(String in) {
		return in;
	}

}
