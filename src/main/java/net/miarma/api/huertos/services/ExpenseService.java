package net.miarma.api.huertos.services;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import net.miarma.api.common.http.QueryParams;
import net.miarma.api.huertos.dao.ExpenseDAO;
import net.miarma.api.huertos.entities.ExpenseEntity;
import net.miarma.api.util.MessageUtil;

public class ExpenseService {

	private final ExpenseDAO expenseDAO;

	public ExpenseService(Pool pool) {
		this.expenseDAO = new ExpenseDAO(pool);
	}

	public Future<List<ExpenseEntity>> getAll(QueryParams params) {
		return expenseDAO.getAll(params);
	}

	public Future<ExpenseEntity> getById(Integer id) {
		return expenseDAO.getAll().compose(expenses -> {
			ExpenseEntity expense = expenses.stream()
				.filter(e -> e.getExpense_id().equals(id))
				.findFirst()
				.orElse(null);
			if (expense == null) {
				return Future.failedFuture(MessageUtil.notFound("Expense", "in the database"));
			}
			return Future.succeededFuture(expense);
		});
	}

	public Future<ExpenseEntity> create(ExpenseEntity expense) {
		return expenseDAO.insert(expense);
	}

	public Future<ExpenseEntity> update(ExpenseEntity expense) {
		return getById(expense.getExpense_id()).compose(existing -> {
			if (existing == null) {
				return Future.failedFuture(MessageUtil.notFound("Expense", "to update"));
			}
			return expenseDAO.update(expense);
		});
	}

	public Future<ExpenseEntity> delete(Integer id) {
		return getById(id).compose(existing -> {
			if (existing == null) {
				return Future.failedFuture(MessageUtil.notFound("Expense", "to delete"));
			}
			return expenseDAO.delete(id);
		});
	}
}
