package controller.filter;

import java.util.Set;

public abstract class AbsFilter {
	public abstract String toString();
	
	public abstract Set<String> toTablesSet();
}
