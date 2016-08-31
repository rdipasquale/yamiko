package ar.edu.ungs.census;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class IntRowMapper implements RowMapper<Integer>{

	public IntRowMapper() {
	}
	
	@Override
	public Integer mapRow(ResultSet arg0, int arg1) throws SQLException {
		return arg0.getInt(1);
	}
}
