package com.org.util;

import java.util.List;

import javax.persistence.TypedQuery;

public class QueryUtil {

	public static <T> T getUniqueResult(TypedQuery<T> query) {
		List<T> results = query.getResultList();
		if (results.size() == 1) {
			return results.get(0);
		} else if (results.isEmpty()) {
			return null;
		} else {
			throw new RuntimeException(
					"Multiple Entities found for TypedQuery : "
							+ query.toString());
		}
	}

	public static <T> T getFirstResult(TypedQuery<T> query) {
		List<T> results = query.getResultList();
		if (results.isEmpty()) {
			return null;
		} else {
			return results.get(0);
		}
	}

}
