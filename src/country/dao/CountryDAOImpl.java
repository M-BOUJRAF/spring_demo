package country.dao;

import country.model.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class CountryDAOImpl implements CountryDAO {
	private static final Logger LOGGER = Logger.getLogger(CountryDAOImpl.class.getName());
	@Autowired
	private DataSource dataSource;
	
	@Override
	public Country getByCode(String countryCode) {
		Country country = null;
		try {
			Connection connection = dataSource.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM country where code = ?;");
			preparedStatement.setString(1, countryCode);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				country = new Country();
				Integer id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				String code = resultSet.getString(3);
				String devise = resultSet.getString(4);
				String greetings = resultSet.getString(5);
				
				country.setId(id);
				country.setName(name);
				country.setCode(code);
				country.setDevise(devise);
				country.setGreetings(greetings);
				
			}
		} catch (SQLException exception) {
			LOGGER.log(Level.SEVERE, "Exception while accessing the database", exception);
		}
		return country;
	}

	@Override
	public Country saveCountry(Country c) {
			
		try {
			Connection connection = dataSource.getConnection();
			PreparedStatement pS = connection.prepareStatement("INSERT INTO country(name, code, devise, greetings) VALUES(?,?,?,?);");
			pS.setString(1, c.getName());
			pS.setString(2, c.getCode());
			pS.setString(3, c.getDevise());
			pS.setString(4, c.getGreetings());
			pS.executeUpdate();
			pS.close();
			PreparedStatement pS2 = connection.prepareStatement("SELECT MAX(id) AS MAX_ID FROM country");
			ResultSet rs = pS2.executeQuery();
			if(rs.next()) {
				c.setId(rs.getInt("MAX_ID"));
			}
			pS2.close();
		} catch( SQLException e) {
			e.printStackTrace();
		}
		return c;
	}

	@Override
	public void deleteCountry(String code) {
		try {
			
		Connection connection = dataSource.getConnection();
		PreparedStatement pS = connection.prepareStatement("DELETE FROM country WHERE code=?");
		pS.setString(1, code);
		pS.executeUpdate();
		pS.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
}
