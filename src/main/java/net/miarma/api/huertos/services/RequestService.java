package net.miarma.api.huertos.services;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import net.miarma.api.common.QueryFilters;
import net.miarma.api.huertos.dao.RequestDAO;
import net.miarma.api.huertos.entities.RequestEntity;

public class RequestService {

	private final RequestDAO requestDAO;

	public RequestService(Pool pool) {
		this.requestDAO = new RequestDAO(pool);
	}

	public Future<List<RequestEntity>> getAll(QueryFilters filters) {
		return requestDAO.getAll(filters);
	}

	public Future<RequestEntity> getById(Integer id) {
		return requestDAO.getAll().compose(requests -> {
			RequestEntity request = requests.stream()
				.filter(r -> r.getRequest_id().equals(id))
				.findFirst()
				.orElse(null);
			return Future.succeededFuture(request);
		});
	}

	public Future<RequestEntity> create(RequestEntity request) {
		return requestDAO.insert(request);
	}

	public Future<RequestEntity> update(RequestEntity request) {
		return requestDAO.update(request);
	}

	public Future<RequestEntity> delete(Integer id) {
		return requestDAO.delete(id);
	}
}
