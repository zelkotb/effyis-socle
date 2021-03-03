package effyis.partners.socle.datasource;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.security.core.context.SecurityContextHolder;

import effyis.partners.socle.exception.DataSourceNotFoundException;

/**
 * 
 * @author EL KOTB ZAKARIA
 *
 */
public class MyRoutingDataSource extends AbstractRoutingDataSource {

	@Autowired
	private HttpServletRequest request;

	@Override
	protected Object determineCurrentLookupKey() {
		String keyDB;
		if (SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
			keyDB = this.request.getHeader("keyDB");
			if (keyDB == null) {
				keyDB = "client1";
			}
		} else {
			keyDB = TenantStorageContext.getTenantId();
		}
		return keyDB;
	}

	@Override
	protected DataSource determineTargetDataSource() {
		try {
			DataSource ds = super.determineTargetDataSource();
			return ds;
		} catch (IllegalStateException e) {
			throw new DataSourceNotFoundException(this.request.getHeader("keyDB"));
		}
	}

}
